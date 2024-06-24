package com.example.groupproject.Services;

import com.example.groupproject.dto.ProductDto;
import com.example.groupproject.model.Product;

import java.util.List;


public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(int id);
}
