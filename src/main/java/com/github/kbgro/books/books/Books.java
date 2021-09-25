package com.github.kbgro.books.books;

import com.github.kbgro.books.repository.BooksRepository;
import org.openqa.selenium.WebDriver;


public class Books extends BookBase {

    public Books(BooksRepository repository, WebDriver driver, int limit) {
        super(repository, driver);
        this.limit = limit;
    }
}
