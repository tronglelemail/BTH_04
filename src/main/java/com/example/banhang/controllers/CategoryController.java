package com.example.banhang.controllers;

import com.example.banhang.models.Category;
import com.example.banhang.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // Khai báo Controller trả giao diện HTML
@RequiredArgsConstructor
@RequestMapping("/categories") // Tiền tố đường dẫn chung cho toàn bộ controller này
public class CategoryController {

    private final CategoryService categoryService; // Sang lớp Service xử lý nghiệp vụ

    // 1. HIỂN THỊ DANH SÁCH DANH MỤC
    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories); // Đẩy danh sách categories sang file HTML
        return "categories/categories-list"; // Trả về file templates/categories/categories-list.html
    }

    // 2. HIỂN THỊ FORM THÊM MỚI
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category()); // Gửi một đối tượng rỗng để hứng dữ liệu từ Form
        return "categories/add-category"; // Trả về file templates/categories/add-category.html
    }

    // 3. LƯU FORM THÊM MỚI
    @PostMapping("/add")
    public String addCategory(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) { // Nếu dữ liệu nhập vào bị lỗi validation (trống, sai định dạng...)
            return "categories/add-category"; // Giữ nguyên trang để người dùng sửa lỗi
        }
        categoryService.addCategory(category); // Gọi service lưu vào DB
        return "redirect:/categories"; // Lưu xong thì quay lại trang danh sách
    }

    // 4. HIỂN THỊ FORM CẬP NHẬT (SỬA)
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID danh mục không hợp lệ: " + id));
        model.addAttribute("category", category); // Nạp dữ liệu cũ lên Form để sửa
        return "categories/update-category"; // Trả về file templates/categories/update-category.html
    }

    // 5. LƯU FORM CẬP NHẬT
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, @Valid Category category, BindingResult result) {
        category.setId(id); // Đảm bảo ID lấy từ đường dẫn
        if (result.hasErrors()) {
            return "categories/update-category"; // Có lỗi thì giữ lại trang
        }
        categoryService.updateCategory(category); // Thực hiện cập nhật
        return "redirect:/categories"; // Cập nhật xong quay lại danh sách
    }

    // 6. XÓA DANH MỤC
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id); // Thực hiện xóa
        return "redirect:/categories"; // Xóa xong quay lại danh sách
    }
}
