package org.example.UserInterfaces.cli.io;

import org.example.Commands.CommandData;

import java.util.HashMap;

public interface Parser {
    public CommandData parseLine(String line);
}
