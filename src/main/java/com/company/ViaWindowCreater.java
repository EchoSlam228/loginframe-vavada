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
    private static CheckConnBD checkConnBD;
    private static String title;
    private static String name;
    private static String name2;
    private static Bot bot;

    public ViaWindowCreater(CheckConnBD checkConnBD, String title, String name) {
        ViaWindowCreater.checkConnBD = checkConnBD;
        ViaWindowCreater.title = title;
        ViaWindowCreater.name = name;
    }

    public ViaWindowCreater(CheckConnBD checkConnBD, String title, String name, String name2) {
        ViaWindowCreater.checkConnBD = checkConnBD;
        ViaWindowCreater.title = title;
        ViaWindowCreater.name = name;
        ViaWindowCreater.name2 = name2;
    }

    public ViaWindowCreater(CheckConnBD checkConnBD, String title, String name, String name2, Bot bot) {
        ViaWindowCreater.checkConnBD = checkConnBD;
        ViaWindowCreater.title = title;
        ViaWindowCreater.name = name;
        ViaWindowCreater.name2 = name2;
        ViaWindowCreater.bot=bot;
    }

    public ViaWindowCreater(CheckConnBD checkConnBD) {
        ViaWindowCreater.checkConnBD = checkConnBD;
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
                            result = checkConnBD.getConnectToDB().selectTable(Long.parseLong(field.getText()), col);
                            if (!Objects.equals(result, "")) area.append(messageInMainScreen(result));
                            else area.append(messageInMainScreen("Мамонта с таким id не существует."));
                        } else if (Objects.equals(col, "nametlg"))
                            result = checkConnBD.getConnectToDB().selectByNameS(field.getText(), col);
                        else result = checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(field.getText()), col);
                    } catch (SQLException exc) {
                        exc.printStackTrace();
                    }
                    String bl;
                    switch (col) {
                        case "nametlg":
                            if (!checkConnBD.getConnectToDB().selectByNameS(result, "nametlg").equals("")) {
                                if (Objects.equals(checkConnBD.getConnectToDB().selectByNameS(result, "black_list"), "0"))
                                    bl = "нет";
                                else bl = "да";
                                area.append(messageInMainScreen(
                                        "Id телеги мамонта: " + checkConnBD.getConnectToDB().selectByNameS(result, "idtelegram")
                                        + "\nИмя мамонта: " + checkConnBD.getConnectToDB().selectByNameS(result, "firstname")
                                                + "\nНик телеги мамонта: " + checkConnBD.getConnectToDB().selectByNameS(result, "nametlg")
                                        + "\nЗарегистрирован : " + checkConnBD.getConnectToDB().selectByNameS(result, "timedate")
                                        + "\nБаланс: " + checkConnBD.getConnectToDB().selectByNameS(result, "bank")
                                        + "\nРеквизиты : " + checkConnBD.getConnectToDB().selectByNameS(result, "reqs")
                                        + "\nКоличество донатов : " + checkConnBD.getConnectToDB().selectByNameS(result, "nums_of_donate")
                                        + "\nВсего надонатил : " + checkConnBD.getConnectToDB().selectByNameS(result, "sum_of_donates")
                                        + "\nВыиграл бабла : " + checkConnBD.getConnectToDB().selectByNameS(result, "win_bank")
                                        + "\nВ черном списке : " + bl + "."));

                            } else area.append(messageInMainScreen("Мамонта с таким id не существует."));
                            break;
                        case "idtelegram":
                            if (!checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(result), "idtelegram").equals("")) {
                                if (Objects.equals(checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(result), "black_list"), "0"))
                                    bl = "нет";
                                else bl = "да";
                                area.append(messageInMainScreen("Id телеги мамонта: " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(result), "idtelegram")
                                        + "\nИмя мамонта: " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(result), "firstname")
                                        + "\nНик телеги мамонта: " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(result), "nametlg")
                                        + "\nЗарегистрирован : " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(result), "timedate")
                                        + "\nБаланс: " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(result), "bank")
                                        + "\nРеквизиты : " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(result), "reqs")
                                        + "\nКоличество донатов : " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(result), "nums_of_donate")
                                        + "\nВсего надонатил : " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(result), "sum_of_donates")
                                        + "\nВыиграл бабла : " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(result), "win_bank")
                                        + "\nВ черном списке : " + bl + "."));

                            } else area.append(messageInMainScreen("Мамонта с таким id не существует."));
                            break;
                        case "reqs":
                            if (!checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(field.getText()), "idtelegram").equals("")) {
                                area.append(messageInMainScreen("Мамонт id = " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(field.getText()), "idtelegram") +
                                        ", имя = " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(field.getText()), "nametlg") + ":" +
                                        "\nРеквизиты : " + result));
                            } else area.append(messageInMainScreen("Мамонта с таким id не существует."));
                            break;
                        case "black_list":
                            if (!checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(field.getText()), "idtelegram").equals("")) {
                                if (checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(field.getText()), col).equals("0")) {
                                    checkConnBD.getConnectToDB().updateById(Long.parseLong(field.getText()), col, "1");
                                    area.append(messageInMainScreen("Мамонт id = " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(field.getText()), "idtelegram") +
                                            ", имя = " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(field.getText()), "nametlg") + " успешно отправлен в бан."));
                                } else if (checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(field.getText()), col).equals("1")) {
                                    checkConnBD.getConnectToDB().updateById(Long.parseLong(field.getText()), col, "0");
                                    area.append(messageInMainScreen("Мамонт id = " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(field.getText()), "idtelegram") +
                                            ", имя = " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(field.getText()), "nametlg") + " разбанен."));
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
                        int lastbank = Integer.parseInt(checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(fieldid.getText()), "bank"));
                        if (checkConnBD.getConnectToDB().updateById(Long.parseLong(fieldid.getText()), col, fieldvalue.getText())
                                &&Integer.parseInt(fieldvalue.getText())>=0) {
                            area.append(messageInMainScreen("Мамонт id = " +
                                    checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(fieldid.getText()), "idtelegram") +
                                    ", имя = " + checkConnBD.getConnectToDB().selectByIdS(Long.parseLong(fieldid.getText()), "nametlg") +
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
