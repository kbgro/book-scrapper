package com.github.kbgro.books.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Util {
    public static void executeSqlScript(Connection conn) {
        String delimiter = ";";
        Scanner scanner;
        String file = Objects.requireNonNull(Util.class.getClassLoader().getResource("books.sql")).getFile();

        try {
            scanner = new Scanner(new File(file)).useDelimiter(delimiter);
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        Statement currentStatement = null;
        while (scanner.hasNext()) {
            String rawStatement = scanner.next() + delimiter;
            try {
                currentStatement = conn.createStatement();
                currentStatement.execute(rawStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (currentStatement != null) {
                    try {
                        currentStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                currentStatement = null;
            }
        }
        scanner.close();
    }

    public static Properties getBooksProperties() throws Exception {
        Map<String, String> envs = System.getenv();
        Properties prop = new Properties();
        String db = envs.get("MYSQL_DATABASE");
        String user = envs.get("MYSQL_USER");
        String password = envs.get("MYSQL_PASSWORD");

        if (db == null || user == null || password == null) {
            throw new Exception("Set MYSQL_DATABASE, MYSQL_USER and MYSQL_PASSWORD environmental variables.");
        }

        prop.setProperty("DB_URL", "jdbc:mysql://localhost:3306/" + db);
        prop.setProperty("DB_USER", user);
        prop.setProperty("DB_PASSWORD", password);
        return prop;
    }
}
