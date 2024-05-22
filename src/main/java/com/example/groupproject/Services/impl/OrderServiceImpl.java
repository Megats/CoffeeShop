//package com.example.groupproject.Services.impl;
//
//import com.example.groupproject.Services.CartService;
//import com.example.groupproject.Services.OrderServices;
//import com.example.groupproject.Services.ProductService;
//import com.example.groupproject.dto.CartDto;
//import com.example.groupproject.dto.OrderDto;
//import com.example.groupproject.dto.ProductDto;
//import com.example.groupproject.model.Cart;
//import com.example.groupproject.model.Order;
//import com.example.groupproject.model.Product;
//import com.example.groupproject.model.User;
//import com.example.groupproject.repository.OrderRepository;
//import com.example.groupproject.repository.ProductRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class OrderServiceImpl implements OrderServices {
//    private final OrderRepository orderRepository;
//    private final ProductService productService;
//
//    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService) {
//        this.orderRepository = orderRepository;
//        this.productService = productService;
//    }
//
//    @Override
//    public Order createOrder(int userId, int cartId, List<CartDto> carts) {
//        Order order = new Order();
//        order.setUser_id(userId);
//        order.setCart_id(cartId);
//        order.setOrder_quantity(calculateTotalQuantity(carts));
//        order.setOrder_total(calculateOrderTotal(carts));
//        order.setOrder_status("Pending");
//        return order;
//    }
//
//    @Override
//    public void saveOrder(Order order) {
//        orderRepository.save(order);
//    }
//
//    private double calculateOrderTotal(List<CartDto> carts) {
//        double total = 0.0;
//        for (CartDto cart : carts) {
//            ProductDto product = productService.getProductById(cart.getProduct_id());
//            total += product.getProduct_price() * cart.getCart_quantity();
//        }
//        return total;
//    }
//
//    private int calculateTotalQuantity(List<CartDto> carts) {
//        int totalQuantity = 0;
//        for (CartDto cart : carts) {
//            totalQuantity += cart.getCart_quantity();
//        }
//        return totalQuantity;
//    }
//
//    @Override
//    public List<OrderDto> getAllOrders() {
//        List<Order> orders = orderRepository.findAll();
//        return orders.stream()
//                .map(this::mapToOrderDto)
//                .collect(Collectors.toList());
//    }
//    private OrderDto mapToOrderDto(Order order) {
//        OrderDto orderDto = new OrderDto();
//        orderDto.setOrder_id(order.getOrder_id());
//        orderDto.setUser_id(order.getUser_id());
//        orderDto.setCart_id(order.getCart_id());
//        orderDto.setOrder_quantity(order.getOrder_quantity());
//        orderDto.setOrder_total(order.getOrder_total());
//        orderDto.setOrder_status(order.getOrder_status());
//        // Set other properties as needed
//        return orderDto;
//    }
//
//}
//
//
