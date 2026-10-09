package com.example.banhang.services;

import com.example.banhang.models.Product;
import com.example.banhang.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // 1. Lấy toàn bộ danh sách sản phẩm
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 2. Tìm sản phẩm theo ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // 3. Thêm mới sản phẩm
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // 4. Cập nhật thông tin sản phẩm
    public Product updateProduct(@NotNull Product product) {
        Product existingProduct = productRepository.findById(product.getId())
            .orElseThrow(() -> new IllegalStateException("Sản phẩm ID " + product.getId() + " không tồn tại."));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());

        return productRepository.save(existingProduct);
    }

    // 5. Xóa sản phẩm theo ID
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Sản phẩm ID " + id + " không tồn tại.");
        }
        productRepository.deleteById(id);
    }
}
