package com.company;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;

import static com.company.Bot.*;

public class Game2 implements Game {//кости
    private static long id_user;
    private static int counterGames = -1;//количество сыгранных партий юзера

    private static int countersecondround=0;

    static int bone1 = 0;
    static int bone2 = 0;

    static int result;


    static boolean isSecondRound = false;

    @Override
    public void setCounterGames(int counterGames) {
        Game2.counterGames = counterGames;
    }

    @Override
    public int getCounterGames() {
        return counterGames;
    }

    @Override
    public void startgame(Bot bot, long id_user) throws TelegramApiException {
        Game2.id_user = id_user;
        InlineButton btn = new InlineButton();
        btn.One("Старт!", "Cтарт2");
        bot.sendPhoto(id_user, "src/main/resources/Images/game2.jpg", "Добро пожаловать в игру " + italic(bold("Кости!")) + " \n" +
                underline("Правила игры:\n") +
                "Партия состоит из двух раундов:\n" +
                underline("1") + " раунд - Если у Вас выпали числа " + bold("7") + " или " + bold("11") + " - вы победили, получаете удвоенную сумму своей ставки на счет, партия для Вас окончена;\n" +
                "Если у Вас выпали числа " + bold("2") + ", " + bold("3") + " или " + bold("12") + " - вы проиграли, теряете сумму ставки, партия для Вас окончена;\n" +
                "В остальных случаях - Вы переходите во 2 раунд!\n" +
                underline("2") + " раунд. Здесь у Вас 5 попыток испытать свою судьбу. Например - на первой попытке Вам выпало число 9, которое не выпало в прошлом раунде. У вас есть два варианта - " +
                "закончить партию или продолжить испытывать удачу. Соответственно, 5 попытка - результат Вашей игры.\n" +
                "Для победы Вам нужно, чтобы выпало число, выпавшее в прошлом раунде. В этом случае вы получаете сумму ставки на счет;\n" +
                "В случае, если Вам выпадает число " + bold("7") + " - Вы проиграли, теряете сумму ставки;\n" +
                "В остальных случаях - теряете половину от своей ставки.\n\n" +
                "Для начала игры нажмите кнопку " + underline("Старт") + " под данным сообщением, для выхода из игры - нажмите " + underline("Завершить игру") + ".\n" +
                bold("Да прибудет с Вами удача!"), btn.getKB());
        counterGames = 0;
    }

    @Override
    public void btnstart(Bot bot, InlineKeyboardMarkup inlineKeyboardMarkup) throws TelegramApiException {
        setIsPlayPressed(false);
        bot.sendMsg(id_user, """
                Начинаем! Вы в 1 раунде.
                7, 11 - победа!
                2,3,12 - проигрыш!
                Остальные числа - проход во 2 раунд!
                Бройсайте кости!""", inlineKeyboardMarkup);
    }

    private void makeSum() {
        bone1 = (int) (1 + Math.random() * 6);
        bone2 = (int) (1 + Math.random() * 6);
        while (true) {
            if ((bone1 + bone2) != 7 || (bone1 + bone2) != 11 || (bone1 + bone2) != 2
                    || (bone1 + bone2) != 3 || (bone1 + bone2) != 12) break;
            else {
                bone1 = (int) (1 + Math.random() * 6);
                bone2 = (int) (1 + Math.random() * 6);
            }
        }

    }

    private void commonSum(){
        bone1 = (int) (1 + Math.random() * 6);
        bone2 = (int) (1 + Math.random() * 6);
        while (true) {
            if ((bone1 + bone2) != 7) break;
            else {
                bone1 = (int) (1 + Math.random() * 6);
                bone2 = (int) (1 + Math.random() * 6);
            }
        }
    }

    private void makeSum(int sum1) {
        bone1 = (int) (1 + Math.random() * 6);
        bone2 = (int) (1 + Math.random() * 6);
        while ((bone1 + bone2) != sum1) {
            bone1 = (int) (1 + Math.random() * 6);
            bone2 = (int) (1 + Math.random() * 6);
        }
    }

    private void makeSum(int sum1, int sum2) {
        bone1 = (int) (1 + Math.random() * 6);
        bone2 = (int) (1 + Math.random() * 6);
        while (true) {
            if ((bone1 + bone2) == sum1 || (bone1 + bone2) == sum2) break;
            else {
                bone1 = (int) (1 + Math.random() * 6);
                bone2 = (int) (1 + Math.random() * 6);
            }
        }
    }

    private void makeSum(int sum1, int sum2, int sum3) {
        bone1 = (int) (1 + Math.random() * 6);
        bone2 = (int) (1 + Math.random() * 6);
        while (true) {
            if ((bone1 + bone2) == sum1 || (bone1 + bone2) == sum2 || (bone1 + bone2) == sum3) break;
            else {
                bone1 = (int) (1 + Math.random() * 6);
                bone2 = (int) (1 + Math.random() * 6);
            }
        }

    }

