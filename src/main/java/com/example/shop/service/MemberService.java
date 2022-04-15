package com.example.shop.service;

import com.example.shop.domain.Member;
import com.example.shop.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입 메서드,
     * 데이터베이스에 Member 엔티티 저장
     * @param member 저장할 Member 엔티티
     * @return 데이터베이스에 저장된 Member 엔티티
     */
    @Transactional
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    /**
     * 중복된 아이디인지 검사하는 메서드,
     * 같은 이름의 사용자 아이디가 데이터베이스에 존재하면 예외 발생
     * @param member 데이터베이스에 저장할 Member 엔티티
     */
    private void validateDuplicateMember(Member member) {

        Optional<Member> findMember = memberRepository.findByUsername(member.getUsername());

        if (findMember.isPresent()) {
            if (findMember.get().getPhone().equals(member.getPhone())) {
                throw new IllegalStateException("이미 가입된 회원 입니다.");
            }
            throw new IllegalStateException("중복된 아이디 입니다.");
        }
    }

    /**
     * 사용자 아이디로 데이터베이스에서 Member 엔티티를 가져오는 메서드
     * @param username 사용자 아이디
     * @return 데이터베이스에서 찾은 Member 엔티티
     */
    public Member getMemberByUsername(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);

        if (findMember.isPresent()) {
            return findMember.get();
        } else {
            throw new EntityNotFoundException("로그인을 해주세요");
        }
    }
}
