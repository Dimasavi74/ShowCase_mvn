package org.example.Common.ServerCommands;

import org.example.Bd.BdManager;
import org.example.Common.User;
import org.example.Exceptions.DefaultException;

import java.io.Serializable;
import java.util.HashMap;

public class ServerMyFavourites implements ServerCommand, Serializable {
    private BdManager bdManager;
    public DefaultException error;
    public HashMap<Integer, String> userFavourites;
    private User user;

    public ServerMyFavourites(User u) {
        user = u;
    }

    public void execute() {
        try {
            userFavourites = bdManager.userFavourites(user);
        } catch (DefaultException e) {
            error = e;
        }
    }


    public String getError() {
        return error.getMessage();
    }

    public void setError(String errorMessage) {
        error = new DefaultException(errorMessage);
    }

    public void setBdManager(BdManager bdMan) {
        this.bdManager = bdMan;
    }
}