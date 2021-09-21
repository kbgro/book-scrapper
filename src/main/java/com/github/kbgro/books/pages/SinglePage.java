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

    String currentText;
    String nextLink;
    String previousLink;
    boolean hasNextLink;
    boolean hasPrevLink;

    public SinglePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        initLinks();
    }

    /**
     * Initializes *Links members. This is to avoid not Such Element when a get is called on driver
     * element and webpage changes
     */
    public void initLinks() {
        if (hasPrevPage())
            previousLink = previous.getAttribute("href");
        else previousLink = null;

        if (hasNextPage())
            nextLink = next.getAttribute("href");
        else nextLink = null;

        try {
            currentText = current.getText().strip();
        } catch (NoSuchElementException ignored) {
            currentText = "Page 1 of 1";
        }
        hasNextLink = nextPrevElement("ul.pager li.next a") != null;
        hasPrevLink = nextPrevElement("ul.pager li.previous a") != null;
    }

    public String getPreviousLink() {
        return previousLink;
    }

    public String getPageNumber() {
        return currentText;
    }

    public String getNextLink() {
        return nextLink;
    }

    public boolean hasNextPage() {
        return nextPrevElement("ul.pager li.next a") != null;
    }

    public boolean hasPrevPage() {
        return nextPrevElement("ul.pager li.previous a") != null;
    }

    public boolean hasNextLink() {
        return hasNextLink;
    }

    public boolean hasPrevLink() {
        return hasPrevLink;
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
