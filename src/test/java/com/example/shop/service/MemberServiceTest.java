package com.example.shop.service;

import com.example.shop.Dtos.member.MemberFormDto;
import com.example.shop.domain.entity.Member;
import com.example.shop.domain.enumtype.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void saveMemberTest() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setUsername("dlwldyd");
        memberFormDto.setName("이지용");
        memberFormDto.setPassword("11111111");
        memberFormDto.setAddress("부산");
        memberFormDto.setEmail("dlwldyd@naver.com");
        memberFormDto.setPhone("01000000000");
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        Member savedMember = memberService.saveMember(member);

        assertThat(member.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(member.getName()).isEqualTo(savedMember.getName());
        assertThat(member.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(member.getAddress()).isEqualTo(savedMember.getAddress());
        assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(member.getPhone()).isEqualTo(savedMember.getPhone());
        assertThat(savedMember.getRole()).isEqualTo(Role.USER);
    }
}