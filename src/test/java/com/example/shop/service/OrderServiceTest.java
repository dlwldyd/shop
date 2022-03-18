package com.example.shop.service;

import com.example.shop.Dtos.order.OrderDto;
import com.example.shop.domain.Item;
import com.example.shop.domain.Member;
import com.example.shop.domain.Order;
import com.example.shop.enumtype.ItemCategory;
import com.example.shop.enumtype.ItemStatus;
import com.example.shop.enumtype.Role;
import com.example.shop.repository.order.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ItemService itemService;

    @Mock
    private MemberService memberService;

    @Mock
    private OrderRepository orderRepository;

    @Test
    void orderTest() {
        when(itemService.getItem(anyLong())).thenReturn(new Item("테스트", 100000, 300, ItemStatus.SELL, ItemCategory.KEYBOARD, "테스트"));
        when(memberService.getMemberByUsername(anyString())).thenReturn(new Member("유저", "홍길동", "weiffb@naver.com", "asvkn", "서울", "01000000000", Role.USER));
        when(orderRepository.save(any())).thenReturn(new Order(new Member(null, null, null, null, null, null, null), LocalDateTime.now(), null));

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(3L);
        orderDto.setCount(50);
        Order order = orderService.order(orderDto, "유저");

        assertThat(order.getTotalPrice()).isEqualTo(5000000);
        assertThat(order.getOrderItems().size()).isEqualTo(1);
        assertThat(order.getMember().getUsername()).isEqualTo("유저");
    }
}