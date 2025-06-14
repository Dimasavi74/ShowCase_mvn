package org.example.Client.UserInterfaces.cli;

import org.example.Client.Commands.Command;
import org.example.Client.Commands.CommandData;
import org.example.Client.UserInterfaces.cli.io.Inputer;
import org.example.Client.UserInterfaces.cli.io.Outputer;
import org.example.Client.UserInterfaces.cli.io.Parser;

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
