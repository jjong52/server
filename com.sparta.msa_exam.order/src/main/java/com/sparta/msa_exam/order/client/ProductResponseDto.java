package com.sparta.msa_exam.order.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private Integer supply_price;
}
