package com.example.BookRental.controller;

import com.example.BookRental.dto.MemberDto;
import com.example.BookRental.helper.CheckValidation;
import com.example.BookRental.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final CheckValidation validation;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> insertMember(@Valid @RequestBody MemberDto memberDto, BindingResult result) {
        validation.checkValidation(result);
        return memberService.insertMember(memberDto);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllMember() {
        return memberService.getAllMember();
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getMemberById(@PathVariable("id") Long id) {
        return memberService.getMemberById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long id, @Valid @RequestBody MemberDto memberDto, BindingResult result) {
        validation.checkValidation(result);
        memberDto.setId(id);
        return memberService.updateMember(memberDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteMember(@PathVariable("id") Long id) {
        return memberService.deleteMember(id);
    }
}
