package com.example.shop.service;

import com.example.shop.Dtos.order.OrderDto;
import com.example.shop.Dtos.order.OrderHistDto;
import com.example.shop.Dtos.order.OrderItemDto;
import com.example.shop.domain.*;
import com.example.shop.enumtype.ItemStatus;
import com.example.shop.enumtype.OrderStatus;
import com.example.shop.exception.DeletedItemException;
import com.example.shop.exception.OutOfStockException;
import com.example.shop.repository.order.OrderRepository;
import com.example.shop.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final ItemService itemService;
    private final MemberService memberService;
    private final OrderRepository orderRepository;
    private final ItemImgService itemImgService;
    private final PaymentService paymentService;

    /**
     * 단일 상품 주문을 위한 메서드,
     * OrderItem, Order 엔티티를 생성하고 저장함
     * @param orderDto 주문할 상품 id 와 개수가 담긴 DTO
     * @param username 사용자 아이디
     * @param totalPrice 결제할 금액
     */
    @Transactional(rollbackFor = Exception.class)
    public Order order(OrderDto orderDto, String username, long totalPrice) throws IOException {

        Item item = itemService.getItemForUpdateStock(orderDto.getItemId());

        try {

            if (item.getStatus() == ItemStatus.DELETED){
                throw new DeletedItemException("삭제된 상품입니다. : " + item.getItemName());
            }
            Member member = memberService.getMemberByUsername(username);

            List<OrderItem> orderItemList = new ArrayList<>();

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);

            Order order = Order.createOrder(member, orderItemList, orderDto.getMerchantUid(), orderDto.getImpUid(), totalPrice);
            orderRepository.save(order);

            return order;
        } catch (OutOfStockException | DeletedItemException e) {
            paymentService.outOfStock(orderDto.getImpUid());
            throw e;
        }
    }

    /**
     * 주문 취소 메서드
     * @param order 취소할 주문
     */
    @Transactional
    public void cancelOrder(Order order) {
        order.getOrderItems().forEach(orderItem -> {
            Item item = itemService.getItemForUpdateStock(orderItem.getItem().getId());
            item.addStock(orderItem.getCount());
        });
        order.setOrderStatus(OrderStatus.CANCEL);
    }

    /**
     * 장바구니에 담긴 상품들을 주문하기 위한 메서드
     * @param orderDtoList 주문할 상품의 id와 개수가 담긴 DTO 의 리스트
     * @param username 사용자 아이디
     * @param merchantUid 주문 번호
     * @param pgUid PG 사측 결제 번호
     */
    @Transactional(rollbackFor = Exception.class)
    public Order orders(List<OrderDto> orderDtoList, String username, String merchantUid, String pgUid, long totalPrice) throws IOException {

        Member member = memberService.getMemberByUsername(username);
        List<OrderItem> orderItemList = new ArrayList<>();
        List<Long> itemIdList = new ArrayList<>();

        orderDtoList.sort((o1, o2) -> {
            if (o1.getItemId().equals(o2.getItemId())) {
                return 0;
            } else if (o1.getItemId() > o2.getItemId()) {
                return 1;
            } else {
                return -1;
            }
        });
        orderDtoList.forEach(orderDto -> itemIdList.add(orderDto.getItemId()));

        List<Item> itemList = itemService.getItemListForUpdateStock(itemIdList);

        try {
            if (itemList.size() != itemIdList.size()) {
                throw new EntityNotFoundException("존재하지 않는 상품을 주문했습니다.");
            }
            for (int i = 0; i < itemIdList.size(); i++) {
                if (!itemList.get(i).getId().equals(itemIdList.get(i))) {
                    throw new EntityNotFoundException("잘못된 데이터 조회");
                }
                Item item = itemList.get(i);
                if (item.getStatus() == ItemStatus.DELETED) {
                    throw new DeletedItemException("삭제된 상품입니다. : " + item.getItemName());
                }
                OrderItem orderItem = OrderItem.createOrderItem(item, orderDtoList.get(i).getCount());
                orderItemList.add(orderItem);
            }

            Order order = Order.createOrder(member, orderItemList, merchantUid, pgUid, totalPrice);
            return orderRepository.save(order);

        } catch (OutOfStockException | DeletedItemException e) {
            paymentService.outOfStock(pgUid);
            throw e;
        }
    }

    /**
     * 사용자의 주문 내역을 받아오는 메서드
     * @param username 사용자 아이디
     * @param pageable 페이징
     * @return 주문일자, 주문 상태(취소됐는지 아닌지), 결제금액이 담긴 OrderHistDto 의 Page 객체
     */
    public Page<OrderHistDto> getOrderList(String username, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(username, pageable);
        Long count = orderRepository.countOrder(username);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {

                ItemImg itemRepImg = itemImgService.getItemRepImg(orderItem.getItem().getId());
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemRepImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<>(orderHistDtos, pageable, count);
    }

    public Order getOrderWithOrderItems(Long orderId) {
        return orderRepository.findByIdWithOrderItems(orderId).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * 해당 사용자가 Order 엔티티를 수정할 권한이 있는지 검사하는 메서드
     * @param orderId Order 엔티티의 id
     * @param username 사용자 아이디
     * @return true 면 수정 가능, false 면 수정 불가
     */
    public boolean validateOrder(Long orderId, String username) {
        Member loginMember = memberService.getMemberByUsername(username);
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Member orderMember = order.get().getMember();

        return loginMember.getUsername().equals(orderMember.getUsername());
    }
}
