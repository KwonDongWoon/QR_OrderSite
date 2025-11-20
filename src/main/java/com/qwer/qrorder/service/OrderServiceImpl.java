package com.qwer.qrorder.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qwer.qrorder.domain.Menu;
import com.qwer.qrorder.domain.OrderDetail;
import com.qwer.qrorder.domain.OrderMain;
import com.qwer.qrorder.domain.TableInfo;
import com.qwer.qrorder.dto.OrderItemDTO;
import com.qwer.qrorder.dto.OrderRequestDTO;
import com.qwer.qrorder.repository.MenuRepository;
import com.qwer.qrorder.repository.OrderDetailRepository;
import com.qwer.qrorder.repository.OrderMainRepository;
import com.qwer.qrorder.repository.TableInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMainRepository orderMainRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final MenuRepository menuRepository;
    private final TableInfoRepository tableInfoRepository; // 테이블 조회용

    @Override
    @Transactional
    public OrderMain createOrder(OrderRequestDTO orderRequestDTO) {

        // 1. ORDER_MAIN 생성
        OrderMain orderMain = new OrderMain();

        // 테이블 번호(Integer)를 TableInfo 엔티티로 조회 후 설정
        TableInfo table = tableInfoRepository.findById(orderRequestDTO.getTableNo())
                .orElseThrow(() -> new IllegalArgumentException("테이블 번호 오류: " + orderRequestDTO.getTableNo()));
        orderMain.setTable(table);

        orderMain.setPeopleCount(orderRequestDTO.getPeopleCount());
        orderMain.setOrderStatus("WAIT");

        int totalPrice = 0; // 총 금액 계산용

        // 2. ORDER_DETAIL 생성 (일반 for문 사용)
        for (OrderItemDTO item : orderRequestDTO.getOrderItems()) {

            Menu menu = menuRepository.findById(item.getMenuNo())
                    .orElseThrow(() -> new IllegalArgumentException("메뉴 ID 오류: " + item.getMenuNo()));

            OrderDetail detail = new OrderDetail();
            detail.setMenu(menu);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(menu.getPrice() * item.getQuantity());
            detail.setMemo(item.getMemo());

            // 양방향 관계 처리: OrderMain <-> OrderDetail
            orderMain.addDetail(detail);

            // 총 금액 누적
            totalPrice += detail.getPrice();
        }

        // 총 금액 설정
        orderMain.setTotalPrice(totalPrice);

        // CascadeType.ALL로 인해 OrderDetail도 자동 저장됨
        return orderMainRepository.save(orderMain);
    }

    @Override
    public OrderMain getOrderById(Integer orderNo) {
        return orderMainRepository.findById(orderNo)
                .orElseThrow(() -> new IllegalArgumentException("주문번호 없음: " + orderNo));
    }
}