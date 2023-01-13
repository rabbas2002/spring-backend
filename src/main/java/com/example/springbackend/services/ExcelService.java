package com.example.springbackend.services;

import com.example.springbackend.helper.ExcelHelper;
import com.example.springbackend.model.Product;
import com.example.springbackend.repositories.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    private final ProductRepository productRepository;

    public ExcelService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public void save(MultipartFile file) {
        try {
            List<Product> tutorials = ExcelHelper.excelToProducts(file.getInputStream());
            productRepository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public List<Product>getProductsByName(String name,Pageable pageable ){
        List<Product>products = new ArrayList<>();
        if (name!=null){
            products = productRepository.searchProductsByNameIsContainingIgnoreCase(name,pageable).getContent();
        }
        else{
            products = productRepository.findAll();
        }
        return  products;

    }


}
