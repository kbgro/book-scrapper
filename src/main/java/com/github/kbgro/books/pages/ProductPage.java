package com.github.kbgro.books.pages;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductPage implements Page {
    Element productImage;
    Element title;
    Element price;
    Element stock;
    Element description;
    Element category;
    List<Element> productInfo;

    public ProductPage(Document doc) {
        initElements(doc);
    }

    @Override
    public void initElements(Document doc) {
        productImage = doc.selectFirst("#product_gallery img");
        title = doc.selectFirst(".col-sm-6.product_main h1");
        price = doc.selectFirst(".col-sm-6.product_main p.price_color");
        stock = doc.selectFirst(".col-sm-6.product_main p.instock.availability");
        description = doc.selectFirst("#product_description + p");
        category = doc.selectFirst("ul.breadcrumb li:nth-last-child(2)");
        productInfo = doc.select(".product_page table tr");
    }

    public BigDecimal getPrice() {
        return extractMoney(price);
    }

    public String getTitle() {
        return title.text().strip();
    }

    public String getDescription() {
        return description.text().strip();
    }

    public String getProductImageUrl() {
        return productImage.attr("abs:src").strip();
    }

    public String getCategory() {
        return category.text().strip();
    }

    public Map<String, String> getProductInfo() {
        Map<String, String> productInfos = new HashMap<>();
        for (final Element tr : productInfo) {
            String th = Objects.requireNonNull(tr.selectFirst("th")).text().strip();
            String td = Objects.requireNonNull(tr.selectFirst("td")).text().strip();
            productInfos.put(th, td);
        }
        productInfos.put("Availability", extractMoney(stock).toString());
        productInfos.put("Tax", extractMoney(productInfos.get("Tax")).toString());
        return productInfos;
    }

    private BigDecimal extractMoney(Element element) {
        return extractMoney(element.text());
    }

    private BigDecimal extractMoney(String element) {
        Matcher m = Pattern.compile("\\d+.?\\d{1,2}").matcher(element.strip());
        BigDecimal money = new BigDecimal(0);
        while (m.find()) {
            money = new BigDecimal(m.group());
        }
        return money;
    }
}
