package com.github.kbgro.books.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class SinglePage {
    private final WebDriver driver;

    @FindBy(css = "ul.pager li.previous a")
    WebElement previous;

    @FindBy(css = "ul.pager li.next a")
    WebElement next;

    @FindBy(css = "ul.pager li.current")
    WebElement current;

    @FindBy(css = "ol.row li .image_container>a")
    List<WebElement> productLinks;

    public SinglePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean hasNextPage() {
        return nextPrevElement("ul.pager li.next a") != null;
    }

    public boolean hasPrevPage() {
        return nextPrevElement("ul.pager li.previous a") != null;
    }

    public List<String> getProductLinks() {
        List<String> links = new ArrayList<>(20);
        for (final WebElement link : productLinks)
            links.add(link.getAttribute("href").strip());
        return links;
    }

    private WebElement nextPrevElement(String css) {
        WebElement nextPrev = null;
        try {
            nextPrev = driver.findElement(By.cssSelector(css));
        } catch (NoSuchElementException ignored) {
        }
        return nextPrev;
    }
}
