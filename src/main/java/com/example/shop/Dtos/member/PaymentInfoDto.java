package com.example.shop.Dtos.member;

import com.example.shop.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentInfoDto {

    private String name;

    private String email;

    private String phone;

    private String address;

    public static PaymentInfoDto of(Member member) {
        return new PaymentInfoDto(
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getAddress());
    }
}
