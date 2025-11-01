package com.bookmanager.dto;

import lombok.Data;

@Data
public class BorrowApproveDTO {
    private Long borrowRecordId;
    private Boolean approved;
    private String rejectReason;
}