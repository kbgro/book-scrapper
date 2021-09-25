package com.github.kbgro.books.repository;

import com.github.kbgro.books.models.Product;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvRepository implements BooksRepository {
    final Path filename;

    public CsvRepository(String filename) {
        this.filename = Paths.get(System.getProperty("user.dir"), filename);
    }

    @Override
    public void add(Product product) {
        try (PrintWriter out = new PrintWriter(new FileWriter(String.valueOf(filename)), true)) {
            out.println(product.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product get(String id) {
        return null;
    }
}
