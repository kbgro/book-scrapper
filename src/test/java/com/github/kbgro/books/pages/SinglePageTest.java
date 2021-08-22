package com.github.kbgro.books.pages;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;


class SinglePageTest {
    private WebDriver driver;
    private SinglePage singlePage;

    @BeforeEach
    public void setUp() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://books.toscrape.com");

        singlePage = new SinglePage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testNextPage() {
        driver.get("https://books.toscrape.com/catalogue/page-50.html");
        Assertions.assertFalse(singlePage.hasNextPage());
        Assertions.assertThrows(NoSuchElementException.class, () -> singlePage.next.getText());
    }

    @Test
    public void testPrevPage() {
        Assertions.assertFalse(singlePage.hasPrevPage());
        Assertions.assertFalse(singlePage.hasPrevLink());
        Assertions.assertThrows(NoSuchElementException.class, () -> singlePage.previous.getText()
        );
    }

    @Test
    public void testNextPrevPage() {
        driver.get("https://books.toscrape.com/catalogue/page-2.html");
        Assertions.assertTrue(singlePage.hasNextPage());
        Assertions.assertTrue(singlePage.hasPrevPage());
        Assertions.assertNotNull(singlePage.getPreviousLink());
        Assertions.assertEquals(singlePage.current.getText().strip(), "Page 2 of 50");
    }

    @Test
    public void testProductLinks() {
        Assertions.assertEquals(singlePage.getProductLinks().size(), 20);
        List<String> links = singlePage.getProductLinks();
        Assertions.assertTrue(links.get(0).contains("https://books.toscrape.com/catalogue/"));
    }
}
