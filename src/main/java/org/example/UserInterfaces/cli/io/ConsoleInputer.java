package org.example.UserInterfaces.cli.io;

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

