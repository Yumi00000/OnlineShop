package com.market.onlineshop;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "users") // Use a non-reserved name for the table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @NotNull
    @Size(min = 6)
    private String password;

    private String firstName;
    private String lastName;

    @Email
    private String email;


    private String phone;
    private String image;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders; // Correct `mappedBy` to refer to the `user` field in Order

    public User() {
        this.isAdmin = false;
    }
}
