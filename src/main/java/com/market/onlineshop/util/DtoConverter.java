package com.market.onlineshop.util;


import com.market.onlineshop.dto.OrderDTO;
import com.market.onlineshop.dto.UserDTO;
import com.market.onlineshop.models.User;
import com.market.onlineshop.models.Order;

import java.util.stream.Collectors;

public class DtoConverter {

    public static UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setImage(user.getImage());
        userDTO.setOrders(user.getOrders().stream().map(DtoConverter::convertToDto).collect(Collectors.toList()));
        return userDTO;
    }

    public static OrderDTO convertToDto(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setTimestamp(order.getTimestamp());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setTotalPrice(order.getTotalPrice());
        return orderDTO;
    }
}
