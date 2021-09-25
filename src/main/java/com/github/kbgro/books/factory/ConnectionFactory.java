package com.github.kbgro.books.factory;

import com.github.kbgro.books.App;
import com.github.kbgro.books.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static Connection getConnection(String dbUrl, String dbUser, String dbPassword) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            logger.info("Creating Table App Start.");

            Util.executeSqlScript(conn);

            logger.info("Clearing Table...");

            //noinspection SqlWithoutWhere
            PreparedStatement statement = conn.prepareStatement("DELETE FROM books.books");
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
