package com.market.onlineshop;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "orders") // Ensure this name doesn't conflict with SQL keywords
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    private String orderStatus;
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Remove `table` attribute
    private User user; // Correct mapping
}
