package com.github.kbgro.books.cli;

import com.github.kbgro.books.utils.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Cli {
    private final Map<String, String> map = new HashMap<>();
    private boolean useDb;

    public Map<String, String> getMap() {
        return map;
    }

    public boolean useDb() {
        return useDb;
    }

    /**
     * Add a command and reset key
     *
     * @param key   key to add to map
     * @param value key value
     * @return reset strings
     */
    private String addCommand(String key, String value) {
        map.put(key, value);
        return "";
    }

    /**
     * Commands validator
     *
     * @throws Exception Throw an exception on fail to validate a command
     */
    public void validate() throws Exception {
        // Validate db
        if (map.containsKey("-p") || map.containsKey("-db") || map.containsKey("-u")) {
            String db = map.get("-db");
            String user = map.get("-u");
            String pass = map.get("-p");
            String port = map.getOrDefault("-port", "3306");
            String host = map.getOrDefault("-host", "127.0.0.1");
            String dbUrl = String.format("jdbc:mysql://%s:%s/%s", host, port, db);
            try {
                Connection conn = DriverManager.getConnection(dbUrl, user, pass);
                useDb = true;
                map.put("-dbUrl", dbUrl);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Invalid database credentials");
            }
        }

        // -page
        if (map.containsKey("-page")) {
            int page = Integer.parseInt(map.get("-page"));
            if (page < 1 || page > 50) {
                throw new Exception("Invalid page");
            }
        }

        // limit
        if (map.containsKey("-l")) {
            int page = Integer.parseInt(map.get("-l"));
            if (page < 1) {
                throw new Exception("Invalid page");
            }
        }
    }

    /**
     * Parse user options
     *
     * @param args command line arguments
     * @throws Exception raised on invalid or non enough arguments.
     */
    public void parse(String[] args) throws Exception {
        if (args.length < 2)
            throw new Exception("Not enough arguments.");

        String currentKey = "";
        for (final String arg : args) {
            if (arg.startsWith("-")) {
                if (arg.length() < 2) {
                    throw new Exception("Not enough arguments.");
                } else if (arg.contains("-")) {
                    currentKey = arg;
                    if (arg.equals("-all")) {
                        currentKey = addCommand(currentKey, "");
                    }
                }
            } else if (!currentKey.equals("")) {
                currentKey = addCommand(currentKey, arg);
            }
        }

        if (!currentKey.equals("")) {
            throw new Exception("Not enough arguments");
        }
    }

    /**
     * Construct a help message
     *
     * @return help string
     */
    public String help() {
        Properties prop = Util.getBooksProperties();
        int descLen = 10;
        int optLen = 5;
        String newLine = "\n";
        String tab = "\t";
        String exampleTitle = "java -jar book-scrapper.jar ";

        return prop.getProperty("APP_NAME") + " " + prop.getProperty("APP_VERSION") + newLine +

                createExampleHeading("Crawl: options", newLine) +
                createOptionHelp("-c", optLen, "category", descLen, "Scrape books by category", tab) +
                createOptionHelp("-page", optLen, "page no", descLen, "Scrape books by page from page 1-50", tab) +
                createOptionHelp("-all", optLen, "", descLen, "Scrape all books", tab) +
                createOptionHelp("-l", optLen, "limit", descLen, "Limit of books to scrape e.g 10", tab) +

                createExampleHeading("Storage: options", newLine) +
                createOptionHelp("-o", optLen, "output", descLen, "Output to a csv file", tab) +
                createOptionHelp("-p", optLen, "password", descLen, "MySQL password", tab) +
                createOptionHelp("-u", optLen, "user", descLen, "MySQL user", tab) +
                createOptionHelp("-db", optLen, "database", descLen, "MySQL database", tab) +
                createOptionHelp("-port", optLen, "database", descLen, "MySQL port, 3306 is used by default", tab) +
                createOptionHelp("-host", optLen, "database", descLen, "MySQL hot, localhost is used by default", tab) +

                createExampleHeading("Examples", newLine) +
                createExample(exampleTitle,
                        "Scrape 5 History books",
                        "-c History -l 5 -o history-books.csv",
                        tab, newLine) +
                createExample(exampleTitle,
                        "Scrape 5 books from page 22",
                        "-page 22 -l 5 -o page22-books.csv",
                        tab, newLine) +
                createExample(exampleTitle,
                        "SScrape all books",
                        "-all -o page22-books.csv",
                        tab, newLine) +
                createExample(exampleTitle,
                        "Scrape all books and save to database",
                        "-all -p password -u user -db books",
                        tab, newLine) +
                createExample(exampleTitle,
                        "Scrape 5 Fiction books and save to database providing host and port",
                        "-all -host 127.0.0.1 -p 13306 -p password -u user -db books",
                        tab, newLine);
    }

    /**
     * Helper method for padding a help line
     *
     * @param option  option string
     * @param optLen  option string padding length
     * @param desc    option command description
     * @param descLen description padding length
     * @param help    option help string
     * @param tab     tab character
     * @return option help string
     */
    private String createOptionHelp(String option, int optLen, String desc, int descLen, String help, String tab) {
        return Util.padRight(option, optLen) + tab + Util.padRight(desc, descLen) + tab + help + "\n";
    }

    private String createExampleHeading(String heading, String newLine) {
        return newLine + heading + newLine;
    }

    private String createExample(String title, String subTitle, String help, String tab, String newLine) {
        return subTitle + newLine + tab + title + help + newLine + newLine;
    }
}
