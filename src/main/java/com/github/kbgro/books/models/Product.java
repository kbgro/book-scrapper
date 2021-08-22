package com.github.kbgro.books.models;

import java.math.BigDecimal;

public class Product {
    String id;
    String title;
    String description;
    String imageUrl;
    String category;
    BigDecimal tax;
    BigDecimal price;
    int stock;
    int numberOfReviews;

    public Product(String id, String title, String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
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

    public String sqlString() {
        return String.format("INSERT INTO books VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s',%6$s,%7$s,%8$s,%9$s)",
                id, title, description, imageUrl, category, tax, price, stock, numberOfReviews);
    }
}
