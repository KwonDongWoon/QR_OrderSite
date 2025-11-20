package com.qwer.qrorder.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TABLE_INFO")
@Getter @Setter
public class TableInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TABLE_NO")
    private Integer tableNo;

    @Column(name = "TABLE_NAME", nullable = false, length = 50)
    private String tableName;

    @Column(name = "QR_CODE_URL", nullable = false)
    private String qrCodeUrl;

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status = "EMPTY";
}