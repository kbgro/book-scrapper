package com.github.kbgro.books.cli.Command;

import com.github.kbgro.books.books.Books;

public class PageCommand implements Command {
    private final Books receiver;
    private final int pageNumber;

    public PageCommand(Books receiver, int pageNumber) {
        this.receiver = receiver;
        this.pageNumber = pageNumber;
    }

    @Override
    public String execute() {
        receiver.scrapeByPage(pageNumber);
        return "scrapeByPage Completed";
    }
}
