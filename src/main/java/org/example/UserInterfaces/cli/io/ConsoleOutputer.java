package org.example.UserInterfaces.cli.io;

public class ConsoleOutputer implements Outputer {
    public void outputLine(String line) {
        System.out.println(line);
    }

    public void outputHalfLine(String line) {System.out.print(line);}
}
