package com.example.springbackend.controllers;

import com.example.springbackend.model.Product;
import com.example.springbackend.repositories.ProductRepository;
import com.example.springbackend.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;
    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    private ResponseEntity<List<Product>> helper(List<Product>products,Page<Product>productPage){
        products = productPage.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false)String name, @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "5") int size) {
        try {
            List<Product> products = productService.getAllProducts();
            Pageable paging = PageRequest.of(page, size);
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else{
                Page<Product> productPage;
                if (name==null){
                    productPage = productRepository.findAll(paging);
                }
                else{
                    productPage = productRepository.searchProductsByNameIsContainingIgnoreCase(name,paging);
                    System.out.println(productPage.getTotalElements());
                }
               return helper(products,productPage);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/suppliers")
    public ResponseEntity<List<Product>> getSupplierProducts(@RequestParam(required = false)String name,@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "5") int size){
        try {
            List<Product> products = productService.getAllProducts();

            Pageable paging = PageRequest.of(page, size);
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else{
                Page<Product>productPage;
                if (name==null){
                    productPage = productRepository.findAll(paging);

                }
                else{
                    productPage = productRepository.findAllBySupplierContainingIgnoreCase(name,paging);
                }
                return helper(products,productPage);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/suppliers/unexpired")
    public ResponseEntity<List<Product>>getUnexpiredProducts(@RequestParam(required = false)String supplierName,@RequestParam(defaultValue = "false")Boolean flag,@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "5")int size){
        try{
            List<Product> products = productService.getAllProducts();
            Pageable paging = PageRequest.of(page, size);
            Page<Product>productPage;
            if (supplierName!=null && flag){
                productPage = productRepository.filterProductsUsingNameAndExpiryDate(supplierName,paging);
                return helper(products,productPage);
            }
            if(supplierName!=null && !flag){
                return getSupplierProducts(supplierName,0,3);
            }
            if(supplierName==null && flag){

                productPage =productRepository.findUnExpiredProducts(paging);
                System.out.println(productPage.getTotalElements());
                return helper(products,productPage);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

}
