package com.example.springbackend.repositories;

import com.example.springbackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //to list all the products of a given supplier
    @Query("SELECT p FROM Product p WHERE p.supplier=:supplierName AND p.stock>0")
    Page<Product>getAllProductsBySupplierName(@Param("supplierName") String supplierName,Pageable pageable);
    //to search the products in the supplier's inventory.
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:productName% AND p.supplier=:supplierName AND p.stock>0 ")
    Page<Product>searchProductsFromSupplierInventory(@Param("productName")String productName,@Param("supplierName")String supplierName,Pageable pageable);
    //to get only the products that didn't expire in the supplier's inventory
    @Query("SELECT p FROM Product p WHERE p.supplier=:supplierName AND p.stock>0 AND p.exp>CURRENT_DATE ")
    Page<Product>getUnexpiredProducts(@Param("supplierName")String supplierName,Pageable pageable);
    //to search the unexpired products in the supplier's inventory
    @Query("SELECT p FROM Product p WHERE p.supplier=:supplierName AND p.stock>0 AND p.exp>CURRENT_DATE AND p.name LIKE %:productName%")
    Page<Product>searchUnexpiredProducts(@Param("supplierName")String supplierName,@Param("productName")String productName,Pageable pageable);

}

