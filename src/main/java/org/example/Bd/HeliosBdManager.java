package org.example.Bd;

import org.example.Commands.Register;
import org.example.Interfaces.cli.Advertisement;
import org.example.Interfaces.cli.Exceptions.DefaultException;
import org.example.Interfaces.cli.User;
import org.postgresql.util.PSQLException;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

//import static java.sql.DriverManager.getConnection;

import java.sql.DriverManager;

public class HeliosBdManager implements BdManager {
    String url;
    Properties info;
    Connection connection;

    public HeliosBdManager() throws IOException {
//        -L <local-address>:<local-port>:<remote-address>:<remote-port>
//        ssh -L 5432:pg:5432 -p 2222 s467318@se.ifmo.ru

        url = "jdbc:postgresql://localhost:5432/studs";
        info = new Properties();
        File file = new File("src/main/java/org/example/Bd/db.cfg");
        info.load(new FileInputStream(file));
        connect();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, info);
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
            String query;
            Array sqlArray = connection.createArrayOf("TEXT", tags);
            ResultSet result;
            query = "SELECT * FROM (SELECT AdvertisementId, array_agg(Tag) FROM AdvertisementTags " +
                        "GROUP BY AdvertisementId HAVING array_agg(Tag) @> ARRAY[?]::text[]) AS Tags " +
                        "INNER JOIN Advertisement ON Tags.AdvertisementId = Advertisement.AdvertisementId;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setArray(1, sqlArray);
            result = ps.executeQuery();

            boolean flag = false;
            HashMap<Integer, String> resultMap = new HashMap<>();
            if (!result.next()) {
                throw new DefaultException("KeyDoesNotExistError");
            } else {
                do {
                    Integer id = result.getInt(1);
                    String title = result.getString(5);
                    String description = result.getString(6);
                    String price = result.getString(7);
                    String contacts = result.getString(8);

                    if (advertisementId != 0 && !(id.equals(advertisementId))) {
                        continue;
                    }

                    for (String word: words) {
                        if (!(title + description + price + contacts).contains(word)) {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        resultMap.put(id, title);
                    }
                } while (result.next());
            }
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
//                return false;
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
//            throw new RuntimeException(e);
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
//            throw new DefaultException("ServerError");
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
//            throw new RuntimeException(e);
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
//            throw new RuntimeException(e);
            throw new DefaultException("ServerError");
        }
    }
}
