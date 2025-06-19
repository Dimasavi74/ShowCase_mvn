package org.example.Client.Commands;

import org.example.Client.UserInterfaces.cli.io.Outputer;

import java.util.HashMap;

public class GetInfo implements Command {
    private Outputer outputer;
    private final HashMap<String, String> data = new HashMap<>();
    private final HashMap<String, Command> commands;
    final String[] necessaryKeys = {"commandName"};
    private String commandName;

    public GetInfo(Outputer outputer, HashMap<String, Command> commands) {
        this.outputer = outputer;
        this.commands = commands;
    }

    public void execute() {
        this.outputer.outputLine(commands.get(commandName).getInfo());
    }

    public void setData(HashMap<String, String> d) {
        if (commands.containsKey(d.get("commandName"))) {
            data.put("commandName", d.get("commandName"));
            commandName = d.get("commandName");
        }
    }

    public String getInfo() {
        return "Возвращает информацию о команде" + "\n" + "Вид: /getInfo commandName{};";
    }

    public void setOutputer(Outputer out) { this.outputer = out; }

    public Outputer getOutputer() { return this.outputer; }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}
