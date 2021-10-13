package com.github.kbgro.books.models;

import com.github.kbgro.books.factory.SimpleProductFactory;
import com.github.kbgro.books.pages.ProductPage;
import com.github.kbgro.books.utils.Env;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class ModelTest {

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private static Document doc;
    private static Product product;

    @BeforeAll
    static void setUp() {
        Env env = new Env();
        dbUrl = env.getEnv("DB_URL");
        dbUser = env.getEnv("DB_USER");
        dbPassword = env.getEnv("DB_PASSWORD");

        try {
            doc = Jsoup.connect("https://books.toscrape.com/catalogue/chase-me-paris-nights-2_977/index.html").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProductPage productPage = new ProductPage(doc);
        product = new SimpleProductFactory().newProduct(productPage);

    }

    @Test
    public void testInsert() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            Model model = new Model(conn);
            model.insert(product);
            Product fetchedProduct = model.get(product.getId());
            Assertions.assertEquals(fetchedProduct.getId(), product.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
