package org.example.Common.ServerCommands;

import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.User;

import java.io.Serializable;
import java.util.HashMap;

public class ServerMyAdvertisements implements ServerCommand, Serializable {
    private transient BdManager bdManager;
    public DefaultException error;
    public HashMap<Integer, String> userAdvertisements;
    private User user;

    public ServerMyAdvertisements(User u) {
        user = u;
    }

    public void execute() {
        try {
            userAdvertisements = bdManager.userAdvertisements(user);
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