package org.example.UserInterfaces.cli.io;

import java.util.HashMap;

public interface Parser {
    public HashMap<String, String> parseLine(String line);
}
