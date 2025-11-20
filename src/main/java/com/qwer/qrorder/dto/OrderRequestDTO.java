package com.qwer.qrorder.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDTO {

    private Integer tableNo;       // 테이블 번호
    private Integer peopleCount;   // 인원 수
    private List<OrderItemDTO> orderItems; // 메뉴 목록
}