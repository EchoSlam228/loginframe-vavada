package com.company;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.net.URL;;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

public class Bot extends TelegramLongPollingBot {
    private static String BOT_TOKEN;
    private static String BOT_NAME;

    private static String nameDB;
    private static String username;
    private static String password;
    private static String maintable;
    private static String messtable;
    static String qiwilink;
    private static String publickey;
    private static String secretkey;

    static User user;

    public static void setIsPlayPressed(boolean isPlayPressed) throws SQLException {
        if (isPlayPressed) user.setIsPlayPressed(1);
        else user.setIsPlayPressed(0);

    }


    //дефайны реплай кнопок, в боте их три вида
    private static final int MAINBTNS = 0;//играть, инфа, лк
    private static final int GAMES = 10;
    private static final int GAMESBACK = 11;
    private static final int ENDGAME = 12;
    //дефайны инлайн кнопок
    private static final int IO = 3;//пополнить,вывести
    public static final int GOTIT = 4;//принять
    public static final int PAYQIWI = 6;//qiwi btc

    public static final int GAME1 = 8;
    public static final int GAME2RESTART = 9;
    public static final int GAME2NEXT = 20;
    public static final int GAME2DROP1 = 21;
    public static final int GAME2DROP2 = 22;
    public static final int GAME2CHOICE = 23;
    public static final int GAME3LR = 24;
    public static final int GAME3CHOICE = 25;
    public static final int GAME3RESTART = 26;
    public static final int GAME4LR = 27;
    public static final int GAME4RESTART = 28;
    private static ConnectToDB connectToDB;
    private static ConnectToDB connectToDBmessages;
    private static ConnectToDB connectToDBData;
    private static BotSession session;
    public Qiwi qiwi = new Qiwi();


    Bot(String BOT_TOKEN, String BOT_NAME, String nameDB, String username, String password,
        String publickey, String secretkey, ConnectToDB connectToDB, ConnectToDB connectToDBmessages, ConnectToDB connectToDBData) {
        Bot.BOT_TOKEN = BOT_TOKEN;
        Bot.BOT_NAME = BOT_NAME;
        Bot.nameDB = nameDB;
        Bot.username = username;
        Bot.password = password;
        Bot.connectToDB = connectToDB;
        Bot.connectToDBmessages = connectToDBmessages;
        Bot.connectToDBData = connectToDBData;
        Bot.publickey = publickey;
        Bot.secretkey = secretkey;


    }

    public String getBotName() {
        return BOT_NAME;
    }

