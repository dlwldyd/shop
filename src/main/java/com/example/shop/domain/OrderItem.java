package com.example.shop.domain;

import com.example.shop.domain.baseentity.DateBaseEntity;
import com.example.shop.enumtype.ItemStatus;
import com.example.shop.exception.SoldOutException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends DateBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int itemPrice;

    private int totalPrice;

    private int count;

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderItem(Item item, int orderPrice, int totalPrice, int count) {
        this.item = item;
        this.itemPrice = orderPrice;
        this.totalPrice = totalPrice;
        this.count = count;
    }

    public static OrderItem createOrderItem(Item item, int count) {
        if (item.getStatus() == ItemStatus.SELL) {
            item.removeStock(count);
            return new OrderItem(item, item.getPrice(), item.getPrice() * count, count);
        } else {
            throw new SoldOutException("해당 상품은 품절되었습니다.");
        }
    }
}
