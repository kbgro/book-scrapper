package com.github.kbgro.books.pages;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage {
    public List<Element> categories;
    public Map<String, String> categoryLinks = new HashMap<>();

    public HomePage(Document doc) {
        initElements(doc);
    }

    private void initElements(Document doc) {
        categories = doc.select("div.side_categories>ul a[href^=catalogue]");
        setCategoryLinks();
    }

    public Map<String, String> getCategoryLinks() {
        return categoryLinks;
    }

    public Map<String, String> titledCategories() {
        Map<String, String> cat = new HashMap<>();
        for (final Element link : categories) {
            cat.put(link.text(), link.attr("abs:href"));
        }
        categoryLinks = cat;
        return cat;
    }

    private void setCategoryLinks() {
        for (final Element link : categories) {
            categoryLinks.put(link.text(), link.attr("abs:href"));
        }
    }
}
