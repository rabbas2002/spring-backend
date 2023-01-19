package com.example.springbackend.controllers;

import com.example.springbackend.model.Product;
import com.example.springbackend.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/products/{supplierName}")
    public ResponseEntity<List<Product>>getProductsBySupplierName(
            @RequestParam(required = false,defaultValue = "0")Integer pageNo,
            @RequestParam(required = false,defaultValue = "5")Integer pageSize,
            @PathVariable String supplierName,
            @RequestParam(required = false)String productName,
            @RequestParam(required = false,defaultValue = "false")String isUnexpired){
            return productService.getProductsBySupplier(productName,supplierName,isUnexpired,pageNo,pageSize);
    }

}
