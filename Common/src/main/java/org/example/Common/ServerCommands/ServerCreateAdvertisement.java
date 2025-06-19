package org.example.Common.ServerCommands;

import org.example.Common.Advertisement;
import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.User;

import java.io.Serializable;

public class ServerCreateAdvertisement implements ServerCommand, Serializable {
    private transient BdManager bdManager;
    public DefaultException error;
    public boolean isAdvertisementCreated;
    public Integer advertisementId;
    private Advertisement advertisement;
    private User user;

    public ServerCreateAdvertisement(Advertisement ad, User u) {
        advertisement = ad;
        user = u;
    }

    public void execute() {
        try {
            int id = bdManager.createAdvertisement(advertisement, user);
            isAdvertisementCreated = id != 0;
            advertisementId = id;
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