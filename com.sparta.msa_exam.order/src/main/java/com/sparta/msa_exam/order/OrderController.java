package com.sparta.msa_exam.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto requestDto) {
        return orderService.createOrder(requestDto);
    }

    @GetMapping("/order/{id}")
    public OrderResponseDto getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @PutMapping("/order/{orderId}")
    public OrderResponseDto addProductToOrder(@PathVariable Long orderId, @RequestBody AddProductRequest requestDto) {
        return orderService.addProductToOrder(orderId, requestDto);
    }
}
