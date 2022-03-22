package com.example.shop.controller;

import com.example.shop.Dtos.member.MemberFormDto;
import com.example.shop.domain.Member;
import com.example.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 화면
     */
    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("member", new MemberFormDto());
        return "member/registration";
    }

    /**
     * 회원가입
     */
    @PostMapping("/registration")
    public String createMember(@Validated @ModelAttribute("member") MemberFormDto memberFormDto, BindingResult bindingResult) {

        if (!memberFormDto.getPassword().equals(memberFormDto.getRePassword())) {
            bindingResult.rejectValue("rePassword", "passwordMismatch");
        }

        if (bindingResult.hasErrors()) {
            return "member/registration";
        }

        Member member = Member.createMember(memberFormDto, passwordEncoder);
        memberService.saveMember(member);
        return "redirect:/";
    }
}
