package com.sparta.msa_exam.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;

//    @OneToMany(mappedBy = "order")
//    private List<OrderItem> orderItems = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "order_product_ids", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "product_id")
    private List<Long> productIds;

    public Order(List<Long> productIds) {
        this.productIds = productIds;
    }
}
