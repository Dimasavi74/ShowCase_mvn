package org.example.Commands;

import org.example.Exceptions.DefaultException;
import org.example.UserInterfaces.cli.CommandBuilder;
import org.example.UserInterfaces.cli.StandardCommandBuilder;
import org.example.UserInterfaces.cli.io.Inputer;
import org.example.UserInterfaces.cli.io.Outputer;
import org.example.UserInterfaces.cli.io.Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ExecuteFile implements Command {
    private Outputer outputer;
    private Inputer inputer;
    private Parser parser;
    private CommandBuilder commandBuilder;
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {"filePath"};
    private String filePath;

    public ExecuteFile(Outputer out, Inputer inp, Parser prs, CommandBuilder cb) {
        this.outputer = out;
        this.inputer = inp;
        this.parser = prs;
        this.commandBuilder = cb;
    }

    public void execute() {
        for (String line: this.inputer.readFile(this.filePath)) {
            HashMap<String, String> parsedCommand = this.parser.parseLine(line);
            try {
                Command command = this.commandBuilder.lazyBuild(parsedCommand.get("command"), parsedCommand);
                if (command.getClass() == ExecuteFile.class) {
                    throw new DefaultException("Файл не должен содержать команду " + parsedCommand.get("command"));
                }
                command.execute();
            } catch (DefaultException e) {
                this.outputer.outputLine(e.getMessage());
                this.outputer.outputLine("Команда " + parsedCommand.get("command") + " пропущена!");
            }
        }
    }

    public void setData(HashMap<String, String> d) {
        if (d.containsKey("filePath")) {
            File file = new File(d.get("filePath"));
            if (file.canRead()) {
                data.put("filePath", d.get("filePath"));
                filePath = d.get("filePath");
            }
        }
    }

    public boolean checkCompleteness() {
        for (String el: necessaryKeys) {
            if (data.get(el) == null) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> getEmptyFields() {
        ArrayList<String> emptyFields = new ArrayList<>();
        for (String el: necessaryKeys) {
            if (data.get(el) == null) {
                emptyFields.add(el);
            }
        }
        return emptyFields;
    }

    public String getInfo() {
        return "Принимает путь до файла и исполняет команды в нем. Файл не должен содержать команду executeFile," +
                " иначе она будет пропущена" + "\n" + "Вид: /executeFile filePath{};";
    }
}
