package com.github.kbgro.books.pages;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class SinglePage implements Page {
    private final Document doc;

    Element previous;
    Element current;
    Element next;
    List<Element> productLinks;

    public SinglePage(Document doc) {
        this.doc = doc;
        initElements(doc);
    }

    @Override
    public void initElements(Document doc) {
        previous = doc.selectFirst("ul.pager li.previous a");
        current = doc.selectFirst("ul.pager li.current");
        next = doc.selectFirst("ul.pager li.next a");
        productLinks = doc.select("ol.row li .image_container>a");
    }

    public String getPreviousLink() {
        return previous.attr("abs:href");
    }

    public String getPageNumber() {
        return current != null ? current.text().strip() : "Page 1 of 1";
    }

    public String getNextLink() {
        return next.attr("abs:href");
    }

    public boolean hasNextPage() {
        return doc.selectFirst("ul.pager li.next a") != null;
    }

    public boolean hasPrevPage() {
        return doc.selectFirst("ul.pager li.previous a") != null;
    }

    public List<String> getProductLinks() {
        List<String> links = new ArrayList<>(20);
        for (final Element link : productLinks) {
            links.add(link.attr("abs:href").strip());
        }
        return links;
    }
}
