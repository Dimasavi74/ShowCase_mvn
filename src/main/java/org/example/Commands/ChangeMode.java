package org.example.Commands;

import org.example.UserInterfaces.cli.StandardCommandBuilder;
import org.example.UserInterfaces.cli.StandardCommandBuilderSettings;
import org.example.UserInterfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangeMode implements Command{
    private Outputer outputer;
    private StandardCommandBuilderSettings settings;


    public ChangeMode(Outputer out, StandardCommandBuilderSettings s) {
        this.outputer = out;
        this.settings = s;
    }

    public void execute() {
        if (settings.inputMode.equals("line")) {
            settings.inputMode = "standard";
        } else {
            settings.inputMode = "line";
        }
        outputer.outputLine("Режим ввода изменен на " + settings.inputMode);
    }

    public void setData(HashMap<String, String> d) {
        return;
    }

    public boolean checkCompleteness() {
        return true;
    }

    public ArrayList<String> getEmptyFields() {
        return null;
    }

    public String getInfo() {
        return "Изменяет режим ввода" + "\n" + "Вид: /changeMode;";
    }
}
