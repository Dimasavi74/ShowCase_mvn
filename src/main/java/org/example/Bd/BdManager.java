package org.example.Bd;

import org.example.Interfaces.cli.Advertisement;
import org.example.Interfaces.cli.User;

import java.util.HashMap;

public interface BdManager {
    public boolean register(String nickname, String mailAddress, String password);
    public boolean login(String nickname, String mailAddress, String password);
    public boolean deleteUser(String nickname, String mailAddress, String password);
    public int createAdvertisement(Advertisement advertisement, User user);
    public boolean deleteAdvertisement(int advertisementId, User user);
    public HashMap<Integer, String> userAdvertisements(User user);
    public Advertisement showAdvertisement(int advertisementId);
    public HashMap<Integer, String> search(String[] words, String[] tags, Integer advertisementId);
}
