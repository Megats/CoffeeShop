package com.example.groupproject.Services;

import com.example.groupproject.dto.CartDto;
import com.example.groupproject.dto.OrderDto;
import com.example.groupproject.model.Cart;
import com.example.groupproject.model.Order;
import com.example.groupproject.model.User;

import java.util.List;

public interface OrderServices {
    void saveOrder(Order order);
    Order createOrder(int userId, int cartId, List<CartDto> carts);

    List<OrderDto> getAllOrders();
}
