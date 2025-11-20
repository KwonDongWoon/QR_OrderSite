package com.qwer.qrorder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDER_MAIN")
@Getter
@Setter
public class OrderMain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_NO")
    private Integer orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABLE_NO", nullable = false)
    private TableInfo table; // 외래키 테이블 정보

    @Column(name = "PEOPLE_COUNT")
    private int peopleCount;

    @Column(name = "TOTAL_PRICE")
    private int totalPrice;

    @Column(name = "ORDER_STATUS", length = 20)
    private String orderStatus = "WAIT";

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "COMPLETED_DATE")
    private LocalDateTime completedDate;

    // ✅ OrderDetail과 1:N 연관관계
    @OneToMany(mappedBy = "orderMain", cascade = CascadeType.PERSIST)
    private List<OrderDetail> details = new ArrayList<>();

    // ✅ 편의 메서드 (양방향 관계 설정)
    public void addDetail(OrderDetail detail) {
        this.details.add(detail);
        detail.setOrderMain(this); // detail에도 OrderMain 설정
    }
}
