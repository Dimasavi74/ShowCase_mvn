package org.example.Common.ServerCommands;

import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.User;

import java.io.Serializable;

public class ServerDeleteAdvertisement implements ServerCommand, Serializable {
    private BdManager bdManager;
    public DefaultException error;
    public boolean isAdvertisementDeleted;
    private Integer advertisementId;
    User user;

    public ServerDeleteAdvertisement(Integer aId, User u) {
        advertisementId = aId;
        user = u;
    }

    public void execute() {
        try {
            isAdvertisementDeleted = bdManager.deleteAdvertisement(advertisementId, user);
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