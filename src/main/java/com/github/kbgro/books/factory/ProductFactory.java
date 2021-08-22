package com.github.kbgro.books.factory;

import com.github.kbgro.books.ProductPage;
import com.github.kbgro.books.models.Product;

public interface ProductFactory {
    Product newProduct(ProductPage productPage);
}
