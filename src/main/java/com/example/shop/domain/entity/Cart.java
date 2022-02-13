package com.example.shop.domain.entity;

import com.example.shop.domain.entity.baseentity.DateEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Cart extends DateEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
