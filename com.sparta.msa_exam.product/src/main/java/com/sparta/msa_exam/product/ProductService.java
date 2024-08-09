package com.sparta.msa_exam.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = Product.builder()
                .name(requestDto.getName())
                .supply_price(requestDto.getSupply_price())
                .build();

        productRepository.save(product);

        return new ProductResponseDto(product);
    }

    public List<ProductResponseDto> getProducts() {
        List<ProductResponseDto> responseDtoList = productRepository.findAll().stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
        return responseDtoList;
    }

    public ProductResponseDto getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NullPointerException("상품이 존재하지 않습니다.")
        );
        return new ProductResponseDto(product);
    }

}
