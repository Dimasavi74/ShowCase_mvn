package org.example.Interfaces.cli;

import org.example.Commands.Command;
import org.example.Interfaces.cli.io.Inputer;
import org.example.Interfaces.cli.io.Outputer;
import org.example.Interfaces.cli.io.Parser;

import java.util.HashMap;

public interface CommandBuilder {
    public Command build(String commandName, HashMap<String, String> commandArgs);
    public Command getCommandObject(String commandName);

    public void setInputer(Inputer inp);
    public void setParser(Parser prs);
    public void setOutputer(Outputer out);
    public Inputer getInputer();
    public Parser getParser();
    public Outputer getOutputer();
}
