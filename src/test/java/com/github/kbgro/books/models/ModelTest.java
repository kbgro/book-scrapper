package com.github.kbgro.books.models;

import com.github.kbgro.books.factory.SimpleProductFactory;
import com.github.kbgro.books.pages.ProductPage;
import com.github.kbgro.books.utils.Util;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

class ModelTest {

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private static WebDriver driver;
    private static Product product;

    @BeforeAll
    static void setUp() throws Exception {
        Properties prop = Util.getBooksProperties();
        dbUrl = prop.getProperty("DB_URL");
        dbUser = prop.getProperty("DB_USER");
        dbPassword = prop.getProperty("DB_PASSWORD");

        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://books.toscrape.com/catalogue/chase-me-paris-nights-2_977/index.html");

        ProductPage productPage = new ProductPage(driver);
        product = new SimpleProductFactory().newProduct(productPage);

    }

    @AfterAll
    static void tearDown() {
        driver.quit();
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