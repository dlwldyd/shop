package com.example.shop.domain.entity;

import com.example.shop.Dtos.MemberFormDto;
import com.example.shop.domain.entity.baseentity.DateBaseEntity;
import com.example.shop.domain.enumtype.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends DateBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(updatable = false)
    private String username;

    private String name;

    private String email;

    private String password;

    private String address;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Member(String username, String name, String email, String password, String address, String phone, Role role) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.role = role;
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        return new Member(memberFormDto.getUsername(),
                memberFormDto.getName(),
                memberFormDto.getEmail(),
                passwordEncoder.encode(memberFormDto.getPassword()),
                memberFormDto.getAddress(),
                memberFormDto.getPhone(),
                Role.USER);
    }
}
