package com.sparta.msa_exam.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

    @Value("${server.port}")
    private String serverPort;

    private final ProductService productService;

    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto) {
        return productService.createProduct(requestDto);
    }

    @GetMapping("/products")
    public List<ProductResponseDto> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable("id") String id) {
        return "Product " + id + " From port : " +serverPort;
    }

}
