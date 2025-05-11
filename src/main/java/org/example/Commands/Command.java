package org.example.Commands;

import java.util.ArrayList;
import java.util.HashMap;

public interface Command {
    public void execute();
    public void setData(HashMap<String, String> d);
    public boolean checkCompleteness();
    public ArrayList<String> getEmptyFields();
    public String getInfo();
//    public void setHandlers(HashMap<"String", Object> handlers);

//    public void setInputer(Inputer inp);
//    public void setParser(Parser prs);
//    public void setOutputer(Outputer out);
}
