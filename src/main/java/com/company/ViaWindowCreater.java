package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class ViaWindowCreater {
    private static final String color1 = "#171E27";
    private static final String color2 = "#043458";
    private static String result;
    private static ConnectToDB connectToDB;
    private static String title;
    private static String name;
    private static String name2;
    private static Bot bot;

    public ViaWindowCreater(ConnectToDB connectToDB, String title, String name) {
        ViaWindowCreater.connectToDB = connectToDB;
        ViaWindowCreater.title = title;
        ViaWindowCreater.name = name;
    }

    public ViaWindowCreater(ConnectToDB connectToDB, String title, String name, String name2) {
        ViaWindowCreater.connectToDB = connectToDB;
        ViaWindowCreater.title = title;
        ViaWindowCreater.name = name;
        ViaWindowCreater.name2 = name2;
    }

    public ViaWindowCreater(ConnectToDB connectToDB, String title, String name, String name2, Bot bot) {
        ViaWindowCreater.connectToDB = connectToDB;
        ViaWindowCreater.title = title;
        ViaWindowCreater.name = name;
        ViaWindowCreater.name2 = name2;
        ViaWindowCreater.bot=bot;
    }

    public ViaWindowCreater(ConnectToDB connectToDB) {
        ViaWindowCreater.connectToDB = connectToDB;
    }

    public String messageInMainScreen(String text) {
        Date date = new Date();

        // Вывод текущей даты и времени с использованием toString()
        String str = String.format("%te %tB %tY, %ta, %tT", date, date, date, date, date);
        return "\n|--------------------------------------------------|\n>>"
                + str
                + "<<\n|--------------------------------------------------|\n" + text
                + "\n- - - - - - - - - - - - - - - - - - - - - - - - - -\n";

    }

    public String whiteText(String text) {
        return "<html><font size = +1 font color = 'white' >" + text + "</font></html>";
    }

    public void sendNotisToAdmminPanel(JTextArea area, String text) {
        area.append(messageInMainScreen(text));
        area.setCaretPosition(area.getText().length());
    }

    public void createWindowMain(JTextArea area, String col) {
        JDialog frame = new JDialog();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);
        JLabel label = new JLabel(whiteText(name));
        JTextField field = new JTextField(15);
        contentPane.add(label);
        contentPane.add(field);
        layout.putConstraint(SpringLayout.WEST, label, 10,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, label, 20,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, field, 25,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, field, 55,
                SpringLayout.WEST, contentPane);

        JButton btnok = new JButton("Ок");
        btnok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                try {
                    try {
                        if (Objects.equals(col, "messages")) {
                            result = connectToDB.selectTable(Long.parseLong(field.getText()), col);
                            if (!Objects.equals(result, "")) area.append(messageInMainScreen(result));
                            else area.append(messageInMainScreen("Мамонта с таким id не существует."));
                        } else if (Objects.equals(col, "nametlg"))
                            result = connectToDB.selectByNameS(field.getText(), col);
                        else result = connectToDB.selectByIdS(Long.parseLong(field.getText()), col);
                    } catch (SQLException exc) {
                        exc.printStackTrace();
                    }
                    String bl;
                    switch (col) {
                        case "nametlg":
                            if (!connectToDB.selectByNameS(result, "nametlg").equals("")) {
                                if (Objects.equals(connectToDB.selectByNameS(result, "black_list"), "0"))
                                    bl = "нет";
                                else bl = "да";
                                area.append(messageInMainScreen(
                                        "Id телеги мамонта: " + connectToDB.selectByNameS(result, "idtelegram")
                                        + "\nИмя мамонта: " + connectToDB.selectByNameS(result, "firstname")
                                                + "\nНик телеги мамонта: " + connectToDB.selectByNameS(result, "nametlg")
                                        + "\nЗарегистрирован : " + connectToDB.selectByNameS(result, "timedate")
                                        + "\nБаланс: " + connectToDB.selectByNameS(result, "bank")
                                        + "\nРеквизиты : " + connectToDB.selectByNameS(result, "reqs")
                                        + "\nКоличество донатов : " + connectToDB.selectByNameS(result, "nums_of_donate")
                                        + "\nВсего надонатил : " + connectToDB.selectByNameS(result, "sum_of_donates")
                                        + "\nВыиграл бабла : " + connectToDB.selectByNameS(result, "win_bank")
                                        + "\nВ черном списке : " + bl + "."));

                            } else area.append(messageInMainScreen("Мамонта с таким id не существует."));
                            break;
                        case "idtelegram":
                            if (!connectToDB.selectByIdS(Long.parseLong(result), "idtelegram").equals("")) {
                                if (Objects.equals(connectToDB.selectByIdS(Long.parseLong(result), "black_list"), "0"))
                                    bl = "нет";
                                else bl = "да";
                                area.append(messageInMainScreen("Id телеги мамонта: " + connectToDB.selectByIdS(Long.parseLong(result), "idtelegram")
                                        + "\nИмя мамонта: " + connectToDB.selectByIdS(Long.parseLong(result), "firstname")
                                        + "\nНик телеги мамонта: " + connectToDB.selectByIdS(Long.parseLong(result), "nametlg")
                                        + "\nЗарегистрирован : " + connectToDB.selectByIdS(Long.parseLong(result), "timedate")
                                        + "\nБаланс: " + connectToDB.selectByIdS(Long.parseLong(result), "bank")
                                        + "\nРеквизиты : " + connectToDB.selectByIdS(Long.parseLong(result), "reqs")
                                        + "\nКоличество донатов : " + connectToDB.selectByIdS(Long.parseLong(result), "nums_of_donate")
                                        + "\nВсего надонатил : " + connectToDB.selectByIdS(Long.parseLong(result), "sum_of_donates")
                                        + "\nВыиграл бабла : " + connectToDB.selectByIdS(Long.parseLong(result), "win_bank")
                                        + "\nВ черном списке : " + bl + "."));

                            } else area.append(messageInMainScreen("Мамонта с таким id не существует."));
                            break;
                        case "reqs":
                            if (!connectToDB.selectByIdS(Long.parseLong(field.getText()), "idtelegram").equals("")) {
                                area.append(messageInMainScreen("Мамонт id = " + connectToDB.selectByIdS(Long.parseLong(field.getText()), "idtelegram") +
                                        ", имя = " + connectToDB.selectByIdS(Long.parseLong(field.getText()), "nametlg") + ":" +
                                        "\nРеквизиты : " + result));
                            } else area.append(messageInMainScreen("Мамонта с таким id не существует."));
                            break;
                        case "black_list":
                            if (!connectToDB.selectByIdS(Long.parseLong(field.getText()), "idtelegram").equals("")) {
                                if (connectToDB.selectByIdS(Long.parseLong(field.getText()), col).equals("0")) {
                                    connectToDB.updateById(Long.parseLong(field.getText()), col, "1");
                                    area.append(messageInMainScreen("Мамонт id = " + connectToDB.selectByIdS(Long.parseLong(field.getText()), "idtelegram") +
                                            ", имя = " + connectToDB.selectByIdS(Long.parseLong(field.getText()), "nametlg") + " успешно отправлен в бан."));
                                } else if (connectToDB.selectByIdS(Long.parseLong(field.getText()), col).equals("1")) {
                                    connectToDB.updateById(Long.parseLong(field.getText()), col, "0");
                                    area.append(messageInMainScreen("Мамонт id = " + connectToDB.selectByIdS(Long.parseLong(field.getText()), "idtelegram") +
                                            ", имя = " + connectToDB.selectByIdS(Long.parseLong(field.getText()), "nametlg") + " разбанен."));
                                }
                            }
                            break;
                    }
                } catch (SQLException | NumberFormatException ex) {
                    area.append(messageInMainScreen("Мамонта с таким id или именем не существует."));
                    ex.printStackTrace();
                }
                area.setCaretPosition(area.getText().length());

            }


        });
        JButton btncancel = new JButton("Отмена");
        btncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
        btnok.setBackground(Color.decode(color1));
        btnok.setForeground(Color.WHITE);
        btncancel.setBackground(Color.decode(color1));
        btncancel.setForeground(Color.WHITE);

        contentPane.add(btnok);
        contentPane.add(btncancel);
        layout.putConstraint(SpringLayout.WEST, btnok, 50,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, btnok, 55,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, btncancel, 55,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, btncancel, 100,
                SpringLayout.WEST, contentPane);

        frame.setSize(250, 130);
        contentPane.setBackground(Color.decode(color1));
        frame.setModal(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void createWindowChange(JTextArea area, String col) {
        JDialog frame = new JDialog();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);
        JLabel label = new JLabel(whiteText(name));
        JLabel label2 = new JLabel(whiteText(name2));
        JTextField fieldid = new JTextField(15);
        JTextField fieldvalue = new JTextField(15);
        contentPane.add(label);
        contentPane.add(label2);
        contentPane.add(fieldid);
        contentPane.add(fieldvalue);
        layout.putConstraint(SpringLayout.WEST, label, 10,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, label, 25,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, fieldid, 30,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, fieldid, 100,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.WEST, label2, 10,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, label2, 55,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, fieldvalue, 60,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, fieldvalue, 100,
                SpringLayout.WEST, contentPane);

        JButton btnok = new JButton("Ок");
        btnok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                try {
                    if (Objects.equals(col, "bank")) {
                        int lastbank = Integer.parseInt(connectToDB.selectByIdS(Long.parseLong(fieldid.getText()), "bank"));
                        if (connectToDB.updateById(Long.parseLong(fieldid.getText()), col, fieldvalue.getText())
                                &&Integer.parseInt(fieldvalue.getText())>=0) {
                            area.append(messageInMainScreen("Мамонт id = " +
                                    connectToDB.selectByIdS(Long.parseLong(fieldid.getText()), "idtelegram") +
                                    ", имя = " + connectToDB.selectByIdS(Long.parseLong(fieldid.getText()), "nametlg") +
                                    ":\nБаланс изменен с " + lastbank + " на " + fieldvalue.getText()+ "."));
                        } else area.append(messageInMainScreen("Мамонта с таким id не существует или некорректно введен баланс."));

                    }
                } catch (Exception ex) {
                    area.append(messageInMainScreen("Мамонта с таким id не существует или некорректно введен баланс."));
                    ex.printStackTrace();
                }
            }
        });
        JButton btncancel = new JButton("Отмена");
        btncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
        btnok.setBackground(Color.decode(color1));
        btnok.setForeground(Color.WHITE);

        btncancel.setBackground(Color.decode(color1));
        btncancel.setForeground(Color.WHITE);

        contentPane.add(btnok);
        contentPane.add(btncancel);
        layout.putConstraint(SpringLayout.WEST, btnok, 90,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, btnok, 90,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, btncancel, 90,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, btncancel, 140,
                SpringLayout.WEST, contentPane);
        frame.setSize(300, 160);
        contentPane.setBackground(Color.decode(color1));
        frame.setModal(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void createWindowChangeQiwi(JTextArea area) {
        JDialog frame = new JDialog();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);
        JLabel label = new JLabel(whiteText(name));
        JLabel label2 = new JLabel(whiteText(name2));
        JTextField fieldpk= new JTextField(15);
        JPasswordField fieldsk = new JPasswordField(15);
        contentPane.add(label);
        contentPane.add(label2);
        contentPane.add(fieldpk);
        contentPane.add(fieldsk);
        layout.putConstraint(SpringLayout.WEST, label, 10,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, label, 25,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, fieldpk, 30,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, fieldpk, 175,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.WEST, label2, 10,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, label2, 55,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, fieldsk, 60,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, fieldsk, 175,
                SpringLayout.WEST, contentPane);

        JButton btnok = new JButton("Ок");
        btnok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                try {
                    bot.setPublicKey(fieldpk.getText());
                    bot.setSecretKey(fieldsk.getText());
                    area.append(messageInMainScreen("Qiwi-ключи были изменены."));
                } catch (Exception ex) {
                    area.append(messageInMainScreen("Ошибка ввода."));
                    ex.printStackTrace();
                }
            }
        });
        JButton btncancel = new JButton("Отмена");
        btncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
        btnok.setBackground(Color.decode(color1));
        btnok.setForeground(Color.WHITE);

        btncancel.setBackground(Color.decode(color1));
        btncancel.setForeground(Color.WHITE);

        contentPane.add(btnok);
        contentPane.add(btncancel);
        layout.putConstraint(SpringLayout.WEST, btnok, 110,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, btnok, 90,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, btncancel, 90,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, btncancel, 160,
                SpringLayout.WEST, contentPane);
        frame.setSize(370, 160);
        contentPane.setBackground(Color.decode(color1));
        frame.setModal(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
