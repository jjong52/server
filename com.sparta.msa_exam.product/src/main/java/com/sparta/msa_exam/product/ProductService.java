package com.sparta.msa_exam.product;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    // 상품 추가 API 를 호출 할 경우 상품 목록 조회 API 의 응답 데이터 캐시가 갱신되도록 구현해주세요.
    @CachePut(cacheNames = "productCache", key = "#result.id")
    @CacheEvict(cacheNames = "productAllCache", allEntries = true)
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = Product.builder()
                .name(requestDto.getName())
                .supply_price(requestDto.getSupply_price())
                .build();

        productRepository.save(product);

        return new ProductResponseDto(product.getId(), product.getName(), product.getSupply_price());
    }

    @Cacheable(cacheNames = "productAllCache", key = "getMethodName()")
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
