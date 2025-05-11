package org.example.Interfaces;

import org.example.Bd.BdManager;
import org.example.Commands.Command;
import org.example.Interfaces.cli.CommandBuilder;
import org.example.Interfaces.cli.Exceptions.WrongInput;
import org.example.Interfaces.cli.StandardCommandBuilder;
import org.example.Interfaces.cli.User;
import org.example.Interfaces.cli.io.Inputer;
import org.example.Interfaces.cli.io.Outputer;
import org.example.Interfaces.cli.io.Parser;

import java.util.HashMap;

public class MainCycleController {
    private static boolean run = false;
    private Inputer inputer;
    private Parser parser;
    private Outputer outputer;
    private BdManager bdManager;
    private User user;

    public MainCycleController(Inputer inp, Parser prs, Outputer out, BdManager bd, User u) {
        this.inputer = inp;
        this.parser = prs;
        this.outputer = out;
        this.bdManager = bd;
        this.user = u;
    }

    public void mainCycle() {
        run = true;
        while (run) {
            String newCommandLine = this.inputer.getLine();
            HashMap<String, String> parsedCommand = this.parser.parseLine(newCommandLine);

            CommandBuilder builder = new StandardCommandBuilder(outputer, inputer, parser, bdManager, user);

            try {
                Command command = builder.build(parsedCommand.get("command"), parsedCommand);
                command.execute();
            } catch (WrongInput e) {
                this.outputer.outputLine(e.getMessage());
            }
        }
    }

    public static void start() {
        run = true;
    }

    public static void stop() {
        run = false;
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
