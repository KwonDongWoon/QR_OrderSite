package com.qwer.qrorder.controller.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.qwer.qrorder.domain.Category;
import com.qwer.qrorder.domain.Menu;
import com.qwer.qrorder.service.MenuService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/menu")
public class AdminMenuController {

	private final MenuService menuService;
	
	/** 메뉴 리스트 페이지 */
	@GetMapping("/list")
	public String menuList(Model model) {
	    List<Menu> menuList = menuService.getAllMenus(); // DB에서 모든 메뉴 가져오기
	    model.addAttribute("menuList", menuList);
	    return "admin/menu/list"; // list.html
	}

	/** 메뉴 등록 폼 */
	@GetMapping("/insert")
	public String insertForm(Model model) {
		model.addAttribute("menu", new Menu());
		return "admin/menu/insertMenu";
	}

	/** 메뉴 등록 처리 */
	@PostMapping("/insert")
	public String insertMenu(@RequestParam("categoryNo") int categoryNo, Menu menu,
			@RequestParam("menuImageFile") MultipartFile file) throws IOException {

		// categoryNo → Category 객체로 변환
		Category category = new Category();
		category.setCategoryNo(categoryNo);
		menu.setCategory(category);

		// uploads 폴더 경로 지정
		String uploadDir = System.getProperty("user.dir") + "/uploads/";
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// 파일 업로드 처리 (Tomcat tmp 문제 방지)
		if (!file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			String storedFilename = UUID.randomUUID() + "_" + originalFilename;

			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir, storedFilename), StandardCopyOption.REPLACE_EXISTING);
			}

			menu.setMenuImage(storedFilename);
		}

		// 기본값 설정
		menu.setIsActive("Y");
		menuService.insertMenu(menu, file);

		return "redirect:/admin/menu/list";
	}
	
    // 수정 폼 이동 (기존 데이터 불러오기)
    @GetMapping("/update/{menuNo}")
    public String updateForm(@PathVariable("menuNo") Integer menuNo, Model model) {
        // menuNo 기준으로 메뉴 정보 조회
        Menu menu = menuService.getMenuById(menuNo);
        model.addAttribute("menu", menu); // JSP/HTML로 전달
        return "admin/menu/updateMenu"; // 수정 화면으로 이동
    }

    // 수정 처리 (POST)
    @PostMapping("/update/{menuNo}")
    public String updateMenu(@PathVariable("menuNo") Integer menuNo,
                             Menu menu, // form에서 넘어온 데이터 자동 매핑
                             @RequestParam(value = "menuImageFile", required = false) MultipartFile file)
                             throws IOException {

        // 수정할 메뉴 번호 지정
        menu.setMenuNo(menuNo);

        // 서비스 호출하여 수정 반영
        menuService.updateMenu(menu, file);

        // 수정 후 리스트로 리다이렉트
        return "redirect:/admin/menu/list";
    }
    
    // 메뉴 삭제 처리
    @GetMapping("/delete/{menuNo}")
    public String deleteMenu(@PathVariable("menuNo") Integer menuNo) {

        // 서비스 호출해서 삭제 실행
        menuService.deleteMenu(menuNo);

        // 삭제 후 리스트 페이지로 리다이렉트
        return "redirect:/admin/menu/list";
    }
}