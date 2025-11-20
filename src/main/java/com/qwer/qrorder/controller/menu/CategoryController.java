package com.qwer.qrorder.controller.menu;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.qwer.qrorder.domain.Category;
import com.qwer.qrorder.service.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public List<Category> list() {
        return categoryService.getAllCategories();
    }
}