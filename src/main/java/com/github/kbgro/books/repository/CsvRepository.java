package com.github.kbgro.books.repository;

import com.github.kbgro.books.models.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CsvRepository implements BooksRepository {
    final Path filename;
    private boolean hasHeader;

    public CsvRepository(String filename) {
        this.filename = Paths.get(System.getProperty("user.dir"), filename);
    }

    @Override
    public void add(Product product) {
        try {
            if (!hasHeader) {
                Files.deleteIfExists(filename);
                Files.createFile(filename);
                String header = "id,title,description,imageUrl,category,tax,price,stock,numberOfReviews";
                Files.write(filename, (header + "\n").getBytes(), StandardOpenOption.APPEND);
                hasHeader = true;
            }
            Files.write(filename, (product + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product get(String id) {
        return null;
    }
}
