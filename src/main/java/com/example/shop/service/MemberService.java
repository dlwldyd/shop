package com.example.shop.service;

import com.example.shop.domain.entity.Member;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
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
