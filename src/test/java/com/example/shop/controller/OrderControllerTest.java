package com.example.shop.controller;

import com.example.shop.Dtos.order.OrderDto;
import com.example.shop.domain.Order;
import com.example.shop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.security.Principal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Order order;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @Test
    void controllerTest() throws IOException {

        MockedStatic<SecurityContextHolder> mSecurityContextHolder = mockStatic(SecurityContextHolder.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(order.getId()).thenReturn(5L);
        when(orderService.order(any(), any(), anyLong())).thenReturn(order);
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));
        when(userDetails.getUsername()).thenReturn("홍길동");

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(5L);
        orderDto.setCount(5);

        ResponseEntity<String> result = orderController.order(orderDto, bindingResult, (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("5");

        mSecurityContextHolder.close();
    }
}