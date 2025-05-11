package org.example.Commands;

import org.example.UserInterfaces.MainCycleController;

import java.util.ArrayList;
import java.util.HashMap;

public class Exit implements Command {

    public void execute() {
        MainCycleController.stop();
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

}
