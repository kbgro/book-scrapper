package com.github.kbgro.books.factory;

import com.github.kbgro.books.pages.ProductPage;
import com.github.kbgro.books.models.Product;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SimpleProductFactory implements ProductFactory {
    @Override
    public Product newProduct(ProductPage productPage) {
        Map<String, String> infos = productPage.getProductInfo();
        Product p = new Product();
        p.setId(infos.get("UPC"));
        p.setTitle(productPage.getTitle());
        p.setDescription(productPage.getDescription());
        p.setImageUrl(productPage.getProductImageUrl());
        p.setCategory(productPage.getCategory());
        p.setPrice(productPage.getPrice());
        p.setTax(new BigDecimal(infos.get("Tax")));
        p.setStock(Integer.parseInt(infos.get("Availability")));
        p.setNumberOfReviews(Integer.parseInt(infos.get("Number of reviews")));
        return p;
    }

    @Override
    public Product newProduct(ResultSet resultSet) throws SQLException {
        resultSet.next();
        Product p = new Product();
        p.setId(resultSet.getString(1));
        p.setTitle(resultSet.getString(2));
        p.setDescription(resultSet.getString(3));
        p.setImageUrl(resultSet.getString(4));
        p.setCategory(resultSet.getString(5));
        p.setPrice(new BigDecimal(resultSet.getString(7)));
        p.setTax(new BigDecimal(resultSet.getString(6)));
        p.setStock(Integer.parseInt(resultSet.getString(8)));
        p.setNumberOfReviews(Integer.parseInt(resultSet.getString(9)));
        return p;
    }
}
