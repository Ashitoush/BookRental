package com.example.BookRental.mapper;

import com.example.BookRental.model.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    Integer insertMember(Member member);
    List<Member> getAllMember();
    Member getMemberById(Long id);
    Integer updateMember(Member member);
    Integer deleteMember(Long id);
}
