package org.example.Common.Bd;

import org.apache.commons.lang3.math.NumberUtils;
import org.example.Common.Advertisement;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;

public class HeliosBdManager implements BdManager {
    String url;
    Properties info;
    Connection connection;
    String user;
    String password;

    public HeliosBdManager(String usr, String pwd) {
//        -L <local-address>:<local-port>:<remote-address>:<remote-port>
//        ssh -L 5432:pg:5432 -p 2222 s467318@se.ifmo.ru



        url = "jdbc:postgresql://localhost:5432/studs";
        user = usr;
        password = pwd;
//        info = new Properties();
//        File file = new File("src/main/java/org/example/Bd/db.cfg");

//        File file = new File("C:\\Users\\Dimasavi74\\IdeaProjects\\ShowCase_mvn\\src\\main\\java\\org\\example\\Bd\\db.cfg");
//        File file = new File("org/example/Common/Bd/db.cfg");
//        info.load(new FileInputStream(file));

        connect();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e) {
            if (e.getSQLState().equals("08001")) {
                throw new DefaultException("ConnectionIsClosedError");
            } else {
                throw new DefaultException("ServerError");
            }
        }
    }

    public HashMap<Integer, String> search(String[] words, String[] tags, Integer advertisementId) {
        try {
            if (connection.isClosed()) {connect();}

            System.out.println(Arrays.toString(words));

            String query;
            ResultSet result;

            query = "TRUNCATE ChangedTable;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate();
            query = "TRUNCATE ChangedTable2;";
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
            query = "INSERT INTO ChangedTable SELECT * FROM WordToAdvertisement;";
            ps = connection.prepareStatement(query);
            ps.executeUpdate();

            for (int i = 0; i < words.length; i++) {
                query = "INSERT INTO ChangedTable2 SELECT * FROM ChangedTable WHERE AdvertisementId IN (SELECT AdvertisementId FROM ChangedTable WHERE ChangedTable.Word = ?);";
                ps = connection.prepareStatement(query);
                ps.setString(1, words[i]);
                ps.executeUpdate();
                query = "TRUNCATE ChangedTable;";
                ps = connection.prepareStatement(query);
                ps.executeUpdate();
                query = "INSERT INTO ChangedTable SELECT * FROM ChangedTable2;";
                ps = connection.prepareStatement(query);
                ps.executeUpdate();
                query = "TRUNCATE ChangedTable2;";
                ps = connection.prepareStatement(query);
                ps.executeUpdate();
            }

            Array sqlArray = connection.createArrayOf("TEXT", tags);
            query = "SELECT * FROM (SELECT AdvertisementId, array_agg(Tag) FROM AdvertisementTags " +
                        "GROUP BY AdvertisementId HAVING array_agg(Tag) @> ARRAY[?]::text[]) AS Tags " +
                        "INNER JOIN Advertisement ON Tags.AdvertisementId = Advertisement.AdvertisementId " +
                    "INNER JOIN ChangedTable ON Tags.AdvertisementId = ChangedTable.AdvertisementId;";
            ps = connection.prepareStatement(query);
            ps.setArray(1, sqlArray);
            result = ps.executeQuery();

            HashMap<Integer, String> resultMap = new HashMap<>();
            System.out.println(resultMap);
            while (result.next()) {
                    Integer id = result.getInt(1);
                    String title = result.getString(5);
//                    String description = result.getString(6);
//                    String price = result.getString(7);
//                    String contacts = result.getString(8);

                    if (advertisementId != 0 && !(id.equals(advertisementId))) {
                        continue;
                    }
                    resultMap.put(id, title);
                }
            System.out.println(resultMap);
            return resultMap;

        } catch(SQLException e){
            throw new DefaultException("ServerError");
        }
    }


    public boolean register(String nickname, String mailAddress, String password) {
        try {
            if (connection.isClosed()) {connect();}
            String query = "INSERT INTO Person VALUES (DEFAULT, ?, ?, ?, FALSE);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, nickname);
            ps.setString(2, mailAddress);
            ps.setString(3, password);
            ps.execute();
            return true;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new DefaultException("KeyAlreadyExistsError");
            } else {
                throw new DefaultException("ServerError");
            }
        }
    }

    public boolean login(String nickname, String mailAddress, String password) {
        try {
            if (connection.isClosed()) {connect();}
            String query = "SELECT EXISTS(SELECT * FROM Person WHERE Nickname = ? AND MailAddress = ? AND Password = ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, nickname);
            ps.setString(2, mailAddress);
            ps.setString(3, password);
            ResultSet results = ps.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            throw new DefaultException("ServerError");
        }
    }

    public boolean deleteUser(String nickname, String mailAddress, String password) {
        try {
            if (connection.isClosed()) {connect();}
            String query = "DELETE FROM Person WHERE Nickname = ? AND MailAddress = ? AND Password = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, nickname);
            ps.setString(2, mailAddress);
            ps.setString(3, password);
            boolean result = (ps.executeUpdate() > 0);
            if (!result) {
                throw new DefaultException("KeyDoesNotExistError");
            }
            return true;
        } catch (SQLException e) {
            throw new DefaultException("ServerError");
        }
    }

    private Integer getUserId(User user) {
        try {
            if (connection.isClosed()) {connect();}
            String query = "(SELECT * FROM Person WHERE Nickname = ? AND MailAddress = ? AND Password = ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user.nickname);
            ps.setString(2, user.mailAddress);
            ps.setString(3, user.password);
            ResultSet results = ps.executeQuery();
            results.next();
            return results.getInt(1);
        } catch (SQLException e) {
            throw new DefaultException("ServerError");
        }
    }


    public int createAdvertisement(Advertisement advertisement, User user) {
        try {
            if (connection.isClosed()) {connect();}
            if (!login(user.nickname, user.mailAddress, user.password)) {throw new DefaultException("UserDoesNotExist");}
            int userId = getUserId(user);
            String query = "INSERT INTO Advertisement VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING AdvertisementId;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setString(2, advertisement.title);
            ps.setString(3, advertisement.description);
            ps.setInt(4, advertisement.price);
            ps.setString(5, advertisement.contacts);
            ResultSet results = ps.executeQuery();
            results.next();
            int advertisementId = results.getInt(1);

            query = "INSERT INTO WordToAdvertisement VALUES (?, ?);";
            ps = connection.prepareStatement(query);
            String[] adWords = String.join(" ", Arrays.asList((advertisement.title + " " + advertisement.description).split("\n"))).split(" ");
            for (String el: adWords) {
                ps.setString(1, el.toLowerCase());
                ps.setInt(2, advertisementId);
                try {
                    ps.execute();
                } catch (SQLException e) {
                    if (e.getSQLState().equals("23505")) {
                    } else {
                        throw new DefaultException("ServerError");
                    }
                }
            }

            query = "INSERT INTO AdvertisementTags VALUES (?, ?);";
            ps = connection.prepareStatement(query);
            for (String tag: advertisement.tags) {
                ps.setInt(1, advertisementId);
                ps.setString(2, tag);
                try {
                    ps.execute();
                } catch (SQLException e) {
                    if (e.getSQLState().equals("23505")) {
                        throw new DefaultException("TagAlreadyExistsError");
                    } else {
                        throw new DefaultException("ServerError");
                    }
                }
            }
            return advertisementId;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new DefaultException("AdvertisementAlreadyExistsError");
            } else {
                throw new DefaultException("ServerError");
            }
        }
    }

    public boolean deleteAdvertisement(int advertisementId, User user) {
        try {
            if (connection.isClosed()) {connect();}
            if (!login(user.nickname, user.mailAddress, user.password)) {throw new DefaultException("UserDoesNotExist");}
            int userId = getUserId(user);
            String query = "DELETE FROM Advertisement WHERE AdvertisementId = ? AND PersonId = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, advertisementId);
            ps.setInt(2, userId);
            boolean result = (ps.executeUpdate() > 0);
            if (!result) {
                throw new DefaultException("KeyDoesNotExistError");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<Integer, String> userAdvertisements(User user) {
        try {
            if (connection.isClosed()) {connect();}
            if (!login(user.nickname, user.mailAddress, user.password)) {throw new DefaultException("UserDoesNotExist");}
            int userId = getUserId(user);
            String query = "SELECT AdvertisementId, Title FROM Advertisement WHERE PersonId = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet result = ps.executeQuery();
            HashMap<Integer, String> resultMap = new HashMap<>();
            while(result.next()){
                int advertisementId = result.getInt(1);
                String title = result.getString(2);
                resultMap.put(advertisementId, title);
            }
            return resultMap;
        } catch (SQLException e) {
            throw new DefaultException("ServerError");
        }
    }

    public Advertisement showAdvertisement(int advertisementId) {
        try {
            if (connection.isClosed()) {connect();}
            String query = "SELECT * FROM Advertisement WHERE AdvertisementId = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, advertisementId);
            ResultSet result = ps.executeQuery();
            if (!result.next()) {throw new DefaultException("KeyDoesNotExistError");}
            String title = result.getString(3);
            String description = result.getString(4);
            int price = NumberUtils.toInt(result.getString(5).split(",")[0]);
            String contacts = result.getString(6);

            query = "SELECT * FROM AdvertisementTags WHERE AdvertisementId = ?;";
            ps = connection.prepareStatement(query);
            ps.setInt(1, advertisementId);
            result = ps.executeQuery();
            LinkedList<String> tags = new LinkedList<String>();
            while (result.next()) {
                String tag = result.getString(2);
                tags.add(tag);
            }
            return new Advertisement(title, description, price, contacts, tags.toArray(new String[0]));

        } catch (SQLException e) {
            throw new DefaultException("ServerError");
        }
    }

    public boolean addFavourite(User user, int advertisementId) {
        try {
            if (connection.isClosed()) {
                connect();
            }
            if (!login(user.nickname, user.mailAddress, user.password)) {
                throw new DefaultException("UserDoesNotExist");
            }
            int userId = getUserId(user);
            String query = "INSERT INTO PersonFavourite VALUES (?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, advertisementId);
            ps.execute();
            return true;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new DefaultException("KeyAlreadyExistsError");
            } else {
                throw new DefaultException("ServerError");
            }
        }
    }

    public boolean removeFavourite(User user, int advertisementId) {
        try {
            if (connection.isClosed()) {connect();}
            if (!login(user.nickname, user.mailAddress, user.password)) {throw new DefaultException("UserDoesNotExist");}
            int userId = getUserId(user);
            String query = "DELETE FROM PersonFavourite WHERE AdvertisementId = ? AND PersonId = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, advertisementId);
            ps.setInt(2, userId);
            boolean result = (ps.executeUpdate() > 0);
            if (!result) {
                throw new DefaultException("KeyDoesNotExistError");
            }
            return true;
        } catch (SQLException e) {
            throw new DefaultException("ServerError");
        }
    }

    public HashMap<Integer, String> userFavourites(User user) {
        try {
            if (connection.isClosed()) {connect();}
            if (!login(user.nickname, user.mailAddress, user.password)) {throw new DefaultException("UserDoesNotExist");}
            int userId = getUserId(user);
            String query = "SELECT * FROM (SELECT AdvertisementId FROM PersonFavourite WHERE PersonId = ?) AS PersonFavourite INNER JOIN Advertisement ON PersonFavourite.AdvertisementId = Advertisement.AdvertisementId;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet result = ps.executeQuery();
            HashMap<Integer, String> resultMap = new HashMap<>();
            while(result.next()){
                int advertisementId = result.getInt(1);
                String title = result.getString(4);
                resultMap.put(advertisementId, title);
            }
            return resultMap;
        } catch (SQLException e) {
            throw new DefaultException("ServerError");
        }
    }
}
