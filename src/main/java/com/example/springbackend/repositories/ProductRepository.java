package com.example.springbackend.repositories;

import com.example.springbackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product>findByName(String name, Pageable pageable);
    Page<Product>searchProductsByNameIsContainingIgnoreCase(String name,Pageable pageable);

}
