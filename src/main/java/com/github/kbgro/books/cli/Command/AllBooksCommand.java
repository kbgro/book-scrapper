package com.github.kbgro.books.cli.Command;

import com.github.kbgro.books.books.Books;

public class AllBooksCommand implements Command{
    private final Books receiver;

    public AllBooksCommand(Books receiver) {
        this.receiver = receiver;
    }
    @Override
    public String execute() {
        receiver.scrapeE2E();
        return "scrapeE2E Completed";
    }
}
