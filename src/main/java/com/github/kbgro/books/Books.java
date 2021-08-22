package com.github.kbgro.books;

import com.github.kbgro.books.factory.SimpleProductFactory;
import com.github.kbgro.books.models.Model;
import com.github.kbgro.books.models.Product;
import com.github.kbgro.books.pages.HomePage;
import com.github.kbgro.books.pages.ProductPage;
import com.github.kbgro.books.pages.SinglePage;
import com.github.kbgro.books.utils.Util;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class Books {
    private static final Logger logger = LoggerFactory.getLogger(Books.class);
    private Map<String, WebElement> categoryLinks;
    private Set<String> categories;
    private WebDriver driver;
    private Model model;
    Connection conn;


    public void setUp() throws Exception {
        logger.info("Setting Up Books...");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://books.toscrape.com");

        logger.info("Creating db Connection");
        Properties prop = Util.getBooksProperties();
        String dbUrl = prop.getProperty("DB_URL");
        String dbUser = prop.getProperty("DB_USER");
        String dbPassword = prop.getProperty("DB_PASSWORD");
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        logger.info("Creating Table App Start.");
        Util.executeSqlScript(conn);
        logger.info("Clearing Table...");
        PreparedStatement statement = conn.prepareStatement("DELETE FROM books.books");
        statement.executeUpdate();
        statement.close();

        logger.info("Creating Model Object.");
        model = new Model(conn);

        logger.info("Collecting Homepage");
        HomePage homePage = new HomePage(driver);
        categoryLinks = homePage.titledCategories();
        categories = categoryLinks.keySet();
    }

    public void tearDown() {
        logger.info("Shutting down driver.");
        driver.quit();
        try {
            logger.info("Closing DB Connection.");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Books() throws Exception {
        setUp();
    }

    /**
     * Scrape by category.
     *
     * @param category book category name
     */
    public void scrapeByCategory(String category) {
    }

    /**
     * Scape whole site.
     */
    public void scrapeE2E() throws SQLException, InterruptedException {
        logger.info("[ E2E ] Starting E2E");
        SinglePage singlePage = null;

        do {
            if (singlePage != null) {
                driver.get(singlePage.getNextLink());
                Thread.sleep(1500);
            }
            singlePage = new SinglePage(driver);
            String starter = String.format("[ E2E ] ( %s ):", singlePage.getPageNumber());

            logger.info("{} Starting E2E", starter);
            List<String> productLinks = singlePage.getProductLinks();
            int itemNumber = 1;
            for (final String link : productLinks) {
                logger.info("{} [ " + itemNumber + " ] Trying product --> " + link, starter);
                driver.get(link);

                final ProductPage productPage = new ProductPage(driver);
                logger.info("{} Collecting Product Page, Title:: " + productPage.getTitle(), starter);

                final Product product = new SimpleProductFactory().newProduct(productPage);
                logger.info("{} Creating Product Model, Id:: " + product.getId(), starter);

                logger.info("{} Inserting, Id:: " + product.getId() + " to Database.", starter);
                model.insert(product);
                logger.info("{} Insertion for , Product with Id:: " + product.getId() + " to Database successful!", starter);

                itemNumber++;
            }
        } while (singlePage.hasNextLink());

        logger.info("[ E2E ] Stopping E2E");
    }

    /**
     * Main Function
     */
    public static void main(String[] args) {
        logger.info("Starting Application");
        Books books = null;
        try {
            books = new Books();
            books.scrapeE2E();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (books != null)
                books.tearDown();
        }
        logger.info("Exiting Application");
    }
}
