package org.example.Client.Commands;

import org.example.Client.UserInterfaces.MainCycleController;

import java.util.ArrayList;
import java.util.HashMap;

public class Exit implements Command {
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {};
    MainCycleController mainCycleController;

    public Exit(MainCycleController mcController) {
        this.mainCycleController = mcController;
    }

    public void execute() {
        this.mainCycleController.stop();
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
        return "Завершает работу программы" + "\n" + "Вид: /exit;";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }

}
