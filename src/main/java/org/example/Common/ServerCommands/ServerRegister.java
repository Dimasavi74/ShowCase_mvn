package org.example.Common.ServerCommands;

import org.example.Bd.BdManager;
import org.example.Exceptions.DefaultException;

import java.io.Serializable;

public class ServerRegister implements ServerCommand, Serializable {
    private BdManager bdManager;
    public DefaultException error;
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