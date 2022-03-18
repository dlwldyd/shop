package com.example.shop.service;

import com.example.shop.domain.Member;
import com.example.shop.repository.member.MemberRepository;
import com.example.shop.security.member.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> findMember = memberRepository.findByUsername(username);

        if (findMember.isEmpty()) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(findMember.get().getRole().getDescription()));

        return new MemberContext(findMember.get(), roles);
    }
}