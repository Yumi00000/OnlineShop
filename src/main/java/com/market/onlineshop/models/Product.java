package com.market.onlineshop.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Description cannot be empty")
    @Size(min = 15, max = 100, message = "Product Description cannot be shorter than 15 characters")
    private String description;
    @NotNull(message = "Product price is required.")
    @Positive(message = "Product price must be positive.")
    private Double price;

    private String image;


    @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;
}
