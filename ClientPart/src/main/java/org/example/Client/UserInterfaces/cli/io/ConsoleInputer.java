package org.example.Client.UserInterfaces.cli.io;

import org.example.Common.Exceptions.DefaultException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ConsoleInputer implements Inputer {
    private String endSymbol = ";";

    public String getLine() {
        String line = getAbnormalLine();
        line = convertToNormalLine(line);
        return line;
    }

    public String getAbnormalLine() {
        // Returns line if contains end symbol (can be not only one and not in the end)
        Scanner console = new Scanner(System.in);
        String line = console.nextLine();
        while (!line.contains(this.endSymbol)) {
            line += console.nextLine();
        }
        return line;
    }

    public String[] readFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            String content = Files.readString(path);
            if (content.contains(";")) {
                String[] lines = content.substring(0, content.lastIndexOf(";")).split(";");
                String[] finalLines = new String[lines.length];
                for (int i = 0; i != lines.length; i++) {finalLines[i] = lines[i] + ";";}
                return finalLines;
            } else {
                return new String[]{};
            }
        } catch (IOException e) {
            throw new DefaultException("Файла по данному пути не существует, либо он нечитаем. Путь: " + e.getMessage());
        }
    }

    public String convertToNormalLine(String line) {
        // Returns line that contains only one end symbol in the end
        char[] lineArray = line.toCharArray();
        String normalLine = "";
        for (char el: lineArray) {
            normalLine += el;
            if (el == this.endSymbol.charAt(0)) {
                break;
            }
        }
        return normalLine;
    }

    public void setEndSymbol(String end) {
        this.endSymbol = end;
    }

    public String getEndSymbol() {
        return this.endSymbol;
    }
}

