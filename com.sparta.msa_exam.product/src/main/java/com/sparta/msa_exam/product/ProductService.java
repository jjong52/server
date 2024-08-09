package com.sparta.msa_exam.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // Product -> ProductResponseDto 변환 메서드
    private ProductResponseDto convertToDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getSupply_price()
        );
    }

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = Product.builder()
                .name(requestDto.getName())
                .supply_price(requestDto.getSupply_price())
                .build();

        productRepository.save(product);

        return new ProductResponseDto(product.getId(), product.getName(), product.getSupply_price());
    }

    public List<ProductResponseDto> getProducts() {
        List<ProductResponseDto> responseDtoList = productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return responseDtoList;
    }

    public ProductResponseDto getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NullPointerException("상품이 존재하지 않습니다.")
        );
        return new ProductResponseDto(product.getId(), product.getName(), product.getSupply_price());
    }

    public List<ProductResponseDto> getProducts(List<Long> productIds) {
        List<ProductResponseDto> responseDtoList = productRepository.findAllById(productIds).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return responseDtoList;
    }

}
