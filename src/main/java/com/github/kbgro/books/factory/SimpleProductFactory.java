package com.github.kbgro.books.factory;

import com.github.kbgro.books.ProductPage;
import com.github.kbgro.books.models.Product;

import java.math.BigDecimal;
import java.util.Map;

public class SimpleProductFactory implements ProductFactory {
    @Override
    public Product newProduct(ProductPage productPage) {
        Map<String, String> infos = productPage.getProductInfo();
        Product p = new Product(
                infos.get("UPC"),
                productPage.getTitle(),
                productPage.getDescription(),
                productPage.getProductImageUrl()
        );
        p.setCategory(productPage.getCategory());
        p.setPrice(productPage.getPrice());
        p.setTax(new BigDecimal(infos.get("Tax")));
        p.setStock(Integer.parseInt(infos.get("Availability")));
        p.setNumberOfReviews(Integer.parseInt(infos.get("Number of reviews")));
        return p;
    }
}
