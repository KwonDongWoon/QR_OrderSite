package com.qwer.qrorder.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Integer menuNo;
    private Integer quantity;
    private String memo;
}