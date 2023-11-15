package com.project.cravehub.model.admin;
import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="subcategories")
public class SubCategory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subCategoryId;

    private String subCategoryName;

    @Column(name = "isEnabled", nullable = false)
    private boolean isEnabled;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @OneToMany(mappedBy = "subcategories", cascade = CascadeType.ALL)
    private List<Product> products;


    public SubCategory() {
    }

    public SubCategory(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public SubCategory(String subCategoryName, Category category) {
        this.subCategoryName = subCategoryName;
        this.category = category;
    }


    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


}
