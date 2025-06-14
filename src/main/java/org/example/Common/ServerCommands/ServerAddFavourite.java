package org.example.Common.ServerCommands;

import org.example.Bd.BdManager;
import org.example.Common.Advertisement;
import org.example.Common.User;
import org.example.Exceptions.DefaultException;

import java.io.Serializable;

public class ServerAddFavourite implements ServerCommand, Serializable {
    private BdManager bdManager;
    public DefaultException error;
    public boolean isFavouriteAdded;
    private Integer advertisementId;
    private User user;

    public ServerAddFavourite(Integer aId, User u) {
        advertisementId = aId;
        user = u;
    }

    public void execute() {
        try {
            isFavouriteAdded = bdManager.addFavourite(user, advertisementId);
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