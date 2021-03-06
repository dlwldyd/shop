package com.example.shop.service.impl;

import com.example.shop.domain.Member;
import com.example.shop.enumtype.Role;
import com.example.shop.repository.member.MemberRepository;
import com.example.shop.security.member.MemberContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CustomUserDetailsServiceTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void save() {
        memberRepository.save(new Member(
                "user",
                "user",
                "weofij@naver.com",
                passwordEncoder.encode("11111111"),
                "sdofij",
                "01000000000", Role.USER));
    }

    @Test
    void userDetailsTest() {

        MemberContext userDetails = (MemberContext) userDetailsService.loadUserByUsername("user");
        assertThat(userDetails.getMember().getUsername()).isEqualTo("user");
        assertThat(userDetails.getMember().getName()).isEqualTo("user");
        assertThat(userDetails.getMember().getPhone()).isEqualTo("01000000000");
        assertThat(userDetails.getMember().getRole()).isEqualTo(Role.USER);
        assertThat(userDetails.getMember().getEmail()).isEqualTo("weofij@naver.com");
        assertThat(userDetails.getMember().getAddress()).isEqualTo("sdofij");
    }
}