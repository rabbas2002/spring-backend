package com.example.springbackend.repositories;

import com.example.springbackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product>searchProductsByNameIsContainingIgnoreCase(String name,Pageable page);
    Page<Product>findAllBySupplierContainingIgnoreCase(String name,Pageable pageable);
    @Query("SELECT p FROM Product p WHERE  p.stock>0 AND p.supplier LIKE %:supplierName%")
    Page<Product>filterProductsUsingSupplierName(@Param("supplierName") String supplierName,Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.supplier LIKE %:supplier% AND p.name LIKE %:productName%")
    Page<Product>filterProductsBySupplierAndProductName(@Param("supplier")String supplier,@Param("productName")String productName,Pageable pageable);


}

