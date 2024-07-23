package com.market.onlineshop;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date orderDate;
    private String orderStatus;
    private Double totalPrice;

    @ManyToOne
    private User user;
}
