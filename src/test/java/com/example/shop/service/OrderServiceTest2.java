package com.example.shop.service;

import com.example.shop.domain.Member;
import com.example.shop.domain.Order;
import com.example.shop.enumtype.OrderStatus;
import com.example.shop.repository.order.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest2 {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Member member;

    @Test
    void cancelTest() {
        Order order = new Order(member, LocalDateTime.now(), OrderStatus.ORDER);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }

}