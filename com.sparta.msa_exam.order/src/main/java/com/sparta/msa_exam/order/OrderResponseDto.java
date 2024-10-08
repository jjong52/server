package com.sparta.msa_exam.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto implements Serializable {
    private Long order_id;
    private List<Long> productIds;

    public OrderResponseDto(Order order) {
        this.order_id = order.getOrder_id();
        this.productIds = order.getProductIds();
    }
}
