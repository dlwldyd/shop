package com.example.shop.domain;

import com.example.shop.domain.baseentity.DateBaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Cart extends DateBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}