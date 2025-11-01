package com.bookmanager.repository;

import com.bookmanager.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUserId(Long userId);
    List<BorrowRecord> findByStatus(String status);
    List<BorrowRecord> findByStatusAndUserId(String status, Long userId);
}
