package com.example.shop.service;

import com.example.shop.Dtos.item.AdminItemFormDto;
import com.example.shop.Dtos.order.OrderDto;
import com.example.shop.domain.Item;
import com.example.shop.domain.Order;
import com.example.shop.enumtype.ItemCategory;
import com.example.shop.enumtype.ItemStatus;
import com.example.shop.enumtype.OrderStatus;
import com.example.shop.exception.DeletedItemException;
import com.example.shop.exception.OutOfStockException;
import com.example.shop.repository.item.ItemRepository;
import com.example.shop.repository.order.OrderRepository;
import com.example.shop.service.payment.PaymentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class OrderServiceTest2 {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EntityManager em;

    @MockBean
    private PaymentService paymentService;

    @Test
    @Transactional
    void cancelTest() throws IOException {
        Item item = itemRepository.save(new Item("맥북", 1000, 100, ItemStatus.SELL, ItemCategory.LAPTOP, "asldkfj"));

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(2L);
        orderDto.setImpUid("tmp");
        orderDto.setMerchantUid("tmp");
        orderDto.setCount(1);
        Order order = orderService.order(orderDto, "admin", 10000);

        assertThat(item.getStockQuantity()).isEqualTo(99);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDER);

        orderService.cancelOrder(order);

        assertThat(item.getStockQuantity()).isEqualTo(100);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    @Rollback(false)
    void orderTest() throws IOException {
        itemRepository.save(new Item("맥북", 1000, 100, ItemStatus.SELL, ItemCategory.LAPTOP, "asldkfj"));

        doNothing().when(paymentService).outOfStock(anyString());

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(2L);
        orderDto.setImpUid("tmp");
        orderDto.setMerchantUid("tmp");
        orderDto.setCount(1000);
        assertThatThrownBy(() -> orderService.order(orderDto, "admin", 1000000)).isInstanceOf(OutOfStockException.class);
    }

    @Test
    @Rollback(false)
    void ordersTest() throws IOException {
        itemRepository.save(new Item("맥북", 1000, 100, ItemStatus.SELL, ItemCategory.LAPTOP, "asldkfj"));
        itemRepository.save(new Item("맥북", 1000, 100, ItemStatus.DELETED, ItemCategory.LAPTOP, "asldkfj"));
        itemRepository.save(new Item("맥북", 1000, 100, ItemStatus.SELL, ItemCategory.LAPTOP, "asldkfj"));
        itemRepository.save(new Item("맥북", 1000, 100, ItemStatus.SELL, ItemCategory.LAPTOP, "asldkfj"));

        List<OrderDto> orderDtoList = new ArrayList<>();

        OrderDto orderDto1 = new OrderDto();
        orderDto1.setItemId(2L);
        orderDto1.setImpUid("tmp");
        orderDto1.setMerchantUid("tmp");
        orderDto1.setCount(10);

        OrderDto orderDto2 = new OrderDto();
        orderDto2.setItemId(3L);
        orderDto2.setImpUid("tmp");
        orderDto2.setMerchantUid("tmp");
        orderDto2.setCount(10);

        OrderDto orderDto3 = new OrderDto();
        orderDto3.setItemId(4L);
        orderDto3.setImpUid("tmp");
        orderDto3.setMerchantUid("tmp");
        orderDto3.setCount(10);

        OrderDto orderDto4 = new OrderDto();
        orderDto4.setItemId(5L);
        orderDto4.setImpUid("tmp");
        orderDto4.setMerchantUid("tmp");
        orderDto4.setCount(1000);

        orderDtoList.add(orderDto1);
        orderDtoList.add(orderDto2);
        orderDtoList.add(orderDto3);
        orderDtoList.add(orderDto4);

        doNothing().when(paymentService).outOfStock(anyString());

        assertThatThrownBy(() -> orderService.orders(orderDtoList, "admin", "tmp", "tmp", 1003000)).isInstanceOf(DeletedItemException.class);
    }
}