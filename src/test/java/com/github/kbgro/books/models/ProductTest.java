package com.github.kbgro.books.models;

import com.github.kbgro.books.pages.ProductPage;
import com.github.kbgro.books.factory.SimpleProductFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

class ProductTest {
    private static WebDriver driver;
    private static Product product;

    @BeforeAll
    static void setUp() {
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
    public void testId() {
        Assertions.assertEquals(product.getId(), "c2e46a2ee3b4a322");
    }

    @Test
    public void testTitle() {
        Assertions.assertEquals(product.getTitle(), "Chase Me (Paris Nights #2)");
    }

    @Test
    public void testProductImage() {
        Assertions.assertEquals(
                product.getImageUrl(),
                "https://books.toscrape.com/media/cache/6c/84/6c84fcf7a53b02b6e763de7272934842.jpg"
        );
    }

    @Test
    public void testCategory() {
        Assertions.assertEquals(product.getCategory(), "Romance");
    }

    @Test
    public void testPrice() {
        Assertions.assertEquals(product.getPrice(), new BigDecimal("25.27"));
    }

    @Test
    public void testTax() {
        Assertions.assertEquals(product.getTax(), new BigDecimal("0.00"));
    }

    @Test
    public void testStock() {
        Assertions.assertEquals(product.getStock(), 19);
    }

    @Test
    public void testNumOfReviews() {
        Assertions.assertEquals(product.getNumberOfReviews(), 0);
    }

}