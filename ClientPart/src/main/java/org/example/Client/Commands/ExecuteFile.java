package org.example.Client.Commands;

import org.example.Client.UserInterfaces.cli.CommandBuilder;
import org.example.Client.UserInterfaces.cli.io.Inputer;
import org.example.Client.UserInterfaces.cli.io.Outputer;
import org.example.Client.UserInterfaces.cli.io.Parser;
import org.example.Common.Exceptions.DefaultException;

import java.io.File;
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
            CommandData parsedCommandData = this.parser.parseLine(line);
            try {
                Command command = this.commandBuilder.lazyBuild(parsedCommandData);
                if (command.getClass() == ExecuteFile.class) {
                    throw new DefaultException("Файл не должен содержать команду " + parsedCommandData.getCommandName());
                }
                command.execute();
            } catch (DefaultException e) {
                this.outputer.outputLine(e.getMessage());
                this.outputer.outputLine("Команда " + parsedCommandData.getCommandName() + " пропущена!");
            }
        }
    }

    public void setData(HashMap<String, String> d) {
        if (d.containsKey("filePath")) {
            File file = new File(d.get("filePath"));
            if (file.canRead()) {
                data.put("filePath", d.get("filePath"));
                filePath = d.get("filePath");
            } else {
                data.remove("filePath");
                filePath = null;
            }
        }
    }


    public String getInfo() {
        return "Принимает путь до файла и исполняет команды в нем. Файл не должен содержать команду executeFile," +
                " иначе она будет пропущена" + "\n" + "Вид: /executeFile filePath{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}