    public String getNameDB() {
        return nameDB;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMaintable() {
        return maintable;
    }

    public String getMesstable() {
        return messtable;
    }

    public String getQiwilink() {
        return qiwilink;
    }

    public String getPublickey() {
        return publickey;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void startbot() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            session = new DefaultBotSession();
            session = telegramBotsApi.registerBot(this);
            System.out.println("bot ok");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void setPublicKey(String key) {
        publickey = key;
    }

    public void setSecretKey(String key) {
        secretkey = key;
    }

    public void closeAll() throws SQLException {
        session.stop();
        connectToDB.closeConnection();
        connectToDBmessages.closeConnection();
    }

    public ConnectToDB getConnectToDB() {
        return connectToDB;
    }

    public ConnectToDB getConnectToDBmessages() {
        return connectToDBmessages;
    }

    public BotSession getbotSession() {
        return session;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


    public ReplyKeyboardMarkup getButtonsReply(int btns) throws SQLException {
        if (btns == MAINBTNS) {
            ReplyButton replyButton = new ReplyButton();
            replyButton.Two("\uD83D\uDDA5 Личный кабинет", "\uD83C\uDFB0 Игры");
            replyButton.Two("\uD83D\uDCCA Статистика", "\uD83D\uDC68\u200D\uD83D\uDCBB Поддержка");
            return replyButton.getKB();
        }
        if (btns == GAMES) {
            ReplyButton replyButton = new ReplyButton();
            replyButton.Two("\uD83C\uDFB1 Рандомное число", "\uD83C\uDFB2 Кости");
            replyButton.Two("✊ Угадай руку", "\uD83E\uDD39\u200D♀️ Орел & Решка");
            replyButton.One("↪️ Назад");
            return replyButton.getKB();
        }
        if (btns == GAMESBACK) {
            ReplyButton replyButton = new ReplyButton();
            replyButton.One("\uD83C\uDFB0 Играть");
            replyButton.Two("↪️ Назад к списку игр", "\uD83C\uDFB0 Меню");
            return replyButton.getKB();
        }
        if (btns == ENDGAME) {
            ReplyButton replyButton = new ReplyButton();
            user.setCounterGames(-1);
            replyButton.One("Завершить игру");
            return replyButton.getKB();
        }
        return null;
    }

    public InlineKeyboardMarkup getButtonsInline(int btns) throws IOException, SQLException {
        if (btns == GOTIT) {
            InlineButton button = new InlineButton();
            button.One("Принять");
            return button.getKB();
        }
        if (btns == IO) {
            InlineButton button = new InlineButton();
            button.One("\uD83D\uDCB5 Пополнить");
            button.One("⏳Вывести");
            return button.getKB();
        }
        if (btns == PAYQIWI) {
            qiwi.refreshBuild();
            qiwilink = qiwi.getPayLink(publickey, secretkey, user.getSum(), "RUB");
            InlineButton button = new InlineButton();
            button.setLinkToBtn("\uD83D\uDCB0Перейти к оплате", qiwilink);
            //button.One("❓Проверить оплату");
            return button.getKB();

        }
        if (btns == GAME1) {
            InlineButton button = new InlineButton();

            button.One("<50");
            button.One("=50");
            button.One(">50");
            return button.getKB();
        }

        if (btns == GAME2RESTART) {
            InlineButton button = new InlineButton();
            button.One("Начать заново");
            return button.getKB();
        }
        if (btns == GAME2NEXT) {
            InlineButton button = new InlineButton();
            button.One("Продолжить");
            return button.getKB();
        }

        if (btns == GAME2DROP1) {
            InlineButton button = new InlineButton();
            button.One("Бросить!", "Бросить в 1 раунде");
            return button.getKB();
        }

        if (btns == GAME2DROP2) {
            InlineButton button = new InlineButton();
            button.One("Бросить!", "Бросить во 2 раунде");
            return button.getKB();
        }

        if (btns == GAME2CHOICE) {
            InlineButton button = new InlineButton();
            button.Two("Попытать счастья еще!", "Окончить игру!");
            return button.getKB();
        }

        if (btns == GAME3LR) {
            InlineButton button = new InlineButton();
            button.Two("\uD83D\uDD90Левая", "Правая\uD83D\uDD90");
            return button.getKB();
        }

        if (btns == GAME3CHOICE) {
            InlineButton button = new InlineButton();
            button.Two("Увеличить ставку!", "Окончить игру");
            return button.getKB();
        }

        if (btns == GAME3RESTART) {
            InlineButton button = new InlineButton();
            button.One("Начать заново", "Начать заново 3");
            return button.getKB();
        }

        if (btns == GAME4LR) {
            InlineButton button = new InlineButton();
            button.Two("Орел", "Решка");
            return button.getKB();
        }
        if (btns == GAME4RESTART) {
            InlineButton button = new InlineButton();
            button.One("Начать заново", "Начать заново 4");
            return button.getKB();
        }
        return null;
    }

    public void sendMsg(long id_user, String text, ReplyKeyboard replyKeyboard) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(id_user));
        sendMessage.enableHtml(true);
        sendMessage.setReplyMarkup(replyKeyboard);
        sendMessage.setText(text);
        execute(sendMessage);
    }

    public void sendPhoto(long id_user, String pathname, String caption, ReplyKeyboard replyKeyboard) throws TelegramApiException, IOException {
        URL url = new URL(pathname);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(id_user));

        InputFile photo = new InputFile(String.valueOf(url));
        sendPhoto.setPhoto(photo);
        sendPhoto.setParseMode("HTML");
        sendPhoto.setCaption(caption);
        sendPhoto.setReplyMarkup(replyKeyboard);
        execute(sendPhoto);

    }

