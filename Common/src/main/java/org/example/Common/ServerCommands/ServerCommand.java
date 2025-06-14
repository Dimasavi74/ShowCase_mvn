package org.example.Common.ServerCommands;

import org.example.Common.Bd.BdManager;

public interface ServerCommand {
    public void execute();
    public String getError();
    public void setError(String errorMessage);
    public void setBdManager(BdManager bdManager);
}
