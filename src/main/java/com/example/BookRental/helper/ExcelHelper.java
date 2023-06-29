package com.example.BookRental.helper;

import com.example.BookRental.exception.CustomException;
import com.example.BookRental.mapper.MemberMapper;
import com.example.BookRental.model.Book;
import com.example.BookRental.model.BookTransaction;
import com.example.BookRental.model.Member;
import com.example.BookRental.model.RENT_TYPE;
import com.example.BookRental.repo.BookRepo;
import com.example.BookRental.repo.BookTransactionRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RequiredArgsConstructor
@Component
public class ExcelHelper {

    private final BookRepo bookRepo;
    private final MemberMapper memberMapper;
    private final BookTransactionRepo bookTransactionRepo;

    public ByteArrayInputStream createExcel(List<BookTransaction> bookTransactionList) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet spreadSheet = workbook.createSheet("Book Transaction");

        XSSFRow row = spreadSheet.createRow(0);

        XSSFCell cell;

        cell = row.createCell(0);
        cell.setCellValue("Id");

        cell = row.createCell(1);
        cell.setCellValue("Transaction Code");

        cell = row.createCell(2);
        cell.setCellValue("From Date");

        cell = row.createCell(3);
        cell.setCellValue("Rent Status");

        cell = row.createCell(4);
        cell.setCellValue("To Date");

        cell = row.createCell(5);
        cell.setCellValue("Book Name");

        cell = row.createCell(6);
        cell.setCellValue("Member Name");

        Integer i = 1;

        for (BookTransaction bookTransaction : bookTransactionList) {
            row = spreadSheet.createRow(i);

            cell = row.createCell(0);
            cell.setCellValue(bookTransaction.getId());

            cell = row.createCell(1);
            cell.setCellValue(bookTransaction.getCode());

            cell = row.createCell(2);
            cell.setCellValue(bookTransaction.getFromDate().toString());

            cell = row.createCell(3);
            cell.setCellValue(bookTransaction.getRentStatus().toString());

            cell = row.createCell(4);
            cell.setCellValue(bookTransaction.getToDate().toString());

            cell = row.createCell(5);
            cell.setCellValue(bookTransaction.getBook().getName());

            cell = row.createCell(6);
            cell.setCellValue(bookTransaction.getMember().getName());

            i++;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }


//        FileOutputStream out = null;
//        try {
//            File file = new File("C:\\Users\\admin\\Searches\\Desktop\\Projects\\BookRental\\BookRental\\src\\main\\resources\\static\\bookTransaction.xlsx");
//
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            out = new FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            throw new CustomException(e.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//
//        try {
//            workbook.write(out);
//        } catch (IOException e) {
//            throw new CustomException("There was an error while writing in the file");
//        }
//
//        try {
//            out.close();
//        } catch (IOException e) {
//            throw new CustomException(e.getMessage());
//        }
    }

    public List<BookTransaction> updateExcel(MultipartFile multipartFile) {

        List<BookTransaction> bookTransactionList = new ArrayList<>();

        Workbook workbook = null;

        String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (!(ext.equalsIgnoreCase("xlsx") || ext.equalsIgnoreCase("xls"))) {
            throw new CustomException("Invalid format for excel file");
        }
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }
        try {
            if (ext.equalsIgnoreCase("xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new HSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }


//        try {
//            workbook = new XSSFWorkbook(inputStream);
//        } catch (IOException e) {
//            throw new CustomException(e.getMessage());
//        }

        Sheet spreadSheet = workbook.getSheetAt(0);

        if (spreadSheet == null) {
            throw new CustomException("No sheet found");
        }

        Iterator<Row> rowIterator = spreadSheet.rowIterator();

        Integer rowNumber = 0;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (rowNumber == 0) {
                rowNumber++;
                continue;
            }

            Iterator<Cell> cellIterator = row.cellIterator();
            BookTransaction bookTransaction = new BookTransaction();
            Book book = new Book();
            Member member = new Member();

            Integer cellIndex = 0;
            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();
                switch (cellIndex) {
                    case 0:
                        bookTransaction.setId((long) cell.getNumericCellValue());
                        break;
                    case 1:
                        DataFormatter dataFormatter = new DataFormatter();
                        String formatedCellStr = dataFormatter.formatCellValue(cell);
                        bookTransaction.setCode(formatedCellStr);
                        break;
                    case 2:
                        bookTransaction.setFromDate(cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        break;
                    case 3:
                        bookTransaction.setRentStatus(RENT_TYPE.valueOf(cell.getStringCellValue()));
                        break;
                    case 4:
                        LocalDate localDate = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        if (bookTransaction.getFromDate().isBefore(localDate)) {
                            bookTransaction.setToDate(localDate);
                        } else {
                            throw new CustomException("The date is inconsistent");
                        }
                        break;
                    case 5:
                        book = bookRepo.findByName(cell.getStringCellValue());
                        if (book == null) {
                            throw new CustomException("Book " + cell.getStringCellValue() + " does not exists");
                        }
                        bookTransaction.setBook(book);
                        break;
                    case 6:
                        member = memberMapper.getMemberByName(cell.getStringCellValue());
                        if (member == null) {
                            throw new CustomException("Member " + cell.getStringCellValue() + " does not exists");
                        }
                        bookTransaction.setMember(member);
                        break;
                    default:
                        break;
                }
                cellIndex++;
            }
            bookTransactionList.add(bookTransaction);
        }
        return bookTransactionList;
    }

}
