package com.example.BookRental.converter;

import com.example.BookRental.dto.MemberDto;
import com.example.BookRental.model.Member;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemberConverter {

    public MemberDto toDto(Member member) {
        MemberDto memberDto = new MemberDto();

        memberDto.setId(member.getId());
        memberDto.setName(member.getName());
        memberDto.setEmail(member.getEmail());
        memberDto.setAddress(member.getAddress());
        memberDto.setMobileNo(member.getMobileNo());

        return memberDto;
    }

    public List<MemberDto> toDto(List<Member> memberList) {
        List<MemberDto> memberDtoList = new ArrayList<>();

        for (Member member : memberList) {
            MemberDto memberDto = new MemberDto();

            memberDto.setId(member.getId());
            memberDto.setName(member.getName());
            memberDto.setEmail(member.getEmail());
            memberDto.setAddress(member.getAddress());
            memberDto.setMobileNo(member.getMobileNo());

            memberDtoList.add(memberDto);
        }
        return memberDtoList;
    }

    public Member toEntity(MemberDto memberDto) {
        Member member = new Member();

        member.setId(memberDto.getId());
        member.setName(memberDto.getName());
        member.setEmail(memberDto.getEmail());
        member.setAddress(memberDto.getAddress());
        member.setMobileNo(memberDto.getMobileNo());

        return member;
    }

    public List<Member> toEntity(List<MemberDto> memberDtoList) {
        List<Member> memberList = new ArrayList<>();

        for (MemberDto memberDto : memberDtoList) {
            Member member = new Member();

            member.setId(memberDto.getId());
            member.setName(memberDto.getName());
            member.setEmail(memberDto.getEmail());
            member.setAddress(memberDto.getAddress());
            member.setMobileNo(memberDto.getMobileNo());

            memberList.add(member);
        }
        return memberList;
    }
}
