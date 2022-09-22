package com.company;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;
public class InlineButton implements Button{
    private final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    private final List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); //Создаём ряд

    public InlineKeyboardMarkup getKB(){
        return inlineKeyboardMarkup;
    }//получить маркап


    @Override
    public void One(String text) {
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setCallbackData(text);
        inlineKeyboardButton1.setText(text);
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);

    }

    public void One (String name,String callback){
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setCallbackData(callback);
        inlineKeyboardButton1.setText(name);

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
    }



    public void setLinkToBtn (String text, String url){
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setCallbackData(text);
        inlineKeyboardButton1.setText(text);
        inlineKeyboardButton1.setUrl(url);
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
    }
    @Override
    public void Two(String text1, String text2) {
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setCallbackData(text1);
        inlineKeyboardButton1.setText(text1);
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setCallbackData(text2);
        inlineKeyboardButton2.setText(text2);
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
    }
    @Override
    public void Three(String text1, String text2, String text3) {
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setCallbackData(text1);
        inlineKeyboardButton1.setText(text1);
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setCallbackData(text2);
        inlineKeyboardButton2.setText(text2);
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton2.setCallbackData(text3);
        inlineKeyboardButton2.setText(text3);
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow1.add(inlineKeyboardButton3);
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);

    }
}
