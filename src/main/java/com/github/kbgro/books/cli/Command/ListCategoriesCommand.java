package com.github.kbgro.books.cli.Command;

import com.github.kbgro.books.books.Books;

public class ListCategoriesCommand implements Command {
    private final Books receiver;

    public ListCategoriesCommand(Books receiver) {
        this.receiver = receiver;
    }
    @Override
    public String execute() {
        return receiver.getCategoriesNames();
    }
}
