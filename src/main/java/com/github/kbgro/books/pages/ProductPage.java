package com.github.kbgro.books.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductPage {
    @FindBy(css = "#product_gallery img")
    WebElement productImage;

    @FindBy(css = ".col-sm-6.product_main h1")
    WebElement title;


    @FindBy(css = ".col-sm-6.product_main p.price_color")
    WebElement price;

    @FindBy(css = ".col-sm-6.product_main p.instock.availability")
    WebElement stock;

    @FindBy(css = "#product_description + p")
    WebElement description;

    @FindBy(css = "ul.breadcrumb li:nth-last-child(2)")
    WebElement category;

    @FindBy(css = ".product_page table tr")
    List<WebElement> productInfo;

    public ProductPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public BigDecimal getPrice() {
        return extractMoney(price);
    }

    public String getTitle() {
        return title.getText().strip();
    }

    public String getDescription() {
        return description.getText().strip();
    }

    public String getProductImageUrl() {
        return productImage.getAttribute("src").strip();
    }

    public String getCategory() {
        return category.getText().strip();
    }

    public Map<String, String> getProductInfo() {
        Map<String, String> productInfos = new HashMap<>();
        for (final WebElement tr : productInfo) {
            String th = tr.findElement(By.tagName("th")).getText().strip();
            String td = tr.findElement(By.tagName("td")).getText().strip();
            productInfos.put(th, td);
        }
        productInfos.put("Availability", extractMoney(stock).toString());
        productInfos.put("Tax", extractMoney(productInfos.get("Tax")).toString());
        return productInfos;
    }

    private BigDecimal extractMoney(WebElement element) {
        return extractMoney(element.getText());
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
