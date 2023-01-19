package com.example.springbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.sql.Date;

@Entity
@Table(name="product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "batch")
    private String batch;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "deal")
    private Integer deal;

    @Column(name = "free")
    private Integer free;

    @Column(name = "mrp")
    private Float mrp;

    @Column(name = "rate")
    private Float rate;

    @Column(name = "exp")
    private Date exp;

    @Column(name = "company")
    private String company;

    @Column(name = "supplier")
    private String supplier;
}
