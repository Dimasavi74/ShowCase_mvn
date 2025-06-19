package org.example.Client.Commands;

import java.util.HashMap;

public class CommandData {
    private String commandName = "ErrorName";
    private HashMap<String, String> commandArgs = new HashMap<>();

    public void setCommandName(String cName) {
        this.commandName = cName;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public void setCommandArgs(HashMap<String, String> cArgs) {
        this.commandArgs = cArgs;
    }

    public HashMap<String, String> getCommandArgs() {
        return this.commandArgs;
    }
}
