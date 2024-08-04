package com.market.onlineshop.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {
    private Long id;
    private Date timestamp;
    private String orderStatus;
    private Double totalPrice;


}