package com.example.shop.service.impl;

import com.example.shop.domain.entity.Member;
import com.example.shop.repository.MemberRepository;
import com.example.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByUsername(member.getUsername());
        if (findMember != null) {
            if (findMember.getPhone().equals(member.getPhone())) {
                throw new IllegalStateException("이미 가입된 회원 입니다.");
            }
            throw new IllegalStateException("중복된 아이디 입니다.");
        }
    }
}
