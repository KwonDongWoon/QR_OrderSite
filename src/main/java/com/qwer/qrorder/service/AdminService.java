package com.qwer.qrorder.service;

import com.qwer.qrorder.domain.Admin;

public interface AdminService {
    Admin login(String userId, String userPwd);
}