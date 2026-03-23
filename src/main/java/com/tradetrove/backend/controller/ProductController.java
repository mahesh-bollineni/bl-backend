package com.tradetrove.backend.controller;

import com.tradetrove.backend.model.Product;
import com.tradetrove.backend.model.User;
import com.tradetrove.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(
            @RequestParam("title") String title,
            @RequestParam("price") Double price,
            @RequestParam("category") String category,
            @RequestParam("sellerId") Long sellerId,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        
        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);
        product.setCategory(category);
        
        User seller = new User();
        seller.setId(sellerId);
        product.setSeller(seller);
        
        product.setImageData(imageFile.getBytes());
        product.setImageType(imageFile.getContentType());
        
        return ResponseEntity.ok(productRepository.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }
}