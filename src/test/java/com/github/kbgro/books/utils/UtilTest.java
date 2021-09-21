package com.github.kbgro.books.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class UtilTest {
    @Test
    public void testProperties() {
        try {
            Properties prop = Util.getBooksProperties();
            Assertions.assertNotNull(prop.getProperty("DB_URL"));
            Assertions.assertNotNull(prop.getProperty("DB_USER"));
            Assertions.assertNotNull(prop.getProperty("DB_PASSWORD"));
        } catch (Exception noEnvironmentException) {
            noEnvironmentException.printStackTrace();
        }
    }

    @Test
    public void testConn() throws Exception {
        Properties prop = Util.getBooksProperties();
        String DB_URL =prop.getProperty("DB_URL");
        String USER =prop.getProperty("DB_USER");
        String PASS = prop.getProperty("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("Connection successful!");
            Util.executeSqlScript(conn);
            System.out.println("Script executed successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}