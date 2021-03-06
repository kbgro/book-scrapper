package com.github.kbgro.books.pages;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

class ProductPageTest {
    private static Document doc;
    private static ProductPage productPage;

    @BeforeAll
    static void setUp() {
        try {
            doc = Jsoup.connect("https://books.toscrape.com/catalogue/chase-me-paris-nights-2_977/index.html").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        productPage = new ProductPage(doc);
    }

    @Test
    public void textProductTitle() {
        Assertions.assertEquals(productPage.title.text().strip(), "Chase Me (Paris Nights #2)");
    }

    @Test
    public void textProductImageUrl() {
        Assertions.assertEquals(
                productPage.productImage.attr("abs:src").strip(),
                "https://books.toscrape.com/media/cache/6c/84/6c84fcf7a53b02b6e763de7272934842.jpg"
        );
    }

    @Test
    public void textProductDescription() {
        Assertions.assertTrue(productPage.description.text().strip().contains("A Michelin two-star chef at twenty-eight"));
    }

    @Test
    public void textProductPrice() {
        Assertions.assertEquals(productPage.getPrice().toString(), "25.27");
    }

    @Test
    public void textProductCategory() {
        Assertions.assertEquals(productPage.category.text().strip(), "Romance");
    }

    @Test
    public void textProductInfo() {
        Map<String, String> infos = productPage.getProductInfo();
        Assertions.assertEquals(infos.get("Availability"), "19");
        Assertions.assertEquals(infos.get("Number of reviews"), "0");
        Assertions.assertEquals(infos.get("Product Type"), "Books");
        Assertions.assertEquals(infos.get("UPC"), "c2e46a2ee3b4a322");
        Assertions.assertEquals(infos.get("Tax"), "0.00");
    }
}
