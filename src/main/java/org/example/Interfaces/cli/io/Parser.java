package org.example.Interfaces.cli.io;

import java.util.HashMap;

public interface Parser {
    public HashMap<String, String> parseLine(String line);
}
