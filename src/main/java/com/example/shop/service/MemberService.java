package com.example.shop.service;

import com.example.shop.domain.Member;
import com.example.shop.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

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

        Optional<Member> findMember = memberRepository.findByUsername(member.getUsername());

        if (findMember.isPresent()) {
            if (findMember.get().getPhone().equals(member.getPhone())) {
                throw new IllegalStateException("이미 가입된 회원 입니다.");
            }
            throw new IllegalStateException("중복된 아이디 입니다.");
        }
    }

    public Member getMemberByUsername(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);

        if (findMember.isPresent()) {
            return findMember.get();
        } else {
            throw new EntityNotFoundException("로그인을 해주세요");
        }
    }
}
