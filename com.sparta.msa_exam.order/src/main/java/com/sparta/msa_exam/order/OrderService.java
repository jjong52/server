package com.sparta.msa_exam.order;

import com.sparta.msa_exam.order.client.ProductClient;
import com.sparta.msa_exam.order.client.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public ProductResponseDto getProductInfo(Long productId) {
        return productClient.getProduct(productId);
    }

//    1.  `FeignClient`를 이용해서 주문 서비스에 상품 서비스 클라이언트 연결
//    2. [상품 목록 조회 API]를 호출해서 파라미터로 받은 `product_id` 가 상품 목록에 존재하는지 검증
//    3. 존재할경우 주문에 상품을 추가하고, 존재하지 않는다면 주문에 저장하지 않음.

    public List<ProductResponseDto> getProductsByIds(List<Long> productIds) {
        List<ProductResponseDto> productList = new ArrayList<>();
        for (Long productId : productIds) {
            try {
                ProductResponseDto product = productClient.getProduct(productId);
                productList.add(product);
            } catch (Exception e) {
                logger.error("상품번호 {} 가 존재하지 않습니다.", productId, e);
                throw new IllegalArgumentException("상품번호" + productId + "가 존재하지 않습니다." );
            }
        }

        return productList;
    }

    public OrderResponseDto createOrder(OrderRequestDto requestDto) {

        List<ProductResponseDto> products = getProductsByIds(requestDto.getProductIds());
        Order order = new Order(requestDto.getProductIds());
        orderRepository.save(order);
        return new OrderResponseDto(order);
    }

    public OrderResponseDto getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new NullPointerException("주문을 찾을 수 없습니다.")
        );
        return new OrderResponseDto(order);
    }
//    @Transactional
//    public OrderResponseDto createOrder(OrderRequestDto requestDto, String userId) {
//        // Check if products exist and if they have enough quantity
//        for (Long productId : requestDto.getOrderItemIds()) {
//            ProductResponseDto product = productClient.getProduct(productId);
//            log.info("############################ Product 수량 확인 : " + product.getQuantity());
//            if (product.getQuantity() < 1) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product with ID " + productId + " is out of stock.");
//            }
//        }
//
//        // Reduce the quantity of each product by 1
//        for (Long productId : requestDto.getOrderItemIds()) {
//            productClient.reduceProductQuantity(productId, 1);
//        }
//
//
//        Order order = Order.createOrder(requestDto.getOrderItemIds(), userId);
//        Order savedOrder = orderRepository.save(order);
//        return toResponseDto(savedOrder);
//    }


}
