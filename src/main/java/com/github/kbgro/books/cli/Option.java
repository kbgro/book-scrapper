package com.github.kbgro.books.cli;

import com.github.kbgro.books.cli.Command.Command;

public class Option {
    public String executeCommand(Command command) {
        return command.execute();
    }
}
