package com.bookmanager.service.impl;

import com.bookmanager.model.BorrowRecord;
import com.bookmanager.repository.BorrowRecordRepository;
import com.bookmanager.service.BookService;
import com.bookmanager.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowRecordServiceImpl implements BorrowRecordService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookService bookService;

    @Override
    @Transactional
    public BorrowRecord addBorrowRecord(BorrowRecord borrowRecord) {
        borrowRecord.setStatus("pending");
        borrowRecord.setCreatedAt(LocalDateTime.now());
        borrowRecord.setUpdatedAt(LocalDateTime.now());
        return borrowRecordRepository.save(borrowRecord);
    }

    @Override
    public Optional<BorrowRecord> findById(Long borrowRecordId) {
        return borrowRecordRepository.findById(borrowRecordId);
    }

    @Override
    public List<BorrowRecord> findAll() {
        return borrowRecordRepository.findAll();
    }

    @Override
    public List<BorrowRecord> findByUserId(Long userId) {
        return borrowRecordRepository.findByUserId(userId);
    }

    @Override
    public List<BorrowRecord> findByStatus(String status) {
        return borrowRecordRepository.findByStatus(status);
    }

    @Override
    public List<BorrowRecord> findByStatusAndUserId(String status, Long userId) {
        return borrowRecordRepository.findByStatusAndUserId(status, userId);
    }

    @Override
    @Transactional
    public BorrowRecord updateBorrowStatus(Long borrowRecordId, String status) {
        return updateBorrowStatus(borrowRecordId, status, null);
    }

    @Override
    @Transactional
    public BorrowRecord updateBorrowStatus(Long borrowRecordId, String status, String rejectReason) {
        Optional<BorrowRecord> optionalBorrowRecord = borrowRecordRepository.findById(borrowRecordId);
        if (optionalBorrowRecord.isPresent()) {
            BorrowRecord borrowRecord = optionalBorrowRecord.get();
            borrowRecord.setStatus(status);
            borrowRecord.setUpdatedAt(LocalDateTime.now());

            if ("approved".equals(status)) {
                // 审核通过，减少图书库存
                boolean stockDecreased = bookService.decreaseStock(borrowRecord.getBook().getId());
                if (!stockDecreased) {
                    throw new IllegalArgumentException("图书库存不足");
                }
            } else if ("rejected".equals(status)) {
                // 审核拒绝，保存拒绝原因
                borrowRecord.setRejectReason(rejectReason);
            }

            return borrowRecordRepository.save(borrowRecord);
        } else {
            throw new IllegalArgumentException("借阅记录不存在");
        }
    }

    @Override
    @Transactional
    public void returnBook(Long borrowRecordId) {
        Optional<BorrowRecord> optionalBorrowRecord = borrowRecordRepository.findById(borrowRecordId);
        if (optionalBorrowRecord.isPresent()) {
            BorrowRecord borrowRecord = optionalBorrowRecord.get();
            if (!"borrowed".equals(borrowRecord.getStatus())) {
                throw new IllegalArgumentException("图书未被借阅或已归还");
            }

            // 更新借阅记录状态和实际归还日期
            borrowRecord.setStatus("returned");
            borrowRecord.setActualReturnDate(LocalDateTime.now());
            borrowRecord.setUpdatedAt(LocalDateTime.now());

            // 增加图书库存
            bookService.increaseStock(borrowRecord.getBook().getId());

            borrowRecordRepository.save(borrowRecord);
        } else {
            throw new IllegalArgumentException("借阅记录不存在");
        }
    }

    @Override
    public List<BorrowRecord> findOverdueRecords() {
        List<BorrowRecord> allBorrowRecords = borrowRecordRepository.findByStatus("borrowed");
        LocalDateTime now = LocalDateTime.now();

        // 筛选出超过应还日期的记录
        return allBorrowRecords.stream()
                .filter(record -> record.getExpectedReturnDate().isBefore(now))
                .collect(Collectors.toList());
    }
}