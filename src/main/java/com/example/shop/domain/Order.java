package com.example.shop.domain;

import com.example.shop.domain.baseentity.DateBaseEntity;
import com.example.shop.enumtype.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends DateBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String merchantUid;

    private String impUid;

    private long totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(Member member, LocalDateTime orderDate, OrderStatus orderStatus, String merchantUid, String impUid, long totalPrice) {
        this.member = member;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.merchantUid = merchantUid;
        this.impUid = impUid;
        this.totalPrice = totalPrice;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * Order 엔티티를 생성하기 위한 메서드
     * @param member Order 와 연관관계에 있는 Member 엔티티
     * @param orderItemList Order 와 연관관계에 있는 OrderItem 의 리스트
     * @param merchantUid 주문 번호
     * @param pgUid PG 사측 결제 번호
     * @return 생성된 Order 엔티티
     */
    public static Order createOrder(Member member, List<OrderItem> orderItemList, String merchantUid, String pgUid, long totalPrice) {
        Order order = new Order(member, LocalDateTime.now(), OrderStatus.ORDER, merchantUid, pgUid, totalPrice);
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    /**
     * 총 결제 금액 계산을 위한 메서드
     * @return 총 결제 금액
     */
    public int getTotalPrice() {
        int totalPrice = 0;

        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}