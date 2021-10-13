package com.github.kbgro.books.books;

import com.github.kbgro.books.repository.BooksRepository;


public class Books extends BookBase {

    public Books(BooksRepository repository, int limit) {
        super(repository);
        this.limit = limit;
    }
}
