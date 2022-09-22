package com.company;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.Math.abs;

public class GeneratorStata { //во всех функциях сделать upgrade в бд новых значений
    private static ConnectToDB connectToDB;


    GeneratorStata(String namebd,String username,String password){
        connectToDB = new ConnectToDB("jdbc:mysql://localhost:3306/"+password,namebd,username,"stata");
        connectToDB.connect();
        System.out.println("connected to stata table");
    }

    public int getDays() throws SQLException, ParseException {
        Date dateNow = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String dateInString = connectToDB.selectByIdS(0,"date");
        Date dateBefore = formatter.parse(dateInString);
        SimpleDateFormat hdate = new SimpleDateFormat("D");
        int now = Integer.parseInt(hdate.format(dateNow));
        int unnow = Integer.parseInt(hdate.format(dateBefore));
        if (now>unnow) {
            connectToDB.updateById(0,"days","days+"+1);
            return connectToDB.selectById(0,"days");
        }else return connectToDB.selectById(0,"days");
    }

    public int getCash() throws SQLException, ParseException {
        Date dateNow = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String dateInString = connectToDB.selectByIdS(0,"date");
        Date dateBefore = formatter.parse(dateInString);
        SimpleDateFormat hdate = new SimpleDateFormat("H");
        int hnow = Integer.parseInt(hdate.format(dateNow));
        int hunnow = Integer.parseInt(hdate.format(dateBefore));
        int sum = (int) (2000 + Math.random() * 7000);
        if (hnow==hunnow)
        {

            SimpleDateFormat mdate = new SimpleDateFormat("m");
            int mnow = Integer.parseInt(mdate.format(dateNow));
            int munnow = Integer.parseInt(mdate.format(dateBefore));
            if (abs(mnow-munnow)>=10){

                connectToDB.updateById(0,"cash","cash+"+sum);
                return connectToDB.selectById(0,"cash");
            }
            else return connectToDB.selectById(0,"cash");

        }else {
            connectToDB.updateById(0,"cash","cash+"+sum);
            return connectToDB.selectById(0,"cash");
        }
    }

    public int getPlayers() throws SQLException, ParseException {
        Date dateNow = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String dateInString = connectToDB.selectByIdS(0,"date");
        Date dateBefore = formatter.parse(dateInString);
        SimpleDateFormat hdate = new SimpleDateFormat("H");
        int hnow = Integer.parseInt(hdate.format(dateNow));
        int hunnow = Integer.parseInt(hdate.format(dateBefore));
        int sum = (int) (13 + Math.random() * 20);
        if (abs(hnow-hunnow)>=1){
            connectToDB.updateById(0,"players","players+"+sum);
            return connectToDB.selectById(0,"players")+sum;
        }else return connectToDB.selectById(0,"players");
    }

    public boolean closeConnection() throws SQLException {
        connectToDB.closeConnection();
        return !connectToDB.isConnect();
    }

    public void updateDate() throws SQLException, ParseException {
        Date dateNow = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String dateInString = connectToDB.selectByIdS(0,"date");
        Date dateBefore = formatter.parse(dateInString);
        SimpleDateFormat hdate = new SimpleDateFormat("m");
        int hnow = Integer.parseInt(hdate.format(dateNow));
        int hunnow = Integer.parseInt(hdate.format(dateBefore));
        if (abs(hnow-hunnow)>=10) {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = sdf.format(dateNow);

            boolean a = connectToDB.updateById(0, "date", currentTime);
            System.out.println(a);
        }
    }

}
