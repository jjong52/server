package com.sparta.msa_exam.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Value("${server.port}")
    private String serverPort;

    @PostMapping("/product")
    public String createProduct() {
        return "a";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") String id) {
        return "Product " + id + " From port : " +serverPort;
    }

}
