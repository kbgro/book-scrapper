package com.github.kbgro.books.models;

import java.math.BigDecimal;

public class Product {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String category;
    private BigDecimal tax;
    private BigDecimal price;
    private int stock;
    private int numberOfReviews;

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%d,%d",
                id, title, description, imageUrl, category, tax, price, stock, numberOfReviews);
    }
}
