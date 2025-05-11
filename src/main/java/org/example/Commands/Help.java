package org.example.Commands;

import org.example.UserInterfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.HashMap;

public class Help implements Command {
    private Outputer outputer;
    private HashMap<String, Command> commands;

    public Help(Outputer outputer, HashMap<String, Command> commands) {
        this.outputer = outputer;
        this.commands = commands;
    }

    public void execute() {
        this.outputer.outputLine("Список доступных команд:\n/" + String.join(";\n/", commands.keySet()) + ";");
    }

    public void setData(HashMap<String, String> data) {
        return;
    }

    public boolean checkCompleteness() {
        return true;
    }

    public ArrayList<String> getEmptyFields() {
        return new ArrayList<String>();
    }

    public String getInfo() {
        return "Возвращает список команд" + "\n" + "Вид: /help;";
    }

    public void setOutputer(Outputer out) { this.outputer = out; }

    public Outputer getOutputer() { return this.outputer; }
}
