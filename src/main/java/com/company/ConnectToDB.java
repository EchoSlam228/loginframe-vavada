package com.company;

import java.sql.*;
import java.util.Date;


public class ConnectToDB {
    private final String url;
    private final String username;
    private final String password;
    private final String table;
    public static Connection conn;

    private boolean isConnect;

    public ConnectToDB(String url, String username, String password, String table) {

        this.url = url;
        this.username = username;
        this.password = password;
        this.table = table;
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection to DB has been successfully installed.");
            isConnect = true;
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
            isConnect = false;

        }
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }

    public boolean isConnect() {
        return isConnect;
    }

    public Connection getConn() {
        return conn;
    }

    public int selectById(long id, String col) throws SQLException {
        Statement statement = this.getConn().createStatement();//создаю реакцию на события
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE idtelegram=" + id);
        int result = -1;
        while (resultSet.next()) {
            result = resultSet.getInt(col);
        }
        return result;
    }

    public String selectByIdS(long id, String col) throws SQLException {
        Statement statement = this.getConn().createStatement();//создаю реакцию на события
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE idtelegram=" + id);
        String result = "";
        while (resultSet.next()) {
            result = String.valueOf(resultSet.getString(col));

        }
        return result;
    }

    public String selectByNameS(String name, String col) throws SQLException {
        Statement statement = this.getConn().createStatement();//создаю реакцию на события
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE nametlg= " + "'" + name + "'");
        String result = "";
        while (resultSet.next()) {
            result = String.valueOf(resultSet.getString(col));
        }
        return result.toString();
    }

    public String selectTable(long id, String table) throws SQLException {
        Statement statement = this.getConn().createStatement();//создаю реакцию на события
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE idtelegram=" + id);
        StringBuilder result = new StringBuilder();
        while (resultSet.next()) {
            result.append("\nДата смс : ");
            result.append(resultSet.getString("timedate"));
            result.append(" --->>> ");
            result.append(resultSet.getString("message"));
        }
        return result.toString();
    }

    public boolean createRowById(long id, String namemamont, String nametlg) throws SQLException {
        Statement statement = getConn().createStatement();//создаю реакцию на события
        try {
            String date = String.valueOf(new Date());
            statement.executeUpdate("INSERT " + table +
                    "(idtelegram,bank,firstname,nametlg,reqs,all_games,win_games,lost_games,nums_of_donate,sum_of_donates," +
                    "win_bank,black_list,timedate)" +
                    "VALUES (" + id+",0,'"+namemamont+"','@"+nametlg+"',0,0,0,0,0,0,0,0,'"+date+"')");


            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public void createRowById(long id, String namemamont, String message, String nametlg) throws SQLException {
        Statement statement = getConn().createStatement();//создаю реакцию на события
        try {
            String date = String.valueOf(new Date());
            statement.executeUpdate("INSERT " + table +
                    "(idtelegram,nametlg,message,timedate)" +
                    "VALUES (" + id + ",'@"+nametlg+"','" + message + "\n','" + date + "')");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public boolean updateById(long id, String col, String value) throws SQLException {
        Statement statement = this.getConn().createStatement();//создаю реакцию на события
        try {
            statement.executeUpdate("UPDATE " + table +
                    " SET " + col + " = " + value + " WHERE idtelegram = " + id);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }
}

