package com.company;

import java.sql.SQLException;

public class User {
    private static ConnectToDB connectToDB;



    private static long id_user;
    private final Game1 game1 = new Game1();
    private final Game2 game2 = new Game2();
    private final Game3 game3 = new Game3();
    private final Game4 game4 = new Game4();

    public User(ConnectToDB connectToDB,long id_user){
        User.connectToDB=connectToDB;
        User.id_user=id_user;
    }
    public static long getId_user() {
        return id_user;
    }
    public Game1 getGame1() {
        return game1;
    }

    public Game2 getGame2() {
        return game2;
    }

    public Game3 getGame3() {
        return game3;
    }

    public Game4 getGame4() {
        return game4;
    }

    public int getIsCallBack() throws SQLException {
        return connectToDB.selectById(id_user,"isCallBack");
    }

    public void setIsCallBack(int isCallBack) throws SQLException {
        connectToDB.updateById(id_user,"isCallBack", String.valueOf(isCallBack));

    }
    public int getIsOutput() throws SQLException {
        return connectToDB.selectById(id_user,"isOutput");
    }

    public void setIsOutput(int isOutput) throws SQLException {
        connectToDB.updateById(id_user,"isOutput", String.valueOf(isOutput));
    }

    public int getCounterGames() throws SQLException {
        return connectToDB.selectById(id_user,"counterGames");
    }

    public void setCounterGames(int counterGames) throws SQLException {
        connectToDB.updateById(id_user,"counterGames", String.valueOf(counterGames));

    }

    public int getIsPayPressed() throws SQLException {
        return connectToDB.selectById(id_user,"isPayPressed");
    }

    public void setIsPayPressed(int isPayPressed) throws SQLException {
        connectToDB.updateById(id_user,"isPayPressed", String.valueOf(isPayPressed));

    }

    public int getIsPlayPressed() throws SQLException {
        return connectToDB.selectById(id_user,"isPlayPressed");
    }

    public void setIsPlayPressed(int isPlayPressed) throws SQLException {
        connectToDB.updateById(id_user,"isPlayPressed", String.valueOf(isPlayPressed));

    }

    public int getStartGame1() throws SQLException {
        return connectToDB.selectById(id_user,"startGame1");
    }

    public void setStartGame1(int startGame1) throws SQLException {
        connectToDB.updateById(id_user,"startGame1", String.valueOf(startGame1));

    }

    public int getStartGame2() throws SQLException {
        return connectToDB.selectById(id_user,"startGame2");
    }

    public void setStartGame2(int startGame2) throws SQLException {
        connectToDB.updateById(id_user,"startGame2", String.valueOf(startGame2));

    }

    public int getStartGame3() throws SQLException {
        return connectToDB.selectById(id_user,"startGame3");
    }

    public void setStartGame3(int startGame3) throws SQLException {
        connectToDB.updateById(id_user,"startGame3", String.valueOf(startGame3));

    }

    public int getStartGame4() throws SQLException {
        return connectToDB.selectById(id_user,"startGame4");
    }

    public void setStartGame4(int startGame4) throws SQLException {
        connectToDB.updateById(id_user,"startGame4", String.valueOf(startGame4));

    }

    public int getSum() throws SQLException {
        return connectToDB.selectById(id_user,"sum");
    }

    public void setSum(int sum) throws SQLException {
        connectToDB.updateById(id_user,"sum", String.valueOf(sum));

    }

    public int getBid() throws SQLException {
        return connectToDB.selectById(id_user,"bid");
    }

    public void setBid(int bid) throws SQLException {
        connectToDB.updateById(id_user,"bid", String.valueOf(bid));

    }
    public int getIsOutputSum() throws SQLException {
        return connectToDB.selectById(id_user,"isOutputSum");
    }

    public void setIsOutputSum(int isOutput) throws SQLException {
        connectToDB.updateById(id_user,"isOutputSum", String.valueOf(isOutput));
    }


}
