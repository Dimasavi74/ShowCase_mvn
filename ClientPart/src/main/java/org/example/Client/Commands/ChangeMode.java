package org.example.Client.Commands;

import org.example.Client.UserInterfaces.cli.StandardCommandBuilderSettings;
import org.example.Client.UserInterfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangeMode implements Command{
    private Outputer outputer;
    private StandardCommandBuilderSettings settings;
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {};


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

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
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
