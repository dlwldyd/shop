package com.example.shop.Dtos.order;

import com.example.shop.domain.Order;
import com.example.shop.enumtype.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {

    private Long orderId;

    private LocalDateTime orderDate;

    private OrderStatus orderStatus;

    private long totalPrice = 0;

    private String merchantUid;

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getOrderStatus();
        this.merchantUid = order.getMerchantUid();
    }

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        this.orderItemDtoList.add(orderItemDto);
        this.totalPrice += orderItemDto.getTotalPrice();
    }
}
