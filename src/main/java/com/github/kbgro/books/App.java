package com.github.kbgro.books;

import com.github.kbgro.books.books.Books;
import com.github.kbgro.books.cli.Cli;
import com.github.kbgro.books.cli.Command.CategoryCommand;
import com.github.kbgro.books.cli.Command.ListCategoriesCommand;
import com.github.kbgro.books.cli.Command.PageCommand;
import com.github.kbgro.books.cli.Option;
import com.github.kbgro.books.factory.ConnectionFactory;
import com.github.kbgro.books.repository.BooksRepository;
import com.github.kbgro.books.repository.CsvRepository;
import com.github.kbgro.books.repository.DbRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;


public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static Connection conn;

    public static void tearDown() {
        try {
            if (conn != null) {
                logger.info("Closing DB Connection.");
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main Function
     */
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(App::tearDown));

        Cli c = new Cli();

        try {
            c.parse(args);
            c.validate();

            if (c.getMap().containsKey("-h")) {
                System.out.println(c.help());
                System.exit(0);
            }

            int limit = Integer.parseInt(c.getMap().getOrDefault("-l", String.valueOf(0)));
            BooksRepository repository;

            if (c.useDb()) {
                conn = ConnectionFactory.getConnection(c.getMap().get("-dbUrl"), c.getMap().get("-u"), c.getMap().get("-p"));
                repository = new DbRepository(conn);
            } else {
                String filename = c.getMap().getOrDefault("-o", "");
                if (filename.equals("")) {
                    File tempFile = File.createTempFile("prefix-", "-suffix");
                    tempFile.deleteOnExit();
                }
                repository = new CsvRepository(filename);
            }

            logger.info("Starting Application...");

            Books books = new Books(repository, limit);

            Option option = new Option();

            if (c.getMap().containsKey("-lc")) {
                String res = option.executeCommand(new ListCategoriesCommand(books));
                System.out.println(res);
                System.exit(0);
            }
            if (c.getMap().containsKey("-c")) {
                option.executeCommand(new CategoryCommand(books, c.getMap().get("-c")));
            } else if (c.getMap().containsKey("-page")) {
                option.executeCommand(new PageCommand(books, Integer.parseInt(c.getMap().get("-page"))));
            } else if (c.getMap().containsKey("-all")) {
                option.executeCommand(new PageCommand(books, Integer.parseInt(c.getMap().get("-all"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(c.help());
        }

        logger.info("Exiting Application...");
    }
}
