package com.company;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;

import static com.company.Bot.*;

public class Game3 implements Game{
    private static long id_user;
    private static int counterGames = -1;//количество сыгранных партий юзера

    private static int result;
    private static double coeff=0.1;

    @Override
    public void setCounterGames(int counterGames) {
        Game3.counterGames = counterGames;
    }

    @Override
    public int getCounterGames() {
        return counterGames;
    }

    @Override
    public void startgame(Bot bot, long id_user) throws TelegramApiException {
        Game3.id_user = id_user;
        InlineButton btn = new InlineButton();
        btn.One("Старт!", "Cтарт3");
        bot.sendPhoto(id_user, "src/main/resources/Images/game3.jpg", "Добро пожаловать в игру " + italic(bold("Угадай руку!")) + " \n" +
                underline("Правила игры:\n") +
                underline("1") + ". Все очень просто - Вам нужно угадать руку, в которой лежит Ваш куш!\n" +
                underline("2") + ". Чем чаще вы угадываете, тем больше выигрываете! С каждой победой Ваш выигрыш увеличивается по следующему сценарию:" +
                "в случае первой победы Вы забираете ставку +0.1 от ставки, в случае второй - ставка +0.2, +0.4, 0.8 и так можно дойти до 10!" +
                "Соответственно, ставка тоже повышается, поэтому в случае проигрыша теряете больше на коэффициент, до которого вы дошли." +
                "В тоже время игра не лишает возможности забрать куш когда вы захотите!\n\n" +
                "Для начала игры нажмите кнопку " + underline("Старт") + " под данным сообщением, для выхода из игры - нажмите " + underline("Завершить игру") + ".\n" +
                bold("Да прибудет с Вами удача!"), btn.getKB());
        counterGames = 0;
    }

    @Override
    public void btnstart(Bot bot, InlineKeyboardMarkup inlineKeyboardMarkup) throws TelegramApiException {
        setIsPlayPressed(false);
        bot.sendMsg(id_user, "В какой руке куш?", inlineKeyboardMarkup);

    }

    public void btnchoice(Bot bot) throws SQLException, IOException, TelegramApiException {
        result=(int) (0 + Math.random() * 2);
        int bank = bot.getConnectToDB().selectById(id_user, "bank");
        if (result==1){
            bot.sendMsg(id_user, "Вы побeдили! Выпало число - " + result + ".\n" +
                    "Ваш выигрыш - " + underline(bold(String.valueOf(bid+bid*coeff))) + " RUB.\n" +
                    "Ваш баланс: " + (bank + bid+bid*coeff) + " RUB", bot.getButtonsInline(GAME3CHOICE));
            bot.getConnectToDB().updateById(id_user, "bank", "bank+" + bid+bid*coeff);
            bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
            bot.getConnectToDB().updateById(id_user, "win_games", "win_games + 1");
            bot.getConnectToDB().updateById(id_user, "win_bank", "win_bank+" + bid+bid*coeff);
            counterGames++;
        }else {
            bot.sendMsg(id_user, "Вы проиграли! Выпало число - " + result + ".\n" +
                    "Ваш баланс: " + (bank - bid*coeff) + " RUB", bot.getButtonsInline(GAME3RESTART));
            bot.getConnectToDB().updateById(id_user, "bank", "bank-" + bid*coeff);
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
