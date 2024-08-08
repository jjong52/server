package com.sparta.msa_exam.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@NoArgsConstructor
@AllArgsConstructor
public class Product {
    Long product_id;
    String name;
    Integer supply_price;
}
