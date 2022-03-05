package com.example.shop.controller;

import com.example.shop.Dtos.MemberFormDto;
import com.example.shop.domain.entity.Member;
import com.example.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration() {
        return "member/registration";
    }

    @PostMapping("/registration")
    public String createMember(MemberFormDto memberFormDto, BindingResult bindingResult) {
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        memberService.saveMember(member);
        return "redirect:/";
    }
}
