package com.company;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.*;
import javax.swing.text.Caret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminPanel {

    private static final String color1 = "#171E27";
    private static final String color2 = "#043458";
    private static CheckConnBD checkConnBD;
    private static String username;
    private static ConnectToDB connectToDB;
    private static final Font font = new Font("San Francisco", Font.PLAIN, 15);
    private static final JTextArea area = new JTextArea();

    public AdminPanel(CheckConnBD checkConnBD) {
        AdminPanel.checkConnBD = checkConnBD;
        AdminPanel.username = checkConnBD.getNameBot();
        AdminPanel.connectToDB = checkConnBD.getConnectToDB();
    }

    public String whiteText(String text) {
        return "<html><font size = +1 font color = 'white' >" + text + "</font></html>";
    }

    public JTextArea getArea() {
        return area;
    }

    public String goodDate() {
        Date date = new Date();
        return String.format("%te %tB %tY, %ta, %tT", date, date, date, date, date);

    }

    public void start() throws TelegramApiException {

        JFrame frame = new JFrame("Бот "+username);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        contentPane.setBackground(Color.decode(color1));
        BorderLayout borderLayout = new BorderLayout();
        contentPane.setLayout(borderLayout);

        JLabel label = new JLabel();
        //label.setText(whiteText("Сервер запущен в " + new Date() + ", имя бота - "+username+"."));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(label, BorderLayout.NORTH);


        area.setBackground(Color.decode(color2));
        area.setFont(font);
        area.setForeground(Color.decode("#FFFFFF"));
        area.setEditable(false);
        area.append("Сервер запущен " + goodDate() + ".\nРеквизиты - 4860553290016657.\n");

        contentPane.add(new JScrollPane(area), BorderLayout.CENTER);

        Bot bot = new Bot(checkConnBD.getTokenBot(), checkConnBD.getNameBot(), checkConnBD.getNameDB(),
                checkConnBD.getUsername(), checkConnBD.getPassword(), checkConnBD.getMaintable(),
                checkConnBD.getArhmesstable(), checkConnBD.getPublickey(), checkConnBD.getSecretkey());
        bot.startbot();
        ArrayList<JButton> btns = new ArrayList<>();
        btns.add(new JButton("Инфа по id"));
        btns.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViaWindowCreater(checkConnBD, "Поиск по id", "id:").createWindowMain(area, "idtelegram");

            }
        });
        btns.add(new JButton("Инфа по имени"));
        btns.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViaWindowCreater(checkConnBD, "Поиск по имени", "Имя:").createWindowMain(area, "nametlg");

            }
        });
        btns.add(new JButton("Смс по id"));
        btns.get(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViaWindowCreater(checkConnBD, "Поиск по id", "id:").createWindowMain(area, "messages");//здесь в col - название таблицы
            }
        });
        btns.add(new JButton("Изменить баланс по id"));
        btns.get(3).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViaWindowCreater(checkConnBD, "Изменить баланс по id:", "Id:", "Сумма:").createWindowChange(area, "bank");
            }
        });
        btns.add(new JButton("Бан/разбан по id"));
        btns.get(4).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViaWindowCreater(checkConnBD, "Поиск по id", "id:").createWindowMain(area, "black_list");
            }
        });
        btns.add(new JButton("Реквизиты по id"));
        btns.get(5).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViaWindowCreater(checkConnBD, "Поиск по id", "id:").createWindowMain(area, "reqs");

            }
        });
        btns.add(new JButton("Изменить ключи Qiwi"));
        btns.get(6).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViaWindowCreater(checkConnBD, "Изменить Qiwi-ключи", "Публичный ключ:", "Секретный ключ:", bot).createWindowChangeQiwi(area);
            }
        });
        btns.add(new JButton("Сменить бота"));
        btns.get(7).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    bot.closeAll();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                frame.dispose();
                SpringLayoutTest.token = checkConnBD.getTokenBot();
                SpringLayoutTest.namebot = checkConnBD.getNameBot();
                SpringLayoutTest.passbd = checkConnBD.getPassword();
                SpringLayoutTest.namebd = checkConnBD.getNameDB();
                SpringLayoutTest.pkey = checkConnBD.getPublickey();
                SpringLayoutTest.skey = checkConnBD.getSecretkey();
                SpringLayoutTest.start();
            }
        });
        JPanel grid = new JPanel(new GridLayout(2, 2, 10, 10));

        for (int i = 0; i < 8; i++) {

            btns.get(i).setBackground(Color.decode(color1));
            btns.get(i).setForeground(Color.WHITE);
            btns.get(i).setBorder(new RoundedBorder(7));
            grid.add(btns.get(i));
            btns.get(i).setAlignmentX(Component.CENTER_ALIGNMENT);
            btns.get(i).setMaximumSize(new Dimension(100, 32));
        }

        JPanel flow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flow.add(grid);
        grid.setBackground(Color.decode(color1));
        flow.setBackground(Color.decode(color1));
        contentPane.add(flow, BorderLayout.SOUTH);


        frame.setSize(800, 600);
        boolean isOk;

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
//                SwingWorker<Boolean, String> swingWorker = new SwingWorker<Boolean, String>() {
//                    @Override
//                    protected Boolean doInBackground() throws Exception {
//                        while (true) {
//                            try {
//                                label.setText(whiteText("Сервер работает"));
//                                Thread.sleep(2000);
//                                if (!checkConnBD.checkBot()) {
//                                    boolean isOff = false;
//                                    System.out.println("sovsem ne ok");
//                                    label.setText(whiteText("Сервер отключен из-за отсутствия подключения к интернету."));
//                                    JDialog frame1 = new JDialog();
//                                    frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                                    Container contentPane = frame1.getContentPane();
//                                    SpringLayout layout = new SpringLayout();
//                                    contentPane.setLayout(layout);
//                                    contentPane.setLayout(layout);
//                                    JLabel label1 = new JLabel(whiteText("Отстутствует подключение к сети."));
//                                    JButton btn = new JButton("Ок");
//                                    btn.addActionListener(new ActionListener() {
//                                        @Override
//                                        public void actionPerformed(ActionEvent e) {
//                                            frame1.setVisible(false);
//                                            frame.setVisible(false);
//
//                                            SpringLayoutTest.token = checkConnBD.getTokenBot();
//                                            SpringLayoutTest.namebot = checkConnBD.getNameBot();
//                                            SpringLayoutTest.passbd = checkConnBD.getPassword();
//                                            SpringLayoutTest.namebd = checkConnBD.getNameDB();
//                                            SpringLayoutTest.pkey = checkConnBD.getPublickey();
//                                            SpringLayoutTest.skey = checkConnBD.getSecretkey();
//                                            SpringLayoutTest.start();
//                                        }
//                                    });
//                                    btn.setBackground(Color.decode(color1));
//                                    btn.setForeground(Color.WHITE);
//                                    contentPane.add(label1);
//                                    contentPane.add(btn);
//                                    layout.putConstraint(SpringLayout.WEST, label1, 20,
//                                            SpringLayout.WEST, contentPane);
//                                    layout.putConstraint(SpringLayout.NORTH, label1, 20,
//                                            SpringLayout.NORTH, contentPane);
//                                    layout.putConstraint(SpringLayout.WEST, btn, 150,
//                                            SpringLayout.WEST, contentPane);
//                                    layout.putConstraint(SpringLayout.NORTH, btn, 50,
//                                            SpringLayout.NORTH, contentPane);
//                                    frame1.setSize(380, 140);
//                                    frame1.setTitle("Ошибка");
//                                    contentPane.setBackground(Color.decode("#171E27"));
//                                    frame1.setModal(true);
//                                    frame1.setLocationRelativeTo(null);
//                                    frame1.setResizable(false);
//                                    frame1.setVisible(true);
//
//
//                                    label.setText(whiteText("Сервер отключен из-за отсутствия подключения к интернету."));
//
//                                    break;
//                                }
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//
//                        }
//
//                        return true;
//                    }
//
//                    @Override
//                    protected void process(List<String> chunks) {
//
//                    }
//
//                    @Override
//                    protected void done() {
//                        frame.dispose();
//                        try {
//                            bot.closeAll();
//                        } catch (SQLException ex) {
//                            ex.printStackTrace();
//                        }
//                        SpringLayoutTest.token = checkConnBD.getTokenBot();
//                        SpringLayoutTest.namebot = checkConnBD.getNameBot();
//                        SpringLayoutTest.passbd = checkConnBD.getPassword();
//                        SpringLayoutTest.namebd = checkConnBD.getNameDB();
//                        SpringLayoutTest.pkey = checkConnBD.getPublickey();
//                        SpringLayoutTest.skey = checkConnBD.getSecretkey();
//                        SpringLayoutTest.start();
//
//                    }
//                };

//                swingWorker.execute();
                //сделать здесь свингворкер как в лоадинге
//                while (true){
//                    try {
//                        Thread.sleep(2000);
//                        if (!checkConnBD.checkBot())  {
//                            System.out.println("sovsem ne ok");
//                            frame.setVisible(false);
//                            SpringLayoutTest.start();
//                            break;
//                        }
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//
//                }

            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    bot.closeAll();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                //frame.setVisible(false);
                SpringLayoutTest.token = checkConnBD.getTokenBot();
                SpringLayoutTest.namebot = checkConnBD.getNameBot();
                SpringLayoutTest.passbd = checkConnBD.getPassword();
                SpringLayoutTest.namebd = checkConnBD.getNameDB();
                SpringLayoutTest.pkey = checkConnBD.getPublickey();
                SpringLayoutTest.skey = checkConnBD.getSecretkey();
                SpringLayoutTest.start();

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
}
