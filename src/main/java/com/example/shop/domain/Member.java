package com.example.shop.domain;

import com.example.shop.Dtos.member.MemberFormDto;
import com.example.shop.domain.baseentity.DateBaseEntity;
import com.example.shop.enumtype.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Date;

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

    /**
     * Member 엔티티 생성을 위한 메서드,
     * 기본적으로 ROLE_USER 로 생성
     * @param memberFormDto 회원가입 폼에서 받은 사용자 정보
     * @param passwordEncoder 패스워드 인코딩을 위한 passwordEncoder
     * @return 생성된 Member 엔티티를 반환
     */
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
