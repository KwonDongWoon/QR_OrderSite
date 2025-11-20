package com.qwer.qrorder.controller.admin;

import com.qwer.qrorder.domain.Admin;
import com.qwer.qrorder.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "admin/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String userId,
                        @RequestParam String userPwd,
                        HttpSession session,
                        Model model) {

    	Admin admin = adminService.login(userId, userPwd);
   
    	if (admin != null) {
            session.setAttribute("admin", admin); // 세션에 관리자 정보 저장
            return "redirect:/admin/menu/list";   // 로그인 성공 시 관리자 메뉴 리스트로 이동
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "admin/login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
    
}
