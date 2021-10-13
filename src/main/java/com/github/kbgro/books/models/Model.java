package com.github.kbgro.books.models;

import com.github.kbgro.books.factory.SimpleProductFactory;

import java.sql.*;

public class Model {
    final Connection conn;

    public Model(Connection conn) {
        this.conn = conn;
    }

    /**
     * INSERTS given sql string to db.
     *
     * @param product product to insert to db
     * @throws SQLException thrown in case an exception arises.
     */
    public void insert(Product product) throws SQLException {
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
        }
    }

    /**
     * Retrieve a product from db
     *
     * @param id product id
     * @return product Details
     */
    public Product get(String id) throws SQLException {
        Product product;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM books.books WHERE id =?")) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            product = new SimpleProductFactory().newProduct(resultSet);
        }
        return product;
    }
}
