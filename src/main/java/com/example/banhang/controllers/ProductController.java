package com.example.banhang.controllers;

import com.example.banhang.models.Product;
import com.example.banhang.services.CategoryService;
import com.example.banhang.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService; // Dùng để nạp danh sách danh mục khi thêm/sửa sản phẩm

    // 1. HIỂN THỊ DANH SÁCH SẢN PHẨM
    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/products-list";
    }

    // 2. HIỂN THỊ FORM THÊM MỚI
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories()); // Đổ dữ liệu danh mục ra dropdown select
        return "products/add-product";
    }

    // 3. LƯU SẢN PHẨM MỚI
    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories()); // Nạp lại danh mục nếu form lỗi
            return "products/add-product";
        }
        productService.addProduct(product);
        return "redirect:/products";
    }

    // 4. HIỂN THỊ FORM CHỈNH SỬA
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID sản phẩm không hợp lệ: " + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories()); // Nạp danh mục để người dùng thay đổi
        return "products/update-product";
    }

    // 5. LƯU CẬP NHẬT SẢN PHẨM
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid Product product, BindingResult result, Model model) {
        product.setId(id); // Đảm bảo ID lấy từ đường dẫn
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "products/update-product";
        }
        productService.updateProduct(product);
        return "redirect:/products";
    }

    // 6. XÓA SẢN PHẨM
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }
}
