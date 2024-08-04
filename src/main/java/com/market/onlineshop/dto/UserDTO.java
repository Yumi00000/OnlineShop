package com.market.onlineshop.dto;


import lombok.Data;

import java.util.List;


@Data
public class UserDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String image;
    private List<OrderDTO> orders;


}
