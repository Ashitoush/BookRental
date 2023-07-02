package com.example.BookRental.service.ServiceImpl;

import com.example.BookRental.config.CustomMessageSource;
import com.example.BookRental.converter.MemberConverter;
import com.example.BookRental.dto.MemberDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.mapper.MemberMapper;
import com.example.BookRental.model.BookTransaction;
import com.example.BookRental.model.Member;
import com.example.BookRental.model.RENT_TYPE;
import com.example.BookRental.repo.BookTransactionRepo;
import com.example.BookRental.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberConverter memberConverter;
    private final MemberMapper memberMapper;
    private final BookTransactionRepo bookTransactionRepo;
    private final CustomMessageSource messageSource;

    @Override
    public ResponseEntity<Object> insertMember(MemberDto memberDto) {
        Member member = memberConverter.toEntity(memberDto);

        Integer count = memberMapper.insertMember(member);

        if (count == 0) {
            throw new CustomException(messageSource.get("member.insert.error"));
        }

        return new ResponseEntity<>(messageSource.get("member.insert.success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllMember() {
        List<Member> memberList = memberMapper.getAllMember();

        if (memberList.isEmpty()) {
            throw new CustomException(messageSource.get("member.read.error"));
        }
        List<MemberDto> memberDtoList = memberConverter.toDto(memberList);

        return new ResponseEntity<>(memberDtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getMemberById(Long id) {
        Member member = memberMapper.getMemberById(id);
        if (member == null) {
            throw new CustomException(messageSource.get("member.not.found"));
        }

        MemberDto memberDto = memberConverter.toDto(member);
        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateMember(MemberDto memberDto) {
        Member member = memberMapper.getMemberById(memberDto.getId());
        if (member == null) {
            throw new CustomException(messageSource.get("member.not.found"));
        } else {
            member.setName(memberDto.getName());
            member.setAddress(memberDto.getAddress());
            member.setEmail(memberDto.getEmail());
            member.setMobileNo(memberDto.getMobileNo());
        }

        Integer count = memberMapper.updateMember(member);
        if(count == 0) {
            throw new CustomException(messageSource.get("member.update.error"));
        }

        return new ResponseEntity<>(messageSource.get("member.update.success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteMember(Long id) {
        Member member = memberMapper.getMemberById(id);
        if(member == null) {
            throw new CustomException(messageSource.get("member.not.found"));
        }

        List<BookTransaction> bookTransactionList = bookTransactionRepo.findByMemberId(member.getId());

        for (BookTransaction bookTransaction : bookTransactionList) {
            if (bookTransaction.getRentStatus() == RENT_TYPE.RENT) {
                throw new CustomException(messageSource.get("member.delete.returnAll.error"));
            }
        }

        member.setBookTransaction(null);

        Integer count = memberMapper.deleteMember(id);

        if(count == 0) {
            throw new CustomException(messageSource.get("member.delete.error"));
        }

        return new ResponseEntity<>(messageSource.get("member.delete.success"), HttpStatus.OK);
    }
}
