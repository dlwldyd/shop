package com.example.shop.domain.entity;

import com.example.shop.domain.embeddable.Address;
import com.example.shop.domain.entity.baseentity.DateEntity;
import com.example.shop.domain.enumtype.Role;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Member extends DateEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String email;

    private String password;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Role role;
}
