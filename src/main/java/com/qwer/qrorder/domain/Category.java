package com.qwer.qrorder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CATEGORY")
@Getter @Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_NO")
    private Integer categoryNo;

    @Column(name = "CATEGORY_NAME", nullable = false, length = 20)
    private String categoryName;
}