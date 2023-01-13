package com.example.springbackend.repositories;

import com.example.springbackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product>searchProductsByNameIsContainingIgnoreCase(String name,Pageable page);
    Page<Product>findAllBySupplierContainingIgnoreCase(String name,Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.exp > CURRENT_DATE and p.stock>0 and length(p.supplier)>0 ")
    Page<Product>findUnExpiredProducts(Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.exp > CURRENT_DATE AND p.supplier=:name")
    Page<Product>filterProductsUsingNameAndExpiryDate(String name,Pageable pageable);
}
