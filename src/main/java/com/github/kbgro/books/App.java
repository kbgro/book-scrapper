package com.github.kbgro.books;

import com.github.kbgro.books.books.Books;
import com.github.kbgro.books.cli.Cli;
import com.github.kbgro.books.cli.Command.CategoryCommand;
import com.github.kbgro.books.cli.Command.PageCommand;
import com.github.kbgro.books.cli.Option;
import com.github.kbgro.books.factory.ConnectionFactory;
import com.github.kbgro.books.factory.DriverFactory;
import com.github.kbgro.books.repository.BooksRepository;
import com.github.kbgro.books.repository.CsvRepository;
import com.github.kbgro.books.repository.DbRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static Connection conn;
    private static WebDriver driver;

    public static void tearDown() {
        try {
            logger.info("Closing DB Connection.");
            if (conn != null) {
                conn.close();
            }

            logger.info("Shutting down driver.");
            driver.quit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (WebDriverException ignored) {
        }
    }

    /**
     * Main Function
     */
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            App.tearDown();
            System.out.println("Interrupted by Ctrl+C");
        }));

        Cli c = new Cli();

        try {
            c.parse(args);
            c.validate();
            System.out.println(c.getMap());

            int limit = Integer.parseInt(c.getMap().getOrDefault("-l", String.valueOf(0)));
            BooksRepository repository;
            driver = DriverFactory.getDriver();

            if (c.useDb()) {
                conn = ConnectionFactory.getConnection(c.getMap().get("-dbUrl"), c.getMap().get("-u"), c.getMap().get("-p"));
                repository = new DbRepository(conn);
            } else {
                repository = new CsvRepository(c.getMap().get("-o"));
            }

            Books books = new Books(repository, driver, limit);

            Option option = new Option();

            if (c.getMap().containsKey("-c")) {
                option.executeCommand(new CategoryCommand(books, c.getMap().get("-c")));
            } else if (c.getMap().containsKey("-page")) {
                option.executeCommand(new PageCommand(books, Integer.parseInt(c.getMap().get("-page"))));
            } else if (c.getMap().containsKey("-all")) {
                option.executeCommand(new PageCommand(books, Integer.parseInt(c.getMap().get("-all"))));
            }
        } catch (Exception ignored) {
            System.out.println(c.help());
        }
    }
}
