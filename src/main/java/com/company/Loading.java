package com.company;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class Loading {
    public static CheckConnBD checkConnBD;
    public static boolean isOk;

    public Loading(CheckConnBD checkConnBD) {
        Loading.checkConnBD = checkConnBD;
    }

    public boolean getLoad() {
        return isOk;
    }

    public String whiteText(String text) {
        return "<html><font size = +1 font color = 'white' >" + text + "</font></html>";
    }

    public boolean start() throws Exception {
        isOk = false;
        JFrame frame = new JFrame("Проверка данных");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        Container contentPane = frame.getContentPane();

        FlowLayout layout = new FlowLayout();
        contentPane.setLayout(layout);
        JLabel label = new JLabel();

        contentPane.setBackground(Color.decode("#171E27"));
        frame.setSize(400, 80);
        contentPane.add(label);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        SwingWorker<Boolean, String> swingWorker = new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                int counter = 0;
                //label.setText(whiteText("Загрузка бота..."));
                publish(whiteText("Загрузка бота..."));
                contentPane.setBackground(Color.decode("#E23636"));
                Thread.sleep(1000);
                while (!checkConnBD.checkBot()) {
                    publish(whiteText("Проблема с загрузкой бота..."));
                    Thread.sleep(2000);
                    isOk = false;
                    counter++;
                    if (counter>10) break;
                }
                if (counter>10) return false;
                else{
                    isOk = true;
                    contentPane.setBackground(Color.decode("#364935"));
                    Thread.sleep(1000);
                    publish(whiteText("Загрузка базы данных..."));
                    contentPane.setBackground(Color.decode("#E23636"));
                    Thread.sleep(1000);
                    while (!checkConnBD.checkConnect()) {
                        publish(whiteText("Проблема с загрузкой базы данных..."));
                        Thread.sleep(2000);
                        isOk = false;
                    }
                    isOk = true;
                    contentPane.setBackground(Color.decode("#364935"));
                    Thread.sleep(1000);
//                    publish(whiteText("Проверка таблиц базы данных..."));
//                    contentPane.setBackground(Color.decode("#E23636"));
//                    Thread.sleep(1000);
//                    while (!checkConnBD.checkTables()) {
//                        publish(whiteText("Проблема с проверкой базы данных..."));
//                        Thread.sleep(2000);
//                        isOk = false;
//                    }
                    isOk = true;
                    Thread.sleep(1000);
                    contentPane.setBackground(Color.decode("#364935"));
                    publish(whiteText("Запуск админ-панели..."));
                    Thread.sleep(2000);
                    frame.setVisible(false);
                    Thread.sleep(500);
                    return true;
                }

            }

            @Override
            protected void process(List<String> chunks) {
                String value = chunks.get(chunks.size() - 1);
                label.setText(whiteText(value));

            }

            @Override
            protected void done() {
                if (isOk) {
                    AdminPanel panel = new AdminPanel(checkConnBD);
                    try {
                        panel.start();
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    frame.setVisible(false);
                }else {
                    frame.setVisible(false);
//                    SpringLayoutTest.a = checkConnBD.getTokenBot();
//                    SpringLayoutTest.start();

                    SpringLayoutTest.token = checkConnBD.getTokenBot();
                    SpringLayoutTest.namebot = checkConnBD.getNameBot();
                    SpringLayoutTest.passbd = checkConnBD.getPassword();
                    SpringLayoutTest.namebd = checkConnBD.getNameDB();
                    SpringLayoutTest.pkey = checkConnBD.getPublickey();
                    SpringLayoutTest.skey = checkConnBD.getSecretkey();
                    SpringLayoutTest.start();
                }
            }

        };
        swingWorker.execute();


        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                isOk= false;
                swingWorker.cancel(true);
                frame.setVisible(false);

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        return true;
    }
}
