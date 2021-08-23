package com.github.kbgro.books;

import com.github.kbgro.books.factory.SimpleProductFactory;
import com.github.kbgro.books.models.Model;
import com.github.kbgro.books.models.Product;
import com.github.kbgro.books.pages.HomePage;
import com.github.kbgro.books.pages.ProductPage;
import com.github.kbgro.books.pages.SinglePage;
import com.github.kbgro.books.utils.Util;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;

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
    private static final String BOOK_URL = "https://books.toscrape.com";
    private Map<String, String> categoryLinks;
    private Set<String> categories;
    private static WebDriver driver;
    private Model model;
    private static Connection conn;


    public void setUp() throws Exception {
        logger.info("Setting Up Books...");
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(BOOK_URL);

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
        categoryLinks = homePage.getCategoryLinks();
        categories = categoryLinks.keySet();
    }

    public void tearDown() {
        try {
            logger.info("Closing DB Connection.");
            conn.close();

            logger.info("Shutting down driver.");
            driver.quit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (WebDriverException ignored) {
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
    public void scrapeByCategory(String category) throws Exception {
        logger.info("[ Category ] Starting");
        if (!categories.contains(category)) {
            logger.warn("No such category");
            throw new Exception(String.format("scrapeByCategory:: No such '%s' category", category));
        }
        driver.get(categoryLinks.get(category));
        SinglePage singlePage = new SinglePage(driver);
        scrapeNext(singlePage);
        logger.info("[ Category ] Finishing!");
    }

    /**
     * Scape whole site.
     */
    public void scrapeE2E() throws SQLException {
        logger.info("[ E2E ] Starting E2E");
        driver.get(BOOK_URL);
        SinglePage singlePage = new SinglePage(driver);
        scrapeNext(singlePage);
        logger.info("[ E2E ] Stopping E2E");
    }

    public void scrapeNext(SinglePage startPage) throws SQLException {
        SinglePage singlePage = startPage;
        String starter = String.format("[ scrapeNext ] ( %s ):", singlePage.getPageNumber());
        boolean started = false;
        do {
            if (started) {
                driver.get(singlePage.getNextLink());
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored) {
                }
                singlePage = new SinglePage(driver);
            }

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
                if (!started) started = true;
            }
        }
        while (singlePage.hasNextLink());
    }

    /**
     * Main Function
     */
    public static void main(String[] args) {
        logger.info("Starting Application");

        Books books = null;
        try {
            books = new Books();
            final Books finalBooks = books;

            Signal.handle(new Signal("INT"),  // SIGINT
                    signal -> {
                        finalBooks.tearDown();
                        System.out.println("Interrupted by Ctrl+C");
                    });

//            books.scrapeE2E();
            books.scrapeByCategory("History");
        } catch (WebDriverException ignored) {

        } catch (CommunicationsException ignored) {
            System.out.println("Check Database status if running");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (books != null)
                books.tearDown();
        }
    }
}
