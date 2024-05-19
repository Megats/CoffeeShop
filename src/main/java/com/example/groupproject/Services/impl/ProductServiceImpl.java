package com.example.groupproject.Services.impl;

import com.example.groupproject.Services.ProductService;
import com.example.groupproject.dto.ProductDto;
import com.example.groupproject.model.Product;
import com.example.groupproject.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToProductDto(product);
    }

    private ProductDto mapToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setProduct_id(product.getProduct_id());
        productDto.setProduct_name(product.getProduct_name());
        productDto.setProduct_price(product.getProduct_price());
        productDto.setProduct_image(product.getProduct_image());
        return productDto;
    }
}
