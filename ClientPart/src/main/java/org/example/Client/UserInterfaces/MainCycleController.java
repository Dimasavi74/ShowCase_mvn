package org.example.Client.UserInterfaces;


import org.example.Client.Commands.Command;
import org.example.Client.Commands.CommandData;
import org.example.Client.UserInterfaces.cli.CommandBuilder;
import org.example.Client.UserInterfaces.cli.StandardCommandBuilder;
import org.example.Client.UserInterfaces.cli.StandardCommandBuilderSettings;
import org.example.Client.UserInterfaces.cli.io.Communicator;
import org.example.Client.UserInterfaces.cli.io.Inputer;
import org.example.Client.UserInterfaces.cli.io.Outputer;
import org.example.Client.UserInterfaces.cli.io.Parser;
import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.User;

public class MainCycleController {
    private boolean run = false;
    private Inputer inputer;
    private Parser parser;
    private Outputer outputer;
    private BdManager bdManager;
    private Communicator communicator;
    private User user;
    private StandardCommandBuilderSettings settings;

    public MainCycleController(Inputer inp, Parser prs, Outputer out, BdManager bd, Communicator communicator, User u, StandardCommandBuilderSettings s) {
        this.inputer = inp;
        this.parser = prs;
        this.outputer = out;
        this.bdManager = bd;
        this.communicator = communicator;
        this.user = u;
        this.settings = s;
    }

    public void mainCycle() {
        run = true;
        while (run) {
            String newCommandLine = this.inputer.getLine();
            CommandData parsedCommandData = this.parser.parseLine(newCommandLine);

            CommandBuilder builder = new StandardCommandBuilder(outputer, inputer, parser, bdManager, communicator, user, settings, this);

            try {
                Command command = builder.build(parsedCommandData);
                command.execute();
            } catch (DefaultException e) {
                this.outputer.outputLine(e.getMessage());
            }
        }
    }

    public void start() {
        this.run = true;
    }

    public void stop() {
        this.run = false;
    }

    public void setInputer(Inputer inp) {
        this.inputer = inp;
    }

    public void setParser(Parser prs) {
        this.parser = prs;
    }

    public void setOutputer(Outputer out) {
        this.outputer = out;
    }


}
