package com.github.kbgro.books.factory;

import com.github.kbgro.books.models.Product;
import com.github.kbgro.books.pages.ProductPage;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ProductFactory {
    Product newProduct(ProductPage productPage);

    Product newProduct(ResultSet resultSet) throws SQLException;
}
