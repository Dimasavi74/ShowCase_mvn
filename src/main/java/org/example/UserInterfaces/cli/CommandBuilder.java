package org.example.UserInterfaces.cli;

import org.example.Commands.Command;
import org.example.Commands.CommandData;
import org.example.UserInterfaces.cli.io.Inputer;
import org.example.UserInterfaces.cli.io.Outputer;
import org.example.UserInterfaces.cli.io.Parser;

import java.util.HashMap;

public interface CommandBuilder {
    public Command build(CommandData commandData);
    public Command lazyBuild(CommandData commandData);
    public Command getCommandObject(String commandName);

    public void setInputer(Inputer inp);
    public void setParser(Parser prs);
    public void setOutputer(Outputer out);
    public Inputer getInputer();
    public Parser getParser();
    public Outputer getOutputer();
}
