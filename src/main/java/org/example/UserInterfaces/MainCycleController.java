package org.example.UserInterfaces;

import org.example.Bd.BdManager;
import org.example.Commands.Command;
import org.example.Commands.CommandData;
import org.example.Exceptions.DefaultException;
import org.example.UserInterfaces.cli.CommandBuilder;
import org.example.UserInterfaces.cli.StandardCommandBuilder;
import org.example.UserInterfaces.cli.StandardCommandBuilderSettings;
import org.example.Common.User;
import org.example.UserInterfaces.cli.io.Inputer;
import org.example.UserInterfaces.cli.io.Outputer;
import org.example.UserInterfaces.cli.io.Parser;

public class MainCycleController {
    private boolean run = false;
    private Inputer inputer;
    private Parser parser;
    private Outputer outputer;
    private BdManager bdManager;
    private User user;
    private StandardCommandBuilderSettings settings;

    public MainCycleController(Inputer inp, Parser prs, Outputer out, BdManager bd, User u, StandardCommandBuilderSettings s) {
        this.inputer = inp;
        this.parser = prs;
        this.outputer = out;
        this.bdManager = bd;
        this.user = u;
        this.settings = s;
    }

    public void mainCycle() {
        run = true;
        while (run) {
            String newCommandLine = this.inputer.getLine();
            CommandData parsedCommandData = this.parser.parseLine(newCommandLine);

            CommandBuilder builder = new StandardCommandBuilder(outputer, inputer, parser, bdManager, user, settings, this);

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
