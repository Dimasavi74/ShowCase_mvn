package org.example.Common.ServerCommands;

import org.example.Bd.BdManager;
import org.example.Common.ServerCommandData.ServerCommandData;

public interface ServerCommand {
    public void execute();
    public String getError();
    public void setError(String errorMessage);
    public void setBdManager(BdManager bdManager);
}
