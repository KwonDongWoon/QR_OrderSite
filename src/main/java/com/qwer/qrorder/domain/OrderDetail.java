package com.qwer.qrorder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ORDER_DETAIL")
@Getter
@Setter
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DETAIL_NO")
    private Integer detailNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_NO", nullable = false)
    private OrderMain orderMain;  // ★ 연관관계 추가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_NO", nullable = false)
    private Menu menu;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @Column(name = "PRICE", nullable = false)
    private int price;

    @Column(name = "MEMO", length = 200)
    private String memo;
}
