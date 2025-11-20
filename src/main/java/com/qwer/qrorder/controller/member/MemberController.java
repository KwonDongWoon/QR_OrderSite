package com.qwer.qrorder.controller.member;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	// 처음 QR 찍었을 때 화면 보여주는 용도 (GET)
    @GetMapping("/countMember")
    public String countMember(@RequestParam int tableNo, Model model) {
        model.addAttribute("tableNo", tableNo);
        return "member/countMember"; // templates/member/count.html
    }

    // 인원수 & 알레르기 선택 후, 세션에 저장하고 다음 페이지로 이동 (POST)
    @PostMapping("/count")
    public String saveMemberInfo(@RequestParam int member,
                                 @RequestParam(required = false) List<String> allergy,
                                 @RequestParam int tableNo,
                                 HttpSession session) {
        session.setAttribute("member", member);
        session.setAttribute("allergy", allergy);
        session.setAttribute("tableNo", tableNo);

        return "redirect:/menu/list";
    }
}