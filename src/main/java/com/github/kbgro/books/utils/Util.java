package com.github.kbgro.books.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
        InputStream stream = Util.class.getClassLoader().getResourceAsStream("books.sql");

        assert stream != null;
        scanner = new Scanner(stream).useDelimiter(delimiter);

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

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
