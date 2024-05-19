package com.example.groupproject.Services;

import com.example.groupproject.dto.CartDto;
import com.example.groupproject.dto.ProductDto;
import com.example.groupproject.model.Cart;
import com.example.groupproject.model.User;

import java.util.List;

public interface CartService {
    List<CartDto> getAllCarts();
    CartDto getCartById(int id);
    CartDto saveCart(CartDto cartDto);
    int getCurrentCartId();
    CartDto updateCartQuantity(int cartId, int newQuantity);

}
