package com.bookmanager.dto;

import lombok.Data;

@Data
public class BorrowRequestDTO {
    private Long bookId;
    private Integer borrowDays;
}