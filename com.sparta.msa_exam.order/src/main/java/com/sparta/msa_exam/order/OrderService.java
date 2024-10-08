package com.sparta.msa_exam.order;

import com.sparta.msa_exam.order.client.ProductClient;
import com.sparta.msa_exam.order.client.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public ProductResponseDto getProductInfo(Long productId) {
        return productClient.getProduct(productId);
    }

    // Product -> ProductResponseDto 변환 메서드
    private OrderResponseDto convertToDto(Order order) {
        return new OrderResponseDto(
                order.getOrder_id(),
                order.getProductIds()
        );
    }

//    1.  `FeignClient`를 이용해서 주문 서비스에 상품 서비스 클라이언트 연결
//    2. [상품 목록 조회 API]를 호출해서 파라미터로 받은 `product_id` 가 상품 목록에 존재하는지 검증
//    3. 존재할경우 주문에 상품을 추가하고, 존재하지 않는다면 주문에 저장하지 않음.

    public List<ProductResponseDto> getProductsByIds(List<Long> productIds) {
        List<ProductResponseDto> productList = productClient.getProducts(productIds);
//        for (Long productId : productIds) {
//            try {
//                ProductResponseDto product = productClient.getProduct(productId);
//                productList.add(product);
//            } catch (Exception e) {
//                logger.error("상품번호 {} 가 존재하지 않습니다.", productId, e);
//                throw new IllegalArgumentException("상품번호" + productId + "가 존재하지 않습니다." );
//            }
//        }

        return productList;
    }

    public OrderResponseDto createOrder(OrderRequestDto requestDto) {

        List<Long> productIds = requestDto.getProductIds();
        List<ProductResponseDto> products = getProductsByIds(productIds);

        // 존재하는 상품의 ID만 추출
        List<Long> validIds = products.stream()
                .map(ProductResponseDto::getId)
                .collect(Collectors.toList());

        Order order = new Order(validIds);
        orderRepository.save(order);
        return new OrderResponseDto(order);
    }

    // 이 메서드의 결과는 캐싱이 가능하다
    // cacheNames : 이 메서드로 인해서 만들어질 캐시를 지칭하는 이름
    // key : 캐시 데이터를 구분하기 위해 활용하는 값
    @Cacheable(cacheNames = "orderCache", key = "args[0]")
    public OrderResponseDto getOrder(Long id) {
        logger.info("Get order : {}", id);
        return orderRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public OrderResponseDto addProductToOrder(Long orderId, AddProductRequest requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NullPointerException("주문을 찾을 수 없습니다.")
        );

        ProductResponseDto product = productClient.getProduct(requestDto.getProductId());

        // 주문에 이미 포함되어 있는 상품인지 확인
        if(order.getProductIds().contains(product.getId())) {
            throw new IllegalArgumentException("상품이 이미 주문에 추가되어 있습니다.");
        } else {
            order.getProductIds().add(product.getId());
            Order updatedOrder = orderRepository.save(order);
            return new OrderResponseDto(updatedOrder);
        }

    }
}
