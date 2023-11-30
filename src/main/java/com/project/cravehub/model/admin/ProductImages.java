package com.project.cravehub.model.admin;

import com.project.cravehub.dto.ProductDto;

import javax.persistence.*;

@Entity
@Table(name="product_image")
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    @Column(name = "image_name", nullable = false)
    private String imageName;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;



    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductImages(Integer imageId, String imageName, Product product) {
        super();
        this.imageId = imageId;
        this.imageName = imageName;

        this.product = product;
    }

    public ProductImages() {
        super();
    }
}