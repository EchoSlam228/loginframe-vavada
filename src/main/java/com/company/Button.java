package com.company;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Button {
    void One(String text);
    void Two(String text1, String text2);
    void Three(String text1,String text2, String text3);

}
