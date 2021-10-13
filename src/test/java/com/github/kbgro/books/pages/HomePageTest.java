package com.github.kbgro.books.pages;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class HomePageTest {
    private HomePage homePage;
    private Document doc;

    @BeforeEach
    public void setUp() {
        try {
            doc = Jsoup.connect("https://books.toscrape.com").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        homePage = new HomePage(doc);
    }

    @Test
    public void testCategories() {
        var cat = homePage.titledCategories();
        Assertions.assertEquals(cat.keySet().size(), 51);
    }
}
