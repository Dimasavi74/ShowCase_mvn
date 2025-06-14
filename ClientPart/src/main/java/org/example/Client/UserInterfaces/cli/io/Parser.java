package org.example.Client.UserInterfaces.cli.io;

import org.example.Client.Commands.CommandData;

public interface Parser {
    public CommandData parseLine(String line);
}
