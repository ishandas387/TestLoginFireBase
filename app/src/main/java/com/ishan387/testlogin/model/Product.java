package com.ishan387.testlogin.model;

import java.util.List;

/**
 * Created by ishan on 01-11-2017.
 */

public class Product {

    int id;
    String name;
    String description;
    String category;
    String imageUrl;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    float price;

    public Product(int id, String name, String description, String category, String imageUrl, float price, List<Review> reviewList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.price = price;
        this.reviewList = reviewList;
    }

    List<Review> reviewList;

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public Product() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public int getId() {
        return id;

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }


}
