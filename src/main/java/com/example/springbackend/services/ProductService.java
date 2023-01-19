package com.example.springbackend.services;

import com.example.springbackend.model.Product;
import com.example.springbackend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public ResponseEntity<List<Product>>getProductsBySupplier(String productName,String supplierName,String isUnexpired,Integer pageNo,Integer size){
        Pageable paging = PageRequest.of(pageNo,size);
        Page<Product> productPage;
        //get unexpired products
        if(isUnexpired.equals("true")){
            //search unexpired products
            if(productName!=null){
                productPage = productRepository.searchUnexpiredProducts(supplierName,productName,paging);
            }
            //get all unexpired products
            else{
                productPage = productRepository.getUnexpiredProducts(supplierName,paging);
            }
        }
        else{
            //search for the products in a supplier's inventory
            if(productName!=null){
                productPage = productRepository.searchProductsFromSupplierInventory(productName,supplierName,paging);
            }
            //get all products in the supplier's inventory
            else {
                productPage = productRepository.getAllProductsBySupplierName(supplierName,paging);
            }
        }
        List<Product>productList = productPage.getContent();
        if(productList!=null){
            return new ResponseEntity<List<Product>>(productList,new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}
