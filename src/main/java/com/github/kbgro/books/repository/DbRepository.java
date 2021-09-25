package com.github.kbgro.books.repository;

import com.github.kbgro.books.factory.SimpleProductFactory;
import com.github.kbgro.books.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbRepository implements BooksRepository {
    final Connection conn;

    public DbRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void add(Product product) {
        String sqlString = "INSERT INTO books.books VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, product.getId());
            statement.setString(2, product.getTitle());
            statement.setString(3, product.getDescription());
            statement.setString(4, product.getImageUrl());
            statement.setString(5, product.getCategory());
            statement.setBigDecimal(6, product.getTax());
            statement.setBigDecimal(7, product.getPrice());
            statement.setInt(8, product.getStock());
            statement.setInt(9, product.getNumberOfReviews());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product get(String id) {
        Product product = null;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM books.books WHERE id =?")) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            product = new SimpleProductFactory().newProduct(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
}
