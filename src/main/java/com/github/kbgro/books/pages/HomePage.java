package com.github.kbgro.books.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage {
    @FindBy(css = "div.side_categories>ul a[href^=catalogue]")
    public List<WebElement> categories;
    public Map<String, String> categoryLinks = new HashMap<>();

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        setCategoryLinks();
    }

    public Map<String, String> getCategoryLinks() {
        return categoryLinks;
    }

    public Map<String, WebElement> titledCategories() {
        Map<String, WebElement> cat = new HashMap<>();
        for (final WebElement link : categories) {
            cat.put(link.getText(), link);
            categoryLinks.put(link.getText(), link.getAttribute("href"));
        }
        return cat;
    }

    private void setCategoryLinks() {
        for (final WebElement link : categories) {
            categoryLinks.put(link.getText(), link.getAttribute("href"));
        }
    }
}
