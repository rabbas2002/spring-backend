package com.example.springbackend.services;

import com.example.springbackend.dataloader.DataLoader;
import com.example.springbackend.model.Product;
import com.example.springbackend.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private ProductRepository productRepository;
    public void save(MultipartFile file) {
        Logger logger = LoggerFactory.getLogger(ExcelService.class);
        try {
            List<Product> products = DataLoader.excelToProducts(file.getInputStream());
            productRepository.saveAll(products);
            logger.info("Persisted Data Successfully to the MySQL Database!");

        } catch (IOException e) {
            String message = "fail to store excel data: " + e.getMessage();
            logger.error(message);
            throw new RuntimeException(message);
        }
    }
}
