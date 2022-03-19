package com.example.shop.service;

import com.example.shop.Dtos.order.OrderDto;
import com.example.shop.Dtos.order.OrderHistDto;
import com.example.shop.Dtos.order.OrderItemDto;
import com.example.shop.domain.*;
import com.example.shop.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    @Transactional
    public Order order(OrderDto orderDto, String username) {
        Item item = itemService.getItem(orderDto.getItemId());
        Member member = memberService.getMemberByUsername(username);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order;
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new EntityNotFoundException();
        }
        order.get().cancelOrder();
    }

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
