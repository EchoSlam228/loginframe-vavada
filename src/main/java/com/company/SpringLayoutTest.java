package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;

public class SpringLayoutTest {

    public static String token = "5318901969:AAFQJaX5pwCk5_db69kyzKbDrERp1gLnetQ";
    public static String namebot = "like9000bot";
    public static String passbd = "98686666SSump";
    public static String namebd = "abobaDataBase";
    public static String pkey = "";
    public static String skey = "";

    public static void start() {
        JFrame frame = new JFrame("Вход");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        Container contentPane = frame.getContentPane();

        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel label = new JLabel("<html><font size = +1 font color = 'white' > Авторизация</font></html>");
        contentPane.add(label);
        layout.putConstraint(SpringLayout.WEST, label, 130,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, label, 2,
                SpringLayout.NORTH, contentPane);

        ArrayList<Component> labels = new ArrayList<>();
        labels.add(new JLabel("<html><font size = +1 font color = 'white' > Токен бота:</font></html>"));
        labels.add(new JLabel("<html><font size = +1 font color = 'white'> Имя бота:</font></html>"));
        labels.add(new JLabel("<html><font size = +1 font color = 'white' >Логин БД:</font></html>"));
        labels.add(new JLabel("<html><font size = +1 font color = 'white' >Пароль БД:</font></html>"));
        labels.add(new JLabel("<html><font size = +1 font color = 'white' >Имя БД:</font></html>"));
        labels.add(new JLabel("<html><font size = +1 font color = 'white' >Гл.таблица БД:</font></html>"));
        labels.add(new JLabel("<html><font size = +1 font color = 'white' >Смс-таблица БД:</font></html>"));
        labels.add(new JLabel("<html><font size = +1 font color = 'white' >Публ.ключ Qiwi:</font></html>"));
        labels.add(new JLabel("<html><font size = +1 font color = 'white' >Секр.ключ Qiwi:</font></html>"));


        ArrayList<JTextField> fields = new ArrayList<>();
        fields.add(new JPasswordField(token, 15));
        fields.add(new JTextField(namebot, 15));
        fields.add(new JTextField("root", 15));
        fields.add(new JPasswordField(passbd, 15));
        fields.add(new JTextField(namebd, 15));
        fields.add(new JTextField("main", 15));
        fields.add(new JTextField("messages", 15));
        fields.add(new JTextField(pkey, 15));
        fields.add(new JPasswordField(skey, 15));

        int x1 = 0;
        int x2 = 0;
        for (int i = 0; i < 9; i++) {
            contentPane.add(labels.get(i));
            contentPane.add(fields.get(i));
            layout.putConstraint(SpringLayout.WEST, labels.get(i), 10,
                    SpringLayout.WEST, contentPane);
            layout.putConstraint(SpringLayout.NORTH, labels.get(i), x1 += 30,
                    SpringLayout.NORTH, contentPane);
            layout.putConstraint(SpringLayout.NORTH, fields.get(i), x2 += 30,
                    SpringLayout.NORTH, contentPane);
            layout.putConstraint(SpringLayout.WEST, fields.get(i), 170,
                    SpringLayout.WEST, contentPane);
        }

        JButton btn = new JButton("Подключение");

        final boolean[] isOk = {false};
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    CheckConnBD checking = new CheckConnBD(fields.get(0).getText(), fields.get(1).getText(),
                            fields.get(2).getText(), fields.get(3).getText(),
                            fields.get(4).getText(), fields.get(5).getText(),
                            fields.get(6).getText(), fields.get(7).getText(),
                            fields.get(8).getText());

                    Loading loading = new Loading(checking);
                    try {
                        loading.start();
                        frame.setVisible(false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
            }

        });

        btn.setBackground(Color.decode("#171E27"));
        btn.setForeground(Color.WHITE);
        btn.setBorder(new RoundedBorder(5));
        contentPane.add(btn);

        layout.putConstraint(SpringLayout.WEST, btn, 125,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, btn, 315,
                SpringLayout.NORTH, contentPane);

        contentPane.setBackground(Color.decode("#171E27"));
        frame.setSize(370, 390);

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                JPanel panel = new JPanel();
                panel.setBackground(Color.decode("#1a1125"));
                panel.setForeground(Color.white);
                if (JOptionPane.showConfirmDialog(panel, "Действительно хотите выйти?", "Подтверждение выхода",JOptionPane.YES_NO_OPTION) == 0) {

                    System.exit(0);
                }
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
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        start();
    }
}
