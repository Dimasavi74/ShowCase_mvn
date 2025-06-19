package org.example.Common.ServerCommands;

import org.example.Common.Bd.BdManager;

import java.io.Serializable;

public interface ServerCommand extends Serializable {
    public void execute();
    public String getError();
    public void setError(String errorMessage);
    public void setBdManager(BdManager bdManager);
}
