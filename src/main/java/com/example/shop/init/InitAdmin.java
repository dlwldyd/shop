package com.example.shop.init;

import com.example.shop.domain.entity.Member;
import com.example.shop.domain.enumtype.Role;
import com.example.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitAdmin {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        memberService.saveMember(new Member(
                "admin",
                "관리자",
                "aaaa@naver.com",
                passwordEncoder.encode("admin"),
                "서울",
                "01011111111",
                Role.ADMIN));
    }
}