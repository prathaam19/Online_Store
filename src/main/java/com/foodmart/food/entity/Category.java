
        package com.foodmart.food.entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation
        .JsonManagedReference;

import java.util.List;

@Entity
@Table(name = "categories")

public class Category {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)

    private Long id;

    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "category")

    private List<Product> products;

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(
            List<Product> products) {

        this.products = products;
    }
}
