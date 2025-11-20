package com.qwer.qrorder.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.qwer.qrorder.domain.Category;
import com.qwer.qrorder.domain.Menu;
import com.qwer.qrorder.dto.MenuDTO;
import com.qwer.qrorder.repository.CategoryRepository;
import com.qwer.qrorder.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    
    /** 관리자 화면 (Thymeleaf) */
    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    /** 사용자 화면 (AJAX JSON) */
    @Override
    public List<MenuDTO> getMenuDTOList() {
        return menuRepository.findAll().stream()
                .map(menu -> {
                    MenuDTO dto = new MenuDTO();
                    dto.setMenuNo(menu.getMenuNo());
                    dto.setMenuName(menu.getMenuName());
                    dto.setPrice(menu.getPrice());
                    dto.setCategoryName(menu.getCategory().getCategoryName());
                    dto.setImageUrl("/uploads/" + menu.getMenuImage());
                    return dto;
                })
                .toList();
    }

    // Menu 등록
    @Override
    @Transactional
    public void insertMenu(Menu menu, MultipartFile file) throws IOException {

        if (!file.isEmpty()) {

            // 1. 저장 경로를 프로젝트 루트 /uploads 로 통일
            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            // 2. 폴더가 없으면 자동 생성 (Mac 권한 이슈 방지)
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 3. 파일명 랜덤화 후 저장
            String originalFilename = file.getOriginalFilename();
            String storedFilename = UUID.randomUUID() + "_" + originalFilename;

            File dest = new File(dir, storedFilename);
            file.transferTo(dest);

            // 4. DB에는 파일명만 저장
            menu.setMenuImage(storedFilename);
        }

        // 5. 기본값 세팅 후 저장
        menu.setIsActive("Y");
        menuRepository.save(menu);
    }
    
    // Menu 조회
    @Override
    public Menu getMenuById(Integer menuNo) {
        return menuRepository.findById(menuNo)
            .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다. menuNo=" + menuNo));
    }

    // Menu 수정 로직
    @Override
    @Transactional
    public void updateMenu(Menu menu, MultipartFile file) throws IOException {
        Menu existingMenu = menuRepository.findById(menu.getMenuNo())
                .orElseThrow(() -> new IllegalArgumentException("메뉴 없음: " + menu.getMenuNo()));

        existingMenu.setMenuName(menu.getMenuName());
        existingMenu.setPrice(menu.getPrice());
        existingMenu.setIsActive(menu.getIsActive());

        // ✅ categoryNo만 변경을 시도하지 말고, Category 엔티티 전체를 교체
        Integer newCategoryNo = menu.getCategory().getCategoryNo();
        if (newCategoryNo != null) {
            Category newCategory = categoryRepository.findById(newCategoryNo)
                    .orElseThrow(() -> new IllegalArgumentException("카테고리 없음: " + newCategoryNo));
            existingMenu.setCategory(newCategory);
        }

        // 이미지 수정 처리
        if (!file.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            // 기존 파일 삭제
            if (existingMenu.getMenuImage() != null) {
                File oldFile = new File(uploadDir + existingMenu.getMenuImage());
                if (oldFile.exists()) oldFile.delete();
            }

            // 새 파일 저장
            String originalFilename = file.getOriginalFilename();
            String storedFilename = UUID.randomUUID() + "_" + originalFilename;
            file.transferTo(new File(uploadDir + storedFilename));

            existingMenu.setMenuImage(storedFilename);
        }

        menuRepository.save(existingMenu); // 저장
    }
    
    @Override
    @Transactional
    public void deleteMenu(Integer menuNo) {
        Menu menu = menuRepository.findById(menuNo)
                .orElseThrow(() -> new IllegalArgumentException("메뉴 없음: " + menuNo));

        // 이미지 파일 삭제
        if (menu.getMenuImage() != null) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File imgFile = new File(uploadDir + menu.getMenuImage());
            if (imgFile.exists()) {
                imgFile.delete();
            }
        }
        menuRepository.delete(menu);
    }
}