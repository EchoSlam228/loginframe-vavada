package com.company;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckConnBD {
    private Bot bot;
    private String BOT_TOKEN;
    private String BOT_NAME;
    private final String username;
    private final String nameDB;
    private final String password;
    private final String maintable;
    private final String arhmesstable;
    private String publickey;
    private String secretkey;
    private com.company.ConnectToDB connectToDB;

    CheckConnBD(String BOT_TOKEN, String BOT_NAME, String username,
                String password,String nameDB, String maintable,
                String arhmesstable, String publickey, String secretkey) {
        this.BOT_TOKEN = BOT_TOKEN;
        this.BOT_NAME = BOT_NAME;
        this.username = username;
        this.nameDB = nameDB;
        this.password = password;
        this.maintable = maintable;
        this.arhmesstable = arhmesstable;
        this.publickey = publickey;
        this.secretkey = secretkey;
        bot = new Bot(BOT_TOKEN, BOT_NAME, username, nameDB, password, maintable, arhmesstable, publickey, secretkey);
        connectToDB = new ConnectToDB("jdbc:mysql://localhost:3306/" + nameDB, username, password, maintable);
    }

    public ConnectToDB getConnectToDB() {
        return connectToDB;
    }

    public String getNameBot() {
        return BOT_NAME;
    }
    public String getTokenBot() {
        return BOT_TOKEN;
    }
    public String getNameDB() {
        return nameDB;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getMaintable() {
        return maintable;
    }
    public String getArhmesstable() {
        return arhmesstable;
    }
    public String getPublickey() {
        return publickey;
    }
    public String getSecretkey() {
        return secretkey;
    }

    public boolean checkConnect() throws SQLException {
        try {

            connectToDB.connect();
            if (connectToDB.isConnect()) System.out.println("DB ok");
            connectToDB.closeConnection();
            return connectToDB.isConnect();
        } catch (Exception e) {

            return false;
        }

    }

//    public boolean checkTables() throws SQLException {
//        int id1 = -1;
//        int id2 = -1;
//        try {
//            connectToDB.connect();
//            Statement statement = connectToDB.getConn().createStatement();//создаю реакцию на события
//            ResultSet resultSet1 = statement.executeQuery("SELECT * FROM " + maintable + " WHERE id=1");
//            while (resultSet1.next()) {
//                id1 = resultSet1.getInt("id");
//            }
//            ResultSet resultSet2 = statement.executeQuery("SELECT * FROM " + arhmesstable + " WHERE id=1");
//            while (resultSet2.next()) {
//                id2 = resultSet2.getInt("id");
//            }
//            if (id2 != -1 && id1 != -1) {
//                System.out.println("tables ok");
//                connectToDB.closeConnection();
//                return true;
//            } else {
//                connectToDB.closeConnection();
//                return false;
//            }
//        } catch (Exception e) {
//            connectToDB.closeConnection();
//            return false;
//        }
//
//    }

    public boolean checkBot() throws Exception {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
            System.out.println("bot ok");
            return true;
        } catch (Exception e) {
            return false;
        }


    }


}
