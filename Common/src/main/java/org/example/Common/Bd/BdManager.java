package org.example.Common.Bd;

import org.example.Common.Advertisement;
import org.example.Common.User;

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
    public boolean addFavourite(User user, int advertisementId);
    public boolean removeFavourite(User user, int advertisementId);
    public HashMap<Integer, String> userFavourites(User user);
}
