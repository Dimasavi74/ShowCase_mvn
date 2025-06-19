package org.example.Common.ServerCommands;

import org.example.Common.Advertisement;
import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;

import java.io.Serializable;

public class ServerShowAdvertisement implements ServerCommand, Serializable {
    private transient BdManager bdManager;
    public DefaultException error = new DefaultException("");
    public Advertisement advertisement;
    private Integer advertisementId;

    public ServerShowAdvertisement(Integer aId) {
        advertisementId = aId;
    }

    public void execute() {
        try {
            advertisement = bdManager.showAdvertisement(advertisementId);
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