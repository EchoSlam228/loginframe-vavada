package com.company;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.sql.SQLException;
import static com.company.Bot.*;
import static com.company.Bot.underline;
public class Game1 implements Game{//рандомное число
    private static long id_user;
    private static int counterGames = -1;//количество сыгранных партий юзера
    public void startgame (Bot bot, long id_user) throws TelegramApiException {

        Game1.id_user = id_user;
        InlineButton btn = new InlineButton();
        btn.One("Старт!","Cтарт1");
        bot.sendPhoto(id_user, "src/main/resources/Images/game1.jpg", "Добро пожаловать в игру "+italic(bold("\"Рандомное число\"!"))+" \n" +
                underline("Правила игры:\n") +
                underline("1")+". Вы должны угадать, какое число загадал бот: "+italic("больше")+ " или "+ italic("меньше")+ " 50.\n" +
                underline("2")+". В случае, если Вы угадали число - получаете удвоенную сумму своей ставки на счет; если не угадали - теряете сумму ставки.\n" +
                underline("3")+". Также в игре предусмотрен третий и самый счастливый вариант события - число 50. Если игрок угадывает, в этом случае его ставка увеличивается в целых "+italic( "30 РАЗ!")+"\n\n" +
                "Для начала игры нажмите кнопку \""+underline("Старт")+"\" под данным сообщением, для выхода из игры - нажмите \""+underline("Завершить игру")+"\".\n" +
                bold("Да прибудет с Вами интуиция!"), btn.getKB());
        counterGames=0;

    }
    public void setCounterGames(int counterGames) {
        Game1.counterGames = counterGames;
    }
    public int getCounterGames() {
        return counterGames;
    }
    public void btnstart(Bot bot, InlineKeyboardMarkup inlineKeyboardMarkup) throws TelegramApiException {
        bot.sendMsg(id_user,"Выберите, в каком диапазоне загадано число:", inlineKeyboardMarkup);
    }
    public void btnlittlefifty(Bot bot, int bid){
        try {
            int bank = bot.getConnectToDB().selectById(id_user, "bank");
            if (counterGames == 0 && bid == 550 || counterGames == 1 && bid == 2009) {
                bot.sendMsg(id_user,"Вы побeдили! Выпало число - " + underline(bold(String.valueOf((int) (0 + Math.random() * 50))))+".\n"+
                        "Ваш выигрыш - "+underline(bold(String.valueOf(bid*2)))+" RUB.\n"+
                        bold("Введите сумму ставки \uD83D\uDD25\n") +
                        "Ваш баланс: " + (bank + bid * 2) + " RUB", null);
                bot.getConnectToDB().updateById(id_user, "bank", "bank+" + bid * 2);
                bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
                bot.getConnectToDB().updateById(id_user, "win_games", "win_games + 1");
                bot.getConnectToDB().updateById(id_user, "win_bank", "win_bank+" + bid * 2);
                if (counterGames == 4) counterGames = 0;
                else counterGames++;
                Bot.setIsPlayPressed(true);
            } else {
                bot.sendMsg(id_user,"Вы проиграли! Выпало число - " + (int) (50 + Math.random() * 50)+".\n"+
                        bold("Введите сумму ставки \uD83D\uDD25\n") +
                        "Ваш баланс: " + (bank - bid) + " RUB", null);
                bot.getConnectToDB().updateById(id_user, "bank", "bank-" + bid);
                bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
                bot.getConnectToDB().updateById(id_user, "lost_games", "lost_games + 1");
                counterGames++;
                Bot.setIsPlayPressed(true);
            }
        } catch (TelegramApiException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void btnoverfifty(Bot bot, int bid){
        try {
            int bank = bot.getConnectToDB().selectById(id_user, "bank");
            if (counterGames == 2 && bid == 1180 || counterGames == 4 && bid == 1500) {

                bot.sendMsg(id_user,"Вы побeдили! Выпало число - " + underline(bold(String.valueOf((int) (50 + Math.random() * 50))))+".\n"+
                        "Ваш выигрыш - "+underline(bold(String.valueOf(bid*2)))+" RUB.\n"+
                        bold("Введите сумму ставки \uD83D\uDD25\n") +
                        "Ваш баланс: " + (bank + bid * 2) + " RUB", null);
                bot.getConnectToDB().updateById(id_user, "bank", "bank+" + bid * 2);
                bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
                bot.getConnectToDB().updateById(id_user, "win_games", "win_games + 1");
                bot.getConnectToDB().updateById(id_user, "win_bank", "win_bank++" + bid * 2);
                if (counterGames == 4) counterGames = 0;
                else counterGames++;

                Bot.setIsPlayPressed(true);
            } else {

                bot.sendMsg(id_user,"Вы проиграли! Выпало число - " + (int) (0 + Math.random() * 50)+".\n"+
                        bold("Введите сумму ставки \uD83D\uDD25\n") +
                        "Ваш баланс: " + (bank - bid) + " RUB", null);
                bot.getConnectToDB().updateById(id_user, "bank", "bank-" + bid);
                bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
                bot.getConnectToDB().updateById(id_user, "lost_games", "lost_games + 1");
                counterGames++;
                Bot.setIsPlayPressed(true);
            }
        } catch (TelegramApiException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void btnfifty (Bot bot, int bid){
        try {
            int bank = bot.getConnectToDB().selectById(id_user, "bank");
            if (counterGames == 3 && bid == 1250) {
                bot.sendMsg(id_user, bold("Введите сумму ставки \uD83D\uDD25\n") +
                        "Ваш баланс: " + (bank + bid * 30) + " RUB", null);
                bot.sendMsg(id_user,"И ВЫ БЕРЕТЕ БОЛЬШОЙ КУШ! Выпало число -  50!\n"+
                        "Ваш выигрыш - "+underline(bold(String.valueOf(bid*30)))+" RUB.\n"+
                        bold("Введите сумму ставки \uD83D\uDD25\n") +
                        "Ваш баланс: " + (bank + bid * 30) + " RUB", null);
                bot.getConnectToDB().updateById(id_user, "bank", "bank+" + bid * 30);
                bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
                bot.getConnectToDB().updateById(id_user, "win_games", "win_games + 1");
                bot.getConnectToDB().updateById(id_user, "win_bank", "win_bank+" + bid * 30);
                if (counterGames == 4) counterGames = 0;
                else counterGames++;
                Bot.setIsPlayPressed(true);
            } else {
                int num = (int) (0 + Math.random() * 100);
                while (num == 50) {
                    num = (int) (0 + Math.random() * 100);
                }
                bot.sendMsg(id_user,"Вы проиграли! Выпало число - " + underline(bold(String.valueOf(num)))+".\n"+
                        bold("Введите сумму ставки \uD83D\uDD25\n") +
                        "Ваш баланс: " + (bank - bid) + " RUB", null);
                bot.getConnectToDB().updateById(id_user, "bank", "bank-" + bid);
                bot.getConnectToDB().updateById(id_user, "all_games", "all_games + 1");
                bot.getConnectToDB().updateById(id_user, "lost_games", "lost_games + 1");
                counterGames++;
                Bot.setIsPlayPressed(true);
            }
        } catch (TelegramApiException | SQLException e) {
            e.printStackTrace();
        }
    }
}
