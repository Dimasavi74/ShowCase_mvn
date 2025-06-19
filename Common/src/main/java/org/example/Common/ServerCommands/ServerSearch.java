package org.example.Common.ServerCommands;

import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;

import java.io.Serializable;
import java.util.HashMap;

public class ServerSearch implements ServerCommand, Serializable {
    private transient BdManager bdManager;
    public DefaultException error = new DefaultException("");
    public HashMap<Integer, String> searchResult;
    private String[] words = {};
    private String[] tags = {};
    private Integer advertisementId = 0;

    public ServerSearch(String[] w, String[] t, Integer aId) {
        words = w;
        tags = t;
        advertisementId = aId;
    }

    public void execute() {
        try {
            searchResult = bdManager.search(words, tags, advertisementId);
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