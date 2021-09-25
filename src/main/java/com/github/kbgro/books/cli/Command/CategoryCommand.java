package com.github.kbgro.books.cli.Command;


import com.github.kbgro.books.books.Books;

public class CategoryCommand implements Command {
    private final Books receiver;
    String category;

    public CategoryCommand(Books receiver, String category) {
        this.receiver = receiver;
        this.category = category;
    }

    @Override
    public String execute() {
        try {
            receiver.scrapeByCategory(category);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }
}
