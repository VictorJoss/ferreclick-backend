package com.victordev.ferreclickbackend.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("producId")
    private Long id;
    private String name;
    private String description;
    private String image;
    private Double price;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Product_ProductCategory> categories;
}