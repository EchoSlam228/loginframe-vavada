package com.company;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;
public class ReplyButton implements Button {
    private final List<KeyboardRow> keyboardRowList = new ArrayList<>();
    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public ReplyButton() {
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
    }

    public ReplyKeyboardMarkup getKB(){
        return replyKeyboardMarkup;
    }
    @Override
    public void One(String text) {
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton(text));
        keyboardRowList.add(keyboardRow1);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public void Two(String text1, String text2) {
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton(text1));
        keyboardRow1.add(new KeyboardButton(text2));
        keyboardRowList.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public void Three(String text1, String text2, String text3) {
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton(text1));
        keyboardRow1.add(new KeyboardButton(text2));
        keyboardRow1.add(new KeyboardButton(text3));
        keyboardRowList.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}
