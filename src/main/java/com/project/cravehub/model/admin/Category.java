package com.project.cravehub.model.admin;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Category_products")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;

    @Column(name = "category")
    private String category;

    private boolean isEnabled;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<SubCategory> subcategories = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_offer_id")
    private CategoryOffer categoryOffer;

    @ManyToMany
    private Set<Product> products = new HashSet<>();


    public Category(){

    }

    public Category(String category, boolean isEnabled, Set<SubCategory> subcategories, Set<Product> products) {
        this.category = category;
        this.isEnabled = isEnabled;
        this.subcategories = subcategories;
//        this.products = products;
    }

    public Category(String category) {
        this.category = category;
    }

    public Category(List<SubCategory> subcategories) {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Set<SubCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Set<SubCategory> subcategories) {
        this.subcategories = subcategories;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }


    public CategoryOffer getCategoryOffer() {
        return categoryOffer;
    }

    public void setCategoryOffer(CategoryOffer categoryOffer) {
        this.categoryOffer = categoryOffer;
    }
}