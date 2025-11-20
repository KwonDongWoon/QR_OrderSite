package com.qwer.qrorder.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MENU")
@Getter @Setter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_NO")
    private Integer menuNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_NO", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private Admin admin;

    @Column(name = "MENU_NAME", nullable = false, length = 60)
    private String menuName;

    @Column(name = "PRICE", nullable = false)
    private int price;

    @Column(name = "MENU_IMAGE", length = 255)
    private String menuImage;

    @Column(name = "IS_ACTIVE", length = 1)
    private String isActive = "Y";  // 기본 활성화
}