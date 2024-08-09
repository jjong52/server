//package com.sparta.msa_exam.order;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Builder
//@Entity
//public class OrderItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;
//
//    private Long product_id;
//}
