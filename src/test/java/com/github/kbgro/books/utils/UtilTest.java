package com.github.kbgro.books.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class UtilTest {
    @Test
    public void testEnv() {
        try {
            Env env = new Env();
            Assertions.assertNotNull(env.getEnv("DB_URL"));
            Assertions.assertNotNull(env.getEnv("DB_USER"));
            Assertions.assertNotNull(env.getEnv("DB_PASSWORD"));
        } catch (Exception noEnvironmentException) {
            noEnvironmentException.printStackTrace();
        }
    }

    @Test
    public void testConn() {
        Env env = new Env();
        String DB_URL =env.getEnv("DB_URL");
        String USER =env.getEnv("DB_USER");
        String PASS = env.getEnv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("Connection successful!");
            Util.executeSqlScript(conn);
            System.out.println("Script executed successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}