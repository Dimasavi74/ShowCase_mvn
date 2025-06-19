package org.example.Client.Commands;

import java.util.ArrayList;
import java.util.HashMap;

public interface Command {
    public void execute();
    public void setData(HashMap<String, String> d);

    public default boolean checkCompleteness() {
        for (String el: getNesessaryKeys()) {
            if (getData().get(el) == null) {
                return false;
            }
        }
        return true;
    }

    public default ArrayList<String> getEmptyFields() {
        ArrayList<String> emptyFields = new ArrayList<>();
        for (String el: getNesessaryKeys()) {
            if (getData().get(el) == null) {
                emptyFields.add(el);
            }
        }
        return emptyFields;
    }

    public String getInfo();
    public String[] getNesessaryKeys();
    public HashMap<String, String> getData();
//    public void setHandlers(HashMap<"String", Object> handlers);

//    public void setInputer(Inputer inp);
//    public void setParser(Parser prs);
//    public void setOutputer(Outputer out);
}
