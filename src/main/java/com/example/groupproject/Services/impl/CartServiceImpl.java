//package com.example.groupproject.Services.impl;
//
//import com.example.groupproject.Services.CartService;
//import com.example.groupproject.dto.CartDto;
//import com.example.groupproject.model.Cart;
//import com.example.groupproject.repository.CartRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.stream.Collectors;
//
//@Transactional
//@Service
//public class CartServiceImpl implements CartService {
//    private final CartRepository cartRepository;
//
//    public CartServiceImpl(CartRepository cartRepository) {
//        this.cartRepository = cartRepository;
//    }
//
//    @Override
//    public List<CartDto> getAllCarts() {
//        List<Cart> carts = cartRepository.findAll();
//        return carts.stream()
//                .map(this::mapToCartDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public CartDto getCartById(int id) {
//        Cart cart = cartRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));
//        return mapToCartDto(cart);
//    }
//
//    @Override
//    public CartDto saveCart(CartDto cartDto) {
//        Cart cart = mapToCart(cartDto);
//        Cart savedCart = cartRepository.save(cart);
//        return mapToCartDto(savedCart);
//    }
//    @Override
//    public CartDto updateCartQuantity(int cartId, int newQuantity) {
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new NoSuchElementException("Cart not found with id: " + cartId));
//
//        cart.setCart_quantity(newQuantity);
//        Cart updatedCart = cartRepository.save(cart);
//
//        return mapToCartDto(updatedCart);
//    }
//
//    private CartDto mapToCartDto(Cart cart) {
//        CartDto cartDto = new CartDto();
//        cartDto.setCart_id(cart.getCart_id());
//        cartDto.setUser_id(cart.getUser_id());
//        cartDto.setProduct_id(cart.getProduct_id());
//        cartDto.setCart_quantity(cart.getCart_quantity());
//        cartDto.setCart_total(cart.getCart_total());
//        // Set other properties as needed
//        return cartDto;
//    }
//
//    private Cart mapToCart(CartDto cartDto) {
//        Cart cart = new Cart();
//        cart.setCart_id(cartDto.getCart_id());
//        cart.setUser_id(cartDto.getUser_id());
//        cart.setProduct_id(cartDto.getProduct_id());
//        cart.setCart_quantity(cartDto.getCart_quantity());
//        cart.setCart_total(cartDto.getCart_total());
//        // Set other properties as needed
//        return cart;
//    }
//
//    @Override
//    public int getCurrentCartId() {
//        // Implement the logic to retrieve the current cart ID
//        // This could involve retrieving the cart ID from the session or a database
//        // For example:
//        int currentCartId = 1; // Replace with the actual logic to get the current cart ID
//        return currentCartId;
//    }
//
//}
//
