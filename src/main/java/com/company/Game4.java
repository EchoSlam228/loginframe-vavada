package com.company;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;

import static com.company.Bot.*;

public class Game4 implements Game{

    private static long id_user;
    private static int counterGames = -1;//количество сыгранных партий юзера
    @Override
    public void setCounterGames(int counterGames) {
        Game4.counterGames = counterGames;
    }

    @Override
    public int getCounterGames() {
        return counterGames;
    }

    @Override
    public void startgame(Bot bot, long id_user) throws TelegramApiException {
        Game4.id_user = id_user;
        InlineButton btn = new InlineButton();
        btn.One("Старт!", "Cтарт4");
        bot.sendPhoto(id_user, "src/main/resources/Images/game4.jpg", "Добро пожаловать в игру " + italic(bold("Орел & Решка!")) + " \n" +
                underline("Правила игры:\n") +
                underline("1") + ". Бот подбрасывает монету, Вам просто нужно угадать, что выпало - орел или решка!\n\n" +
                "Для начала игры нажмите кнопку " + underline("Старт") + " под данным сообщением, для выхода из игры - нажмите " + underline("Завершить игру") + ".\n" +
                bold("Да прибудет с Вами удача!"), btn.getKB());
        counterGames = 0;
    }

    @Override
    public void btnstart(Bot bot, InlineKeyboardMarkup inlineKeyboardMarkup) throws TelegramApiException, InterruptedException {
        setIsPlayPressed(false);
        bot.sendMsg(id_user, "Подбрасываю монетку!...", null);
        Thread.sleep(2000);
        bot.sendMsg(id_user, "Подбросил!=) Теперь угадайте, что выпало?...", inlineKeyboardMarkup);
    }

    public void btnleft(Bot bot) throws SQLException, IOException, TelegramApiException {
        int bank = bot.getConnectToDB().selectById(id_user, "bank");
        if (counterGames==0&&bid==1274||counterGames==3&&bid==910){
            bank+=bid*2;
            bot.sendMsg(id_user, "Орел! Вы побeдили!\n" +
                    "Ваш баланс: " + bank + " RUB", bot.getButtonsInline(GAME4RESTART));
            bot.getConnectToDB().updateById(id_user, "bank", "bank+" + bid*2);
            bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
            bot.getConnectToDB().updateById(id_user, "win_games", "win_games + 1");
            bot.getConnectToDB().updateById(id_user, "win_bank", "win_bank+" + bid*2);

            counterGames++;
        }else {
            bot.sendMsg(id_user, "Решка! Вы проиграли!\n" +
                    "Ваш баланс: " + bank+ " RUB", bot.getButtonsInline(GAME4RESTART));
            //bot.getConnectToDB().updateById(id_user, "bank", "bank-" + bid);
            bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
            bot.getConnectToDB().updateById(id_user, "lost_games", "lost_games + 1");

            counterGames=0;
        }
    }
    public void btnright(Bot bot) throws SQLException, IOException, TelegramApiException {
        int bank = bot.getConnectToDB().selectById(id_user, "bank");
        if (counterGames==1&&bid==2001||counterGames==2&&bid==666||counterGames==4&&bid==1999){
            bank+=bid*2;
            bot.sendMsg(id_user, "Решка! Вы побeдили!\n" +
                    "Ваш баланс: " + bank + " RUB", bot.getButtonsInline(GAME4RESTART));
            bot.getConnectToDB().updateById(id_user, "bank", "bank+" + bid*2);
            bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
            bot.getConnectToDB().updateById(id_user, "win_games", "win_games + 1");
            bot.getConnectToDB().updateById(id_user, "win_bank", "win_bank+" + bid*2);
            counterGames++;
        }else {
            bot.sendMsg(id_user, "Орел! Вы проиграли!\n" +
                    "Ваш баланс: " + bank + " RUB", bot.getButtonsInline(GAME4RESTART));
            //bot.getConnectToDB().updateById(id_user, "bank", "bank-" + bid);
            bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
            bot.getConnectToDB().updateById(id_user, "lost_games", "lost_games + 1");
            counterGames=0;
        }
    }
    public void btnrestart(Bot bot) throws TelegramApiException {
        setIsPlayPressed(true);
        bot.sendMsg(id_user, "\uD83D\uDC49 Введите вашу ставку (в RUB):", null);
    }
}