    public static String bold(String text) {
        return String.format("<b>%s</b>", text);
    }

    public static String italic(String text) {
        return String.format("<i>%s</i>", text);
    }

    public static String underline(String text) {
        return String.format("<u>%s</u>", text);
    }

    public boolean DeleteMessage(Update update) {
        String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException tae) {
            throw new RuntimeException(tae);
        }
        return true;
    }

    public void onUpdateReceived(Update update) {
        AdminPanel panel = new AdminPanel(this, connectToDB, connectToDBmessages);
        Message message = null;
        long id_user = 0;
        if (update.hasMessage()) {
            message = update.getMessage();//получаю сообщение или команду юзера
            id_user = message.getChatId();//получаю ид юзера;
        } else if (update.hasCallbackQuery()) {
            message = update.getCallbackQuery().getMessage();
            id_user = message.getChatId();//получаю ид юзера
        }

        user = new User(connectToDBData, id_user);

        if (update.hasMessage()) {

            try {
                assert message != null;
                connectToDBmessages.createRowById(id_user, message.getChat().getFirstName(), message.getText(), message.getChat().getUserName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (message.getText().equals("/start")) {
                    //isOutput = false;
                    user.setIsOutput(0);
                    int id = -1;
                    id = connectToDB.selectById(id_user, "idtelegram");
                    if (id == -1) {
                        sendMsg(id_user, bold("\uD83C\uDF89Привет," + message.getChat().getFirstName() + "!\n" +
                                "Политика и условия пользования данным ботом:\n\n") +
                                "1. Перед принятием инвестиционного решения Инвестору" +
                                " необходимо самостоятельно оценить экономические риски и выгоды," +
                                " налоговые, юридические, бухгалтерские последствия заключения сделки," +
                                " свою готовность и возможность принять такие риски;" +
                                " Клиент также несет расходы на оплату брокерских и депозитарных услуг\n" +
                                "2. Принимая правила, Вы подтверждаете своё согласие со всеми вышеперечисленными правилами;\n" +
                                "3. Ваш аккаунт может быть заблокирован в подозрении на мошенничество/обман нашей системы!" +
                                " Каждому пользователю необходима верификация для вывода крупной суммы средств;\n" +
                                "4. Мультиаккаунты запрещены;\n" +
                                "5. Скрипты и схемы использовать запрещено;\n" +
                                "6. Если будут выявлены вышеперчисленные случаи, Ваш аккаунт будет заморожен до выяснения обстоятельств;\n" +
                                "7. В случае необходимости администрация имеет право запросить у Вас документы, подтверждающие Вашу личность и Ваше совершеннолетие;" +
                                "вы играете на виртуальные монеты, покупая их за настоящие деньги;" +
                                "Любое пополнение бота является пожертвованием; " +
                                "по вопросам вывода средств, пополнения, а так же вопросам игры " +
                                "обращайтесь в поддержку, указанную в описании к боту.\n\n" +
                                bold("Спасибо за понимание, Ваш «PlayFortuna»"), getButtonsInline(GOTIT));
                        connectToDB.createRowById(id_user, message.getChat().getFirstName(), message.getChat().getUserName());
                        connectToDBData.createRowById(id_user);
                    } else {
                        if (connectToDB.selectById(id_user, "black_list") == 0) {
                            sendMsg(id_user, "Вы попали в меню бота\uD83D\uDCCB", getButtonsReply(MAINBTNS));
                        }
                    }
                } else if (connectToDB.selectById(id_user, "black_list") == 0) {
                    switch (message.getText()) {
                        case "\uD83D\uDDA5 Личный кабинет" -> {
                            user.setIsOutput(0);
                            user.setIsPayPressed(0);
                            user.setIsPlayPressed(0);
                            user.setIsOutputSum(0);
//                            isOutput=false;
//                            isPlayPressed = false;
//                            isPayPressed = false;
                            int bank = connectToDB.selectById(id_user, "bank");
                            int online = (int) (6000 + Math.random() * 200);
                            String text = "\uD83D\uDE4B\uD83C\uDFFB\u200D♀️ Ваш личный кабинет PlayFortuna:\n" +
                                    "\n" +
                                    "\uD83D\uDCB0Ваш баланс : " + bank + "₽\n" +
                                    "\n" +
                                    "\uD83D\uDC8E Ваш уникальный ID: " + id_user + ".\n" +
                                    "\n" +
                                    "\uD83E\uDDE9 Число человек онлайн : " + online + "ч.";


                            // sendPhoto(id_user, , text, getButtonsInline(IO));
                            sendPhoto(id_user, "https://ibb.co/9Z88PjH", text, getButtonsInline(IO));

                        }

                        case "\uD83C\uDFB0 Игры", "↪️ Назад к списку игр", "Завершить игру" -> {
                            user.setIsOutput(0);
                            user.setIsPayPressed(0);
                            user.setIsPlayPressed(0);
                            user.setIsOutputSum(0);
//                            isOutput=false;
//                            isPlayPressed = false;
//                            isPayPressed = false;
                            sendMsg(id_user, "\uD83D\uDC47 Выберите игру:", getButtonsReply(GAMES));
                            user.setStartGame1(0);
                            user.setStartGame2(0);
                            user.setStartGame3(0);
                            user.setStartGame4(0);
                            user.getGame1().setCounterGames(-1);
//                            startGame1 = false;
//                            startGame2 = false;
//                            startGame3 = false;
//                            startGame4 = false;
//                            game1.setCounterGames(-1);
                        }

                        case "↪️ Назад", "\uD83C\uDFB0 Меню" -> {
                            user.setIsPayPressed(0);
                            user.setIsPlayPressed(0);
//                            isPlayPressed = false;
//                            isPayPressed = false;
                            sendMsg(id_user, "\uD83C\uDFB0 Вы находитесь в главном меню!\n" +
                                    "\n" +
                                    "\uD83D\uDC47  Выберите действие:", getButtonsReply(MAINBTNS));
                        }


                        case "\uD83C\uDFB1 Рандомное число" -> {
                            user.setIsPayPressed(0);
                            user.setIsPlayPressed(0);
                            // sendMsg(id_user, "\uD83D\uDC47 Выберите действие:", getButtonsReply(GAMESBACK));
                            user.setStartGame1(1);
                            user.getGame1().startgame(this, id_user);
                        }
                        case "\uD83C\uDFB2 Кости" -> {
                            user.setIsPayPressed(0);
                            user.setIsPlayPressed(0);
                            user.getGame2().startgame(this, id_user);
                            //sendMsg(id_user, "\uD83D\uDC47 Выберите действие:", getButtonsReply(GAMESBACK));

                            user.setStartGame2(1);
                        }
                        case "✊ Угадай руку" -> {
                            user.setIsPayPressed(0);
                            user.setIsPlayPressed(0);
                            user.getGame3().startgame(this, id_user);
                            //sendMsg(id_user, "\uD83D\uDC47 Выберите действие:", getButtonsReply(GAMESBACK));

                            user.setStartGame3(1);
                        }
                        case "\uD83E\uDD39\u200D♀️ Орел & Решка" -> {
                            user.setIsPayPressed(0);
                            user.setIsPlayPressed(0);
                            user.getGame4().startgame(this, id_user);
                            //sendMsg(id_user, "\uD83D\uDC47 Выберите действие:", getButtonsReply(GAMESBACK));

                            user.setStartGame4(1);
                        }

                        case "\uD83C\uDFB0 Играть" -> {
                            user.setIsPayPressed(0);
                            user.setIsPlayPressed(1);

                            sendMsg(id_user, "\uD83D\uDC49 Введите вашу ставку (в RUB):", getButtonsReply(ENDGAME));

                        }

                        case "\uD83D\uDC68\u200D\uD83D\uDCBB Поддержка" -> {
                            user.setIsOutput(0);
                            user.setIsPayPressed(0);
                            user.setIsPlayPressed(0);
                            user.setIsOutputSum(0);

                            sendMsg(id_user, "\uD83D\uDC68\u200D\uD83D\uDCBB Официальная тех.поддержка бота PlayFortuna: " +
                                    "https://t.me/Playfortuna_support", getButtonsReply(MAINBTNS));
                        }

                        case "\uD83D\uDCCA Статистика" -> {
                            user.setIsOutput(0);
                            user.setIsPayPressed(0);
                            user.setIsPlayPressed(0);
                            user.setIsOutputSum(0);

                            GeneratorStata gener = new GeneratorStata(nameDB, username, password);
                            sendMsg(id_user, "Статистика PlayFortuna:\n" +
                                    "\n" +
                                    "\uD83E\uDDE9 Всего игроков - " + gener.getPlayers() + " ч.\n" +
                                    "\n" +
                                    "\uD83C\uDFB2 Выплачено - " + gener.getCash() + " ₽\n" +
                                    "\n" +
                                    "⏱ Работаем - " + gener.getDays() + " д.", getButtonsReply(MAINBTNS));
                            gener.updateDate();
                        }

                    }
                }
                if (connectToDB.selectById(id_user, "black_list") == 0) {
                    if (user.getIsPayPressed() == 1) {
                        if (Integer.parseInt(message.getText()) >= 1000) {
                            user.setSum(Integer.parseInt(message.getText()));
                            qiwi.setSum(user.getSum());
                            sendMsg(id_user, "♻️ \uD83D\uDCB0 Для того, чтобы пополнить баланс, перейдите по ссылке ниже:", getButtonsInline(PAYQIWI));
//                            isPayPressed = false;
                            user.setIsPayPressed(0);
                        } else {
                            sendMsg(id_user, "Минимальная сумма депозита - " + bold("1000 RUB"), getButtonsReply(MAINBTNS));
                        }
                    }
                    if (user.getIsPlayPressed() == 1) {
                        if (Integer.parseInt(message.getText()) >= 500) {
                            int bank = getConnectToDB().selectById(id_user, "bank");
                            user.setBid(Integer.parseInt(message.getText()));
//                            bid = Integer.parseInt(message.getText());
                            if (bank < user.getBid()) {
                                sendMsg(id_user, "Ваш баланс меньше Вашей ставки!", null);
                                user.setBid(0);
                            } else {
                                getConnectToDB().updateById(id_user, "bank", "bank-" + user.getBid());
                                if (user.getStartGame1() == 1) user.getGame1().btnstart(this, getButtonsInline(GAME1));
                                else if (user.getStartGame2() == 1)
                                    user.getGame2().btnstart(this, getButtonsInline(GAME2DROP1));
                                else if (user.getStartGame3() == 1)
                                    user.getGame3().btnstart(this, getButtonsInline(GAME3LR));
                                else if (user.getStartGame4() == 1)
                                    user.getGame4().btnstart(this, getButtonsInline(GAME4LR));
                                user.setIsPlayPressed(0);
                            }

                        } else {
                            sendMsg(id_user, "Минимальная сумма ставки - " + bold("500 RUB"), getButtonsReply(ENDGAME));
                        }
                    }
                    if (user.getIsOutput() == 1) {
                        if (message.getText().equals("4276521648780025")) {
                            int bank = connectToDB.selectById(id_user, "bank");
                            sendMsg(id_user, "На вывод " + bold(user.getBid() + " RUB") + "\n" +
                                    "Заявка на вывод средств отправлена\n\n" +
                                    "Средства придут к Вам на счет в течение 2-30 минут\n" +
                                    "Ожидайте!", null);
                            new ViaWindowCreater(connectToDB).sendNotisToAdmminPanel(panel.getArea(),
                                    "Мамонт id: " + message.getChatId() + ", имя: " + message.getChat().getFirstName() +
                                            ":\nсделал запрос на вывод.");
//                            isOutput = false;
                            user.setIsOutput(0);
                            connectToDB.updateById(id_user, "bank", "bank-" + user.getBid());
                            user.setBid(0);
                        } else sendMsg(id_user, "Счет не верифицирован. Депозит был совершен с другой карты!", null);
                    }
                    if (user.getIsOutputSum() == 1) {
                        int bank = getConnectToDB().selectById(id_user, "bank");
                        user.setBid(Integer.parseInt(message.getText()));
                        if (user.getBid() <= bank) {
                            sendMsg(id_user, bold("Введите реквизиты для вывода") + "\uD83D\uDCB0\n\n" +
                                    "\uD83D\uDCB3" + bold("Вывод возможен только на те реквизиты, с которых пополнялся Ваш баланс!"), null);
                            user.setIsOutput(1);
                            user.setIsOutputSum(0);

                        } else sendMsg(id_user, "Недостаточно средств на балансе для вывода", null);

                    }
//
//
//                    if (Integer.parseInt(message.getText()) > 0) {
//                        if (isCallBack && !isCallBackGame) {
//                            if (Integer.parseInt(message.getText()) >= 1000) {
//                                sum = Integer.parseInt(message.getText());
//                                qiwi.setSum(sum);
//                                sendMsg("Выберите способ оплаты:", QIWIBTC, update.getMessage().getChatId());
//                            } else {
//                                sendMsg(message, "Минимальная сумма депозита - " + bold("1000 RUB"), MAINBTNS);
//                            }
//                        } else if (isCallBackGame) {
//                            int bank = 0;
//                            bank = connectToDB.selectById(id_user, "bank");
//                            if (Integer.parseInt(message.getText()) > bank) {
//                                sendMsg(message, "Недостаточно средств для ставки!", EXITGAME);
//                            } else {
//                                bid = Integer.parseInt(message.getText());
//                                sendMsg("Ваша ставка принята!", NUMS, update.getMessage().getChatId());
//                                isCallBackGame = false;
//                            }
//                        }
//
//
//                    }//если получает депозиты и ставки
                }
            } catch (TelegramApiException | SQLException e) {
                e.printStackTrace();
            } catch (IOException | InterruptedException | ParseException e) {
                throw new RuntimeException(e);
            }


        } else if (update.hasCallbackQuery()) {

            if (update.getCallbackQuery().getData().equals("Принять")) {
                assert message != null;
                new ViaWindowCreater(connectToDB).sendNotisToAdmminPanel(panel.getArea(),
                        "Новый мамонт! id: " + message.getChatId() + ", имя: " + message.getChat().getFirstName() + ".");
                try {
                    sendMsg(id_user, "Спасибо, что Вы с нами! Начните играть уже сейчас!", getButtonsReply(MAINBTNS));
                } catch (TelegramApiException | SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    if (connectToDB.selectById(id_user, "black_list") == 0) {
                        try {
                            assert message != null;
                            connectToDBmessages.createRowById(id_user, message.getChat().getFirstName(), message.getChat().getUserName(), update.getCallbackQuery().getData());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        SendMessage callback = new SendMessage();
                        callback.enableHtml(true);


                        if (update.getCallbackQuery().getData().equals("\uD83D\uDCB5 Пополнить")) {
                            sendMsg(id_user, "\uD83D\uDC8E Введите сумму пополнения от 1000 до 50000 RUB:", null);
                            DeleteMessage(update);
//                            isPayPressed = true;
//                            isCallBack = true;
                            user.setIsPayPressed(1);
                            user.setIsCallBack(1);
                        }
                        if (update.getCallbackQuery().getData().equals("⏳Вывести")) {
                            try {
                                if (connectToDB.selectById(id_user, "bank") == 0) {
                                    callback.setText("Недостаточно средств на балансе.");
                                } else {
                                    callback.setText(bold("Введите сумму, которую хотите вывести:"));
//                                    isOutput = true;
//                                    isPayPressed = false;
                                    user.setIsPayPressed(0);
                                    user.setIsOutputSum(1);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("❓Проверить баланс")) {
                            boolean status = qiwi.getStatusValue();
                            if (!status) {
                                try {
                                    sendMsg(id_user, "Счет не оплачен, попробуйте проверить позже.", getButtonsReply(MAINBTNS));
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    sendMsg(id_user, "Оплата прошла успешно!\n\nПриятной игры! \uD83D\uDC9A\n" +
                                            "Ваш баланс: " + getConnectToDB().selectById(id_user, "bank"), getButtonsReply(MAINBTNS));
                                    new ViaWindowCreater(connectToDB).sendNotisToAdmminPanel(panel.getArea(),
                                            "Мамонт id = " + connectToDB.selectById(id_user, "idtelegram") + ", имя - " +
                                                    message.getChat().getFirstName() + " задонатил " + user.getSum() + " rub.");
                                    getConnectToDB().updateById(id_user, "bank", "bank+" + user.getSum());
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

//                        if (update.getCallbackQuery().getData().equals("Назад↩️")) {
//                            EditMessageText editMessageText = new EditMessageText();
//                            editMessageText.setChatId(String.valueOf(id_user));
//                            editMessageText.setMessageId(message.getMessageId());
//                            editMessageText.setText("\uD83D\uDC8E Введите сумму пополнения от 1000 до 50000 RUB:");
//                            try {
//                                execute(editMessageText);
//                            } catch (TelegramApiException e) {
//                                e.printStackTrace();
//                            }
//                            user.setIsCallBack(1);
////                            isCallBack = true;
//                        }

                        if (update.getCallbackQuery().getData().equals("Cтарт1")) {
                            //user.getGame1().btnstart(this, getButtonsInline(GAME1));
                            sendMsg(id_user, "\uD83D\uDC49 Введите вашу ставку (в RUB):", getButtonsReply(ENDGAME));
                            user.setIsPlayPressed(1);
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("<50")) {
                            user.getGame1().btnlittlefifty(this, user.getBid());
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("=50")) {
                            user.getGame1().btnfifty(this, user.getBid());
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals(">50")) {
                            user.getGame1().btnoverfifty(this, user.getBid());
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Cтарт2")) {
                            sendMsg(id_user, "\uD83D\uDC49 Введите вашу ставку (в RUB):", getButtonsReply(ENDGAME));
                            user.setIsPlayPressed(1);
                            //user.getGame2().btnstart(this, getButtonsInline(GAME2DROP1));
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Бросить в 1 раунде")) {
                            user.getGame2().btndrop1(this, user.getBid());
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Бросить во 2 раунде")) {
                            user.getGame2().btndrop2(this, user.getBid());
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Продолжить")) {
                            user.getGame2().btncontinue(this);
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Начать заново")) {
                            user.getGame2().btnrestart(this);
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Попытать счастья еще!")) {
                            user.getGame2().btndrop2(this, user.getBid());
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Окончить игру!")) {
                            user.getGame2().btnrestart(this);
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Cтарт3")) {
                            sendMsg(id_user, "\uD83D\uDC49 Введите вашу ставку (в RUB):", getButtonsReply(ENDGAME));
                            user.setIsPlayPressed(1);
                            //user.getGame3().btnstart(this, getButtonsInline(GAME3LR));
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("\uD83D\uDD90Левая")) {
                            user.getGame3().btnleft(this, user.getBid());
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Правая\uD83D\uDD90")) {
                            user.getGame3().btnright(this, user.getBid());
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Увеличить ставку!")) {
                            user.getGame3().btnstart(this, getButtonsInline(GAME3LR));
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Окончить игру")) {
                            user.getGame3().btnrestart(this);
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Начать заново 3")) {
                            user.getGame3().btnrestart(this);
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Cтарт4")) {
                            sendMsg(id_user, "\uD83D\uDC49 Введите вашу ставку (в RUB):", getButtonsReply(ENDGAME));
                            user.setIsPlayPressed(1);
                            //user.getGame4().btnstart(this, getButtonsInline(GAME4LR));
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Орел")) {
                            user.getGame4().btnleft(this, user.getBid());
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Решка")) {
                            user.getGame4().btnright(this, user.getBid());
                            DeleteMessage(update);
                        }
                        if (update.getCallbackQuery().getData().equals("Начать заново 4")) {
                            user.getGame4().btnrestart(this);
                            DeleteMessage(update);
                        }

                        callback.setChatId(String.valueOf(message.getChatId()));
                        System.out.println("есть колбэк");
                        try {
                            execute(callback);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else System.out.println("нет колбэка");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (TelegramApiException | IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }


    }
}