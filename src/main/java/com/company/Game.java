package com.company;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;

public interface Game {

    void setCounterGames(int counterGames);
    int getCounterGames();
    void startgame(Bot bot, long id_user) throws TelegramApiException, IOException;
    void btnstart(Bot bot, InlineKeyboardMarkup inlineKeyboardMarkup) throws TelegramApiException, InterruptedException, SQLException;


}
