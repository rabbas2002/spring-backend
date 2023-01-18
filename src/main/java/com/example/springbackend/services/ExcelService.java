package com.example.springbackend.services;

import com.example.springbackend.helper.ExcelHelper;
import com.example.springbackend.model.Product;
import com.example.springbackend.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    private final ProductRepository productRepository;

    public ExcelService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public void save(MultipartFile file) {
        try {
            List<Product> products = ExcelHelper.excelToProducts(file.getInputStream());
            productRepository.saveAll(products);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

}
