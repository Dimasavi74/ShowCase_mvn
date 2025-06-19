package org.example.Common.ServerCommands;

import org.example.Common.Bd.BdManager;
import org.postgresql.util.ServerErrorMessage;

public class ServerEmptyCommand implements ServerCommand {
    private String errorMessage;

    public void execute() {

    }

    public String getError() {
        return errorMessage;
    }

    public void setError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setBdManager(BdManager bdManager) {

    }
}
