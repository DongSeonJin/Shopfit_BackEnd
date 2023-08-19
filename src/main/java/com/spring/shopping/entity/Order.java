package com.spring.shopping.entity;

import java.util.List;

public class OrderDTO {
    private Long userId;
    private List<Long> productIds;
    private Long totalPrice;
    private String address;
    private String phoneNumber;

    // Getters, setters, constructors...
}