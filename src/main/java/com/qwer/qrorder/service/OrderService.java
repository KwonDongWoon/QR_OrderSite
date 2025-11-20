package com.qwer.qrorder.service;

import com.qwer.qrorder.domain.OrderMain;
import com.qwer.qrorder.dto.OrderRequestDTO;

public interface OrderService {

    // 주문 생성
    OrderMain createOrder(OrderRequestDTO orderRequestDTO);

    // 주문 상세 조회 (필요시)
    OrderMain getOrderById(Integer orderNo);
}