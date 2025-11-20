package com.qwer.qrorder.controller.menu;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qwer.qrorder.domain.Menu;
import com.qwer.qrorder.dto.MenuDTO;
import com.qwer.qrorder.service.MenuService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {
	
	private final MenuService menuService;
	
	@GetMapping("/list")
	public String menuList(HttpSession session, Model model) {

	    // DB에서 메뉴 리스트 가져오기
	    List<Menu> menuList = menuService.getAllMenus();
	    model.addAttribute("menuList", menuList);

	    // 세션 값 확인용
	    model.addAttribute("tableNo", session.getAttribute("tableNo"));
	    model.addAttribute("allergy", session.getAttribute("allergy"));

	    return "menu/list"; // templates/menu/list.html
	}
	
	@GetMapping("/list/data")
	@ResponseBody
	public List<MenuDTO> getMenuListAjax() {
	    return menuService.getMenuDTOList();
	}
	
	@GetMapping("/cart")
	public String cartPage() {
	    return "menu/cart"; // templates/menu/cart.html
	}
}