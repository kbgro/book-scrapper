package com.github.kbgro.books.repository;

import com.github.kbgro.books.models.Product;

import java.sql.SQLException;

public interface BooksRepository {
    void add(Product product);

    Product get(String id);
}
