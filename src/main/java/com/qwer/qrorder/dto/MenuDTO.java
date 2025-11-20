package com.qwer.qrorder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MenuDTO {
    private Integer menuNo;
    private String menuName;
    private int price;
    private String imageUrl;
    private String categoryName;
}