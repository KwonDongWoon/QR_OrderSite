package com.qwer.qrorder.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.qwer.qrorder.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUserId(String userId);
}
