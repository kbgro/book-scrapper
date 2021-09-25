package com.github.kbgro.books.books;

import com.github.kbgro.books.App;
import com.github.kbgro.books.factory.SimpleProductFactory;
import com.github.kbgro.books.models.Product;
import com.github.kbgro.books.pages.HomePage;
import com.github.kbgro.books.pages.ProductPage;
import com.github.kbgro.books.pages.SinglePage;
import com.github.kbgro.books.repository.BooksRepository;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BookBase {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    protected static final String BOOK_URL = "https://books.toscrape.com";

    protected WebDriver driver;
    protected Map<String, String> categoryLinks;
    protected Set<String> categories;
    protected BooksRepository booksRepository;

    protected int processedLinks;
    protected int limit = 0;

    public void loadHome() {
        if (categories != null)
            return;
        driver.get(BOOK_URL);
        HomePage homePage = new HomePage(driver);
        categoryLinks = homePage.getCategoryLinks();
        categories = categoryLinks.keySet();
        logger.debug(String.valueOf(categoryLinks));
        logger.debug(String.valueOf(categoryLinks.values()));
    }

    public BookBase(BooksRepository repository, WebDriver driver) {
        this.booksRepository = repository;
        this.driver = driver;
        loadHome();
    }

    public String getCategoriesNames() {
        logger.info("Collecting Homepage");

        Object[] results = categoryLinks.keySet().toArray();
        Arrays.sort(results);
        return StringUtils.join(results, "\n");
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
     * Scrape by page
     *
     * @param page page number to be scraped.
     */
    public void scrapeByPage(int page) {
        String pageUrl = String.format(BOOK_URL + "/catalogue/page-%d.html", page);

        logger.info("[ Page ] ({}) Starting", page);

        driver.get(pageUrl);
        processPageProducts(
                new SinglePage(driver).getProductLinks(),
                String.format("[ Page ] ( %s ):", page)
        );

        logger.info("[ Page ] ({}) Finishing!s", page);
    }

    /**
     * Scape whole site.
     */
    public void scrapeE2E() {
        logger.info("[ E2E ] Starting E2E");

        driver.get(BOOK_URL);
        scrapeNext(new SinglePage(driver));

        logger.info("[ E2E ] Stopping E2E");
    }

    @SuppressWarnings("BusyWait")
    public void scrapeNext(SinglePage startPage) {
        SinglePage singlePage = startPage;
        String starter = String.format("[ scrapeNext ] ( %s ):", singlePage.getPageNumber());
        boolean started = false;
        do {
            limit++;
            if (started) {
                driver.get(singlePage.getNextLink());
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored) {
                }
                singlePage = new SinglePage(driver);
            }

            logger.info("{} Starting E2E", starter);

            boolean finished = processPageProducts(singlePage.getProductLinks(), starter);
            if (finished) break;
            if (!started) started = true;
        }
        while (singlePage.hasNextLink());
    }

    private boolean processPageProducts(List<String> productLinks, String starter) {
        int itemNumber = 1;
        for (final String link : productLinks) {
            logger.info("{} [ " + itemNumber + " ] Trying product --> " + link, starter);
            driver.get(link);

            final ProductPage productPage = new ProductPage(driver);
            logger.info("{} Collecting Product Page, Title:: " + productPage.getTitle(), starter);

            final Product product = new SimpleProductFactory().newProduct(productPage);
            logger.info("{} Creating Product Model, Id:: " + product.getId(), starter);

            logger.info("{} Inserting, Id:: " + product.getId() + " to Database.", starter);
            booksRepository.add(product);
            logger.info("{} Insertion for , Product with Id:: " + product.getId() + " to Database successful!", starter);

            itemNumber++;
            processedLinks++;
            if (limit > 0 && processedLinks >= limit)
                return true;
        }
        return false;
    }
}
