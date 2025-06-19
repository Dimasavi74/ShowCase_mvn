package org.example.Client.UserInterfaces.cli.io;

import org.example.Client.Commands.CommandData;
import org.example.Common.Exceptions.DefaultException;

import java.util.HashMap;

public class StandardParser implements Parser {
    public CommandData parseLine(String line) {
//      Парсинг строки формата /commandName commandArg1{data} commandArg2{data} ... ;
        CommandData commandData = new CommandData();

        if (line.charAt(line.length() - 1) != ';') {
            throw new DefaultException("");
        }

        // Получение команды
        if (line.charAt(0) == '/') {
            StringBuilder commandName = new StringBuilder();
            for (int i = 1; i < line.length(); i++) {
                char c = line.charAt(i);
                if ((c != ' ') & (c != ';')) {
                    commandName.append(c);
                } else {
                    line = line.substring(i);
                    break;
                }
            }
            commandData.setCommandName(String.valueOf(commandName));
        }

        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '{') {
                String[] all_keys = line.substring(0, i).split(" ");
                String keyName = all_keys[all_keys.length - 1].strip();
                StringBuilder value = new StringBuilder();
                i++;
                c = line.charAt(i);
                while ((c != '}') & (c != ';')) {
                    value.append(c);
                    i++;
                    c = line.charAt(i);
                }
                map.put(keyName, String.valueOf(value));
                line = line.substring(i + 1);
                i = 0;
            }
        }
        commandData.setCommandArgs(map);
        return commandData;
    }
}
