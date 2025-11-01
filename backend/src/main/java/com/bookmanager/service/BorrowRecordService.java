package com.bookmanager.service;

import com.bookmanager.model.BorrowRecord;
import java.util.List;
import java.util.Optional;

public interface BorrowRecordService {
    BorrowRecord addBorrowRecord(BorrowRecord borrowRecord);
    Optional<BorrowRecord> findById(Long borrowRecordId);
    List<BorrowRecord> findAll();
    List<BorrowRecord> findByUserId(Long userId);
    List<BorrowRecord> findByStatus(String status);
    List<BorrowRecord> findByStatusAndUserId(String status, Long userId);
    BorrowRecord updateBorrowStatus(Long borrowRecordId, String status);
    BorrowRecord updateBorrowStatus(Long borrowRecordId, String status, String rejectReason);
    void returnBook(Long borrowRecordId);
    List<BorrowRecord> findOverdueRecords();
}