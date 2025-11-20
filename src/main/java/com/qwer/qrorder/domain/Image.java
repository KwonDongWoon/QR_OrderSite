package com.qwer.qrorder.domain;

import java.time.LocalDateTime;

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
@Table(name = "IMAGE")
@Getter @Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_NO")
    private Integer imageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_NO", nullable = false)
    private Menu menu;

    @Column(name = "ORIGIN_NAME", nullable = false)
    private String originName;

    @Column(name = "STORED_NAME", nullable = false)
    private String storedName;

    @Column(name = "FILE_PATH", nullable = false)
    private String filePath;

    @Column(name = "FILE_SIZE", nullable = false)
    private int fileSize;

    @Column(name = "REG_DATE")
    private LocalDateTime regDate = LocalDateTime.now();
}