package com.github.kbgro.books.utils;

import com.github.kbgro.books.Books;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Env {
    private static final Logger logger = LoggerFactory.getLogger(Books.class);
    private final Map<String, String> envMap = new HashMap<>();

    public Env() {
        loadEnv();
    }

    public void loadEnv() {
        Path pwd = Paths.get(System.getProperty("user.dir"));
        Path envPath = pwd.resolve(".env");
        System.out.println(envPath);
        loadEnv(envPath);
        System.out.println(envMap);
    }

    public void loadEnv(Path filepath) {
        try (BufferedReader br = Files.newBufferedReader(filepath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("=", 2);
                if (values.length != 2)
                    throw new Exception("Env split error occurred!");
                envMap.put(values[0], values[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEnv(String key) {
        String result = envMap.get(key);
        if (result == null)
            result = System.getenv(key);
        return result;
    }
}