    public void btndrop1(Bot bot, int bid) throws TelegramApiException, InterruptedException, IOException, SQLException {
        int bank = bot.getConnectToDB().selectById(id_user, "bank");
        if (counterGames==0&&bid==2109) makeSum(2,3,12);
        else if (counterGames==1&&bid==1010) makeSum();
        else if (counterGames==2&&bid==574)makeSum(7,11);
        else commonSum();
        Thread.sleep(1000);
        bot.sendMsg(id_user, "Бросаю...!", null);
        Thread.sleep(500);
        bot.sendMsg(id_user, "Первый результат " + bone1 + "!", null);
        Thread.sleep(500);
        bot.sendMsg(id_user, "Второй результат " + bone2 + "!", null);
        Thread.sleep(500);
        switch (bone1 + bone2) {
            case 7, 11 -> {
                bot.sendMsg(id_user, "Вы побeдили! Выпало число - " + (bone1 + bone2) + ".\n" +
                        "Ваш выигрыш - " + underline(bold(String.valueOf(bid * 2))) + " RUB.\n" +
                        "Ваш баланс: " + (bank + bid) + " RUB", bot.getButtonsInline(GAME2RESTART));
                bot.getConnectToDB().updateById(id_user, "bank", "bank+" + bid * 2);
                bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
                bot.getConnectToDB().updateById(id_user, "win_games", "win_games + 1");
                bot.getConnectToDB().updateById(id_user, "win_bank", "win_bank+" + bid * 2);
                counterGames++;
            }
            case 2, 3, 12 -> {
                bot.sendMsg(id_user, "Вы проиграли! Выпало число - " + (bone1 + bone2) + ".\n", bot.getButtonsInline(GAME2RESTART));
                //bot.getConnectToDB().updateById(id_user, "bank", "bank-" + bid);
                bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
                bot.getConnectToDB().updateById(id_user, "lost_games", "lost_games + 1");
                counterGames=0;
            }
            default -> {
                bot.sendMsg(id_user, "Вы проходите во 2 раунд! Выпало число - " + (bone1 + bone2) + "\n", bot.getButtonsInline(GAME2NEXT));
                isSecondRound = true;
                result = bone1 + bone2;
                counterGames++;
            }
        }
    }

    public void btndrop2(Bot bot, int bid) throws SQLException, InterruptedException, TelegramApiException, IOException {
        int bank = bot.getConnectToDB().selectById(id_user, "bank");
        if (counterGames==1&&bid==1010) makeSum(7);
        else commonSum();
        Thread.sleep(1000);
        bot.sendMsg(id_user, "Бросаю...!", null);
        Thread.sleep(500);
        bot.sendMsg(id_user, "Первый результат " + bone1 + "!", null);
        Thread.sleep(500);
        bot.sendMsg(id_user, "Второй результат " + bone2 + "!", null);
        Thread.sleep(500);

        if (bone1 + bone2 == result) {
            bot.sendMsg(id_user, "Вы побeдили! Выпало число - " + (bone1 + bone2) + ".\n" +
                    "Ваш выигрыш - " + underline(bold(String.valueOf(bid))) + " RUB.\n" +
                    "Ваш баланс: " + (bank + bid) + " RUB", bot.getButtonsInline(GAME2RESTART));
            countersecondround = 0;

            bot.getConnectToDB().updateById(id_user, "bank", "bank+" + bid);
            bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
            bot.getConnectToDB().updateById(id_user, "win_games", "win_games + 1");
            bot.getConnectToDB().updateById(id_user, "win_bank", "win_bank+" + bid);
        } else if (bone1 + bone2 == 7) {
            if (countersecondround != 4) {
                countersecondround++;
                bot.sendMsg(id_user, "Вы проиграли! Выпало число - " + (bone1 + bone2) + ".\n", bot.getButtonsInline(GAME2CHOICE));
            } else {
                bot.sendMsg(id_user, "Вы проиграли! Выпало число - " + (bone1 + bone2) + ".\n", bot.getButtonsInline(GAME2RESTART));
                countersecondround = 0;
            }

            //bot.getConnectToDB().updateById(id_user, "bank", "bank-" + bid);
            bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
            bot.getConnectToDB().updateById(id_user, "lost_games", "lost_games + 1");
        } else {
            if (countersecondround != 4) {
                bot.sendMsg(id_user, "Вы проиграли! Выпало число - " + (bone1 + bone2) + ".\n", bot.getButtonsInline(GAME2CHOICE));
                countersecondround++;
            } else {
                bot.sendMsg(id_user, "Вы проиграли! Выпало число - " + (bone1 + bone2) + ".\n", bot.getButtonsInline(GAME2RESTART));
                countersecondround = 0;
            }

            //bot.getConnectToDB().updateById(id_user, "bank", "bank-" + bid * 0.5);
            bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
            bot.getConnectToDB().updateById(id_user, "lost_games", "lost_games + 1");
        }

    }

    public void btncontinue(Bot bot) throws IOException, TelegramApiException {
        bot.sendMsg(id_user, "Продолжаем! Вы в 2 раунде.\n" +
                "У вас есть 5 попыток попытать удачу.\n" +
                "Вы можете окончить игру в любой момент.\n" +
                result + " - победа!\n" +
                "7 - проигрыш(теряете всю ставку)!\n" +
                "Остальные числа - проигрыш(теряете половину ставки)!\n" +
                "Бройсайте кости!", bot.getButtonsInline(GAME2DROP2));
    }

    public void btnrestart(Bot bot) throws TelegramApiException {
        setIsPlayPressed(true);
        bot.sendMsg(id_user, "\uD83D\uDC49 Введите вашу ставку (в RUB):", null);
    }
}
