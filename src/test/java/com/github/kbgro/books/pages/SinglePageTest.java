package com.github.kbgro.books.pages;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;


class SinglePageTest {
    private Document doc;
    private SinglePage singlePage;

    @Test
    public void testNextPage() throws IOException {
        doc = Jsoup.connect("https://books.toscrape.com/catalogue/page-50.html").get();
        singlePage = new SinglePage(doc);
        Assertions.assertFalse(singlePage.hasNextPage());
        Assertions.assertNull(singlePage.next);
    }

    @Test
    public void testPrevPage() throws IOException {
        doc = Jsoup.connect("https://books.toscrape.com").get();
        singlePage = new SinglePage(doc);
        Assertions.assertFalse(singlePage.hasPrevPage());
        Assertions.assertTrue(singlePage.hasNextPage());
        Assertions.assertNull(singlePage.previous);
    }

    @Test
    public void testNextPrevPage() throws IOException {
        doc = Jsoup.connect("https://books.toscrape.com/catalogue/page-2.html").get();
        singlePage = new SinglePage(doc);
        Assertions.assertTrue(singlePage.hasNextPage());
        Assertions.assertTrue(singlePage.hasPrevPage());
        Assertions.assertNotNull(singlePage.getPreviousLink());
        Assertions.assertEquals(singlePage.getPageNumber(), "Page 2 of 50");
    }

    @Test
    public void testProductLinks() throws IOException {
        doc = Jsoup.connect("https://books.toscrape.com").get();
        singlePage = new SinglePage(doc);
        Assertions.assertEquals(singlePage.getProductLinks().size(), 20);
        List<String> links = singlePage.getProductLinks();
        Assertions.assertTrue(links.get(0).contains("https://books.toscrape.com/catalogue/"));
    }
}
