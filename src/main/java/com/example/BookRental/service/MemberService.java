package com.example.BookRental.service;

import com.example.BookRental.dto.MemberDto;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    ResponseEntity<Object> insertMember(MemberDto memberDto);
    ResponseEntity<Object> getAllMember();
    ResponseEntity<Object> getMemberById(Long id);
    ResponseEntity<Object> updateMember(MemberDto memberDto);
    ResponseEntity<Object> deleteMember(Long id);
}
