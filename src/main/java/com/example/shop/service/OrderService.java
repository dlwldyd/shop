package com.example.shop.service;

import com.example.shop.Dtos.order.OrderDto;
import com.example.shop.domain.Item;
import com.example.shop.domain.Member;
import com.example.shop.domain.Order;
import com.example.shop.domain.OrderItem;
import com.example.shop.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final ItemService itemService;
    private final MemberService memberService;
    private final OrderRepository orderRepository;

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
}
