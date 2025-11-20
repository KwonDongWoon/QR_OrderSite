package com.qwer.qrorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.qwer.qrorder.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}