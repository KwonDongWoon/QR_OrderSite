package com.qwer.qrorder.service;
import org.springframework.stereotype.Service;

import com.qwer.qrorder.domain.Admin;
import com.qwer.qrorder.repository.AdminRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public Admin login(String userId, String userPwd) {
        return adminRepository.findByUserId(userId)
                .filter(a -> a.getUserPwd().equals(userPwd))
                .orElse(null);
    }
}