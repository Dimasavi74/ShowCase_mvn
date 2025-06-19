package org.example.Common.ServerCommands;

import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;

import java.io.Serializable;

public class ServerRegister implements ServerCommand, Serializable {
    private transient BdManager bdManager;
    public DefaultException error = new DefaultException("");
    public boolean isRegistered;
    private String nickname;
    private String mailAddress;
    private String password;

    public ServerRegister(String nick, String mail, String pass) {
        nickname = nick;
        mailAddress = mail;
        password = pass;
    }

    public void execute() {
        try {
            isRegistered = bdManager.register(nickname, mailAddress, password);
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