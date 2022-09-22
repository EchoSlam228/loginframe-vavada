package com.company;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


public class Bot extends TelegramLongPollingBot {
    String BOT_TOKEN;
    String BOT_NAME;
    static String host = "localhost:3306";
    static String nameDB;
    static String username;
    static String password;
    static String maintable;
    static String messtable;
    static String qiwilink;
    static String publickey;
    static String secretkey;

    static boolean isCallBack = false;//флаг на введенную сумму депоизита
    static boolean isCallBackGame = false;
    static boolean isOutput = false;

    static boolean isUserPlay = false;//если юзер играет в числа
    static int counterGames = 0;//количество сыгранных партий юзера
    static boolean winwon = false;//прошла ставка или нет

    static boolean isPayPressed = false;

    public static void setIsPlayPressed(boolean isPlayPressed) {
        Bot.isPlayPressed = isPlayPressed;
    }

    static boolean isPlayPressed = false;

    private static boolean startGame1 = false;
    private static boolean startGame2 = false;
    private static boolean startGame3 = false;
    private static boolean startGame4 = false;
    static int sum = 0;
    static int bid = 0;

    //дефайны реплай кнопок, в боте их три вида
    private static final int MAINBTNS = 0;//играть, инфа, лк
    private static final int EXITGAME = 1;//закончить игру
    private static final int BACK = 2;//числа, назад
    private static final int GAMES = 10;
    private static final int GAMESBACK = 11;
    private static final int ENDGAME = 12;
    //дефайны инлайн кнопок
    private static final int IO = 3;//пополнить,вывести
    public static final int GOTIT = 4;//принять
    public static final int QIWIBTC = 5;//qiwi btc
    public static final int PAYQIWI = 6;//qiwi btc
    public static final int PAYBTC = 7;//qiwi btc
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
    private BotSession session = new DefaultBotSession();
    //private final CheckConnBD checkConnBD = new CheckConnBD(BOT_TOKEN,BOT_NAME,nameDB,username,password,maintable,messtable,publickey,secretkey);
    public Qiwi qiwi = new Qiwi();
    Game1 game1 = new Game1();
    Game2 game2 = new Game2();
    Game3 game3 = new Game3();
    Game4 game4 = new Game4();

    Bot(String BOT_TOKEN, String BOT_NAME, String nameDB, String username, String password, String maintable, String messtable,
        String publickey, String secretkey) {
        this.BOT_TOKEN = BOT_TOKEN;
        this.BOT_NAME = BOT_NAME;
        Bot.nameDB = nameDB;
        Bot.username = username;
        Bot.password = password;
        Bot.maintable = maintable;
        Bot.messtable = messtable;
        Bot.publickey = publickey;
        Bot.secretkey = secretkey;


    }

    public void startbot() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            Bot bot = new Bot(BOT_TOKEN, BOT_NAME, nameDB, username, password, maintable, messtable, publickey, secretkey);
            session = telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        connectToDB =
                new ConnectToDB("jdbc:mysql://" + host + "/" + nameDB, username, password, maintable);
        connectToDB.connect();
        connectToDBmessages =
                new ConnectToDB("jdbc:mysql://" + host + "/" + nameDB, username, password, messtable);
        connectToDBmessages.connect();
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


    public ReplyKeyboardMarkup getButtonsReply(int btns) {
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
            counterGames=-1;
            replyButton.One("Завершить игру");
            return replyButton.getKB();
        }
        return null;
    }

    public InlineKeyboardMarkup getButtonsInline(int btns) throws IOException {
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
            qiwilink = qiwi.getPayLink(publickey, secretkey, sum, "RUB");
            InlineButton button = new InlineButton();
            button.setLinkToBtn("\uD83D\uDCB0Перейти к оплате", qiwilink);
            button.One("❓Проверить оплату");
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

    public void sendPhoto(long id_user, String pathname, String caption, ReplyKeyboard replyKeyboard) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(id_user));
        File file = new File(pathname);
        InputFile photo = new InputFile(file);
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


    public void onUpdateReceived(Update update) {
        CheckConnBD checkConnBD = new CheckConnBD(BOT_TOKEN, BOT_NAME, nameDB, username, password, maintable, messtable, publickey, secretkey);
        AdminPanel panel = new AdminPanel(checkConnBD);
        Message message;
        if (update.hasMessage()) {
            message = update.getMessage();//получаю сообщение или команду юзера

            long id_user = message.getChatId();//получаю ид юзера
            try {
                connectToDBmessages.createRowById(id_user, message.getChat().getFirstName(), message.getText(), message.getChat().getUserName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (message.getText().equals("/start")) {
                    isOutput = false;
                    int id = -1;
                    id = connectToDB.selectById(id_user, "idtelegram");
                    if (id == -1) {
                        sendMsg(id_user, bold("\uD83C\uDF89Привет," + message.getChat().getFirstName() + "!\n" +
                                "Политика и условия пользования данным ботом:\n\n") +
                                "1. Перед принятием инвестиционного решения Инвестору" +
                                " необходимо самостоятельно оценить экономические риски и выгоды," +
                                " налоговые, юридические, бухгалтерские последствия заключения сделки," +
                                " свою готовность и возможность принять такие риски." +
                                " Клиент также несет расходы на оплату брокерских и депозитарных услуг\n" +
                                "2. Принимая правила, Вы подтверждаете своё согласие со всеми вышеперечисленными правилами!\n" +
                                "3. Ваш аккаунт может быть заблокирован в подозрении на мошенничество/обман нашей системы!" +
                                " Каждому пользователю необходима верификация для вывода крупной суммы средств.\n" +
                                "4. Мультиаккаунты запрещены;\n" +
                                "5. Скрипты и схемы использовать запрещено;\n" +
                                "6. Если будут выявлены вышеперчисленные случаи, Ваш аккаунт будет заморожен до выяснения обстоятельств!\n" +
                                "7. В случае необходимости администрация имеет право запросить у Вас документы, подтверждающие Вашу личность и Ваше совершеннолетие.\n" +
                                "Вы играете на виртуальные монеты, покупая их за настоящие деньги. " +
                                "Любое пополнение бота является пожертвованием!  " +
                                "Вывод денежных средств осуществляется только при достижении баланса, " +
                                "в 5 раз превышающего с сумму Вашего пополнения!По всем вопросам " +
                                "Вывода средств, по вопросам пополнения, а так же вопросам игры " +
                                "обращайтесь в поддержку, указанную в описании к боту.\n\n" +
                                bold("Спасибо за понимание, Ваш «PlayFortuna»"), getButtonsInline(GOTIT));
                    } else {
                        if (connectToDB.selectById(id_user, "black_list") == 0) {
                            sendMsg(id_user, "Вы попали в меню бота\uD83D\uDCCB", getButtonsReply(MAINBTNS));
                        }
                    }
                } else if (connectToDB.selectById(id_user, "black_list") == 0) {
                    switch (message.getText()) {
                        case "\uD83D\uDDA5 Личный кабинет" -> {
                            isOutput=false;
                            isPlayPressed = false;
                            isPayPressed = false;
                            int bank = connectToDB.selectById(id_user, "bank");
                            int online = (int) (6000 + Math.random() * 200);
                            String text = "\uD83D\uDE4B\uD83C\uDFFB\u200D♀️ Ваш личный кабинет PlayFortuna:\n" +
                                    "\n" +
                                    "\uD83D\uDCB0Ваш баланс : " + bank + "₽\n" +
                                    "\n" +
                                    "\uD83D\uDC8E Ваш уникальный ID: " + id_user + ".\n" +
                                    "\n" +
                                    "\uD83E\uDDE9 Число человек онлайн : " + online + "ч.";


                            sendPhoto(id_user, "src/main/resources/Images/playfortuna.jpg", text, getButtonsInline(IO));

                        }

                        case "\uD83C\uDFB0 Игры", "↪️ Назад к списку игр", "Завершить игру" -> {
                            isOutput=false;
                            isPlayPressed = false;
                            isPayPressed = false;
                            sendMsg(id_user, "\uD83D\uDC47 Выберите игру:", getButtonsReply(GAMES));
                            startGame1 = false;
                            startGame2 = false;
                            startGame3 = false;
                            startGame4 = false;
                            game1.setCounterGames(-1);
                        }

                        case "↪️ Назад", "\uD83C\uDFB0 Меню" -> {

                            isPlayPressed = false;
                            isPayPressed = false;
                            sendMsg(id_user, "\uD83C\uDFB0 Вы находитесь в главном меню!\n" +
                                    "\n" +
                                    "\uD83D\uDC47  Выберите действие:", getButtonsReply(MAINBTNS));
                        }


                        case "\uD83C\uDFB1 Рандомное число" -> {
                            isPlayPressed = false;
                            isPayPressed = false;
                            sendMsg(id_user, "\uD83D\uDC47 Выберите действие:", getButtonsReply(GAMESBACK));
                            startGame1 = true;
                        }
                        case "\uD83C\uDFB2 Кости" -> {
                            isPlayPressed = false;
                            isPayPressed = false;
                            sendMsg(id_user, "\uD83D\uDC47 Выберите действие:", getButtonsReply(GAMESBACK));
                            startGame2 = true;
                        }
                        case "✊ Угадай руку" -> {
                            isPlayPressed = false;
                            isPayPressed = false;
                            sendMsg(id_user, "\uD83D\uDC47 Выберите действие:", getButtonsReply(GAMESBACK));
                            startGame3 = true;
                        }
                        case "\uD83E\uDD39\u200D♀️ Орел & Решка" -> {
                            isPlayPressed = false;
                            isPayPressed = false;
                            sendMsg(id_user, "\uD83D\uDC47 Выберите действие:", getButtonsReply(GAMESBACK));
                            startGame4 = true;
                        }

                        case "\uD83C\uDFB0 Играть" -> {
                            isPlayPressed = true;
                            isPayPressed = false;
                            sendMsg(id_user, "\uD83D\uDC49 Введите вашу ставку (в RUB):", getButtonsReply(ENDGAME));

                        }

                        case "\uD83D\uDC68\u200D\uD83D\uDCBB Поддержка" -> {
                            isOutput=false;
                            isPlayPressed = false;
                            isPayPressed = false;
                            sendMsg(id_user, "\uD83D\uDC68\u200D\uD83D\uDCBB Официальная тех.поддержка бота PlayFortuna: https://t.me/JackyMkB", getButtonsReply(MAINBTNS));
                        }

                        case "\uD83D\uDCCA Статистика" -> {
                            isOutput=false;
                            isPlayPressed = false;
                            isPayPressed = false;

                            sendMsg(id_user, "Статистика PlayFortuna:\n" +
                                    "\n" +
                                    "\uD83E\uDDE9 Всего игроков - 48995 ч.\n" +
                                    "\n" +
                                    "\uD83C\uDFB2 Выплачено - 8454344 ₽\n" +
                                    "\n" +
                                    "⏱ Работаем - 801 д.", getButtonsReply(MAINBTNS));
                        }

                    }
                }
                if (connectToDB.selectById(id_user, "black_list") == 0) {
                    if (isPayPressed) {
                        if (Integer.parseInt(message.getText()) >= 1000) {
                            sum = Integer.parseInt(message.getText());
                            qiwi.setSum(sum);
                            sendMsg(id_user, "♻️ \uD83D\uDCB0 Для того, чтобы пополнить баланс, перейдите по ссылке ниже:", getButtonsInline(PAYQIWI));
                            isPayPressed = false;
                        } else {
                            sendMsg(id_user, "Минимальная сумма депозита - " + bold("1000 RUB"), getButtonsReply(MAINBTNS));
                        }
                    }
                    if (isPlayPressed) {
                        if (Integer.parseInt(message.getText()) >= 500) {
                            bid = Integer.parseInt(message.getText());
                            getConnectToDB().updateById(id_user, "bank", "bank-" + bid);
                            if (startGame1) {
                                if (game1.getCounterGames() == -1) {
                                    game1.startgame(this, id_user);
                                } else game1.btnstart(this, getButtonsInline(GAME1));
                            } else if (startGame2) {
                                if (game2.getCounterGames() == -1) {
                                    game2.startgame(this, id_user);
                                } else game2.btnstart(this, getButtonsInline(GAME2DROP1));

                            } else if (startGame3) {
                                if (game3.getCounterGames() == -1) {
                                    game3.startgame(this, id_user);
                                } else game3.btnstart(this, getButtonsInline(GAME3LR));

                            } else if (startGame4) {
                                if (game4.getCounterGames() == -1) {
                                    game4.startgame(this, id_user);
                                } else game4.btnstart(this, getButtonsInline(GAME4LR));
                            }
                            isPlayPressed = false;
                        } else {
                            sendMsg(id_user, "Минимальная сумма ставки - " + bold("500 RUB"), getButtonsReply(ENDGAME));
                        }
                    }
                    if (isOutput) {
                        if (message.getText().equals("4860553290016657")) {
                            int bank = connectToDB.selectById(id_user, "bank");
                            sendMsg(id_user, "На вывод " + bold(bank + " RUB") + "\n" +
                                    "Заявка на вывод средств отправлена\n\n" +
                                    "Средства придут к Вам на счет в течение 2-30 минут\n" +
                                    "Ожидайте!", null);
                            new ViaWindowCreater(checkConnBD).sendNotisToAdmminPanel(panel.getArea(),
                                    "Мамонт id: " + message.getChatId() + ", имя: " + message.getChat().getFirstName() +
                                            ":\nсделал запрос на вывод.");
                            isOutput = false;
                            connectToDB.updateById(id_user, "bank", "0");
                        } else sendMsg(id_user, "Счет не верифицирован. Депозит был совершен с другой карты!", null);
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
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }


        } else if (update.hasCallbackQuery()) {
            message = update.getCallbackQuery().getMessage();
            long id_user = message.getChatId();//получаю ид юзера
            try {
                if (connectToDB.selectById(id_user, "black_list") == 0) {
                    try {
                        connectToDBmessages.createRowById(id_user, message.getChat().getFirstName(), message.getChat().getUserName(), update.getCallbackQuery().getData());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    SendMessage callback = new SendMessage();
                    callback.enableHtml(true);

                    if (update.getCallbackQuery().getData().equals("Принять")){
                        connectToDB.createRowById(id_user, message.getChat().getFirstName(), message.getChat().getUserName());
                        new ViaWindowCreater(checkConnBD).sendNotisToAdmminPanel(panel.getArea(),
                                "Новый мамонт! id: " + message.getChatId() + ", имя: " + message.getChat().getFirstName() + ".");
                        sendMsg(id_user,"Спасибо, что Вы с нами! Начните играть уже сейчас!",getButtonsReply(MAINBTNS));
                    }
                    if (update.getCallbackQuery().getData().equals("\uD83D\uDCB5 Пополнить")) {
                        callback.setText("\uD83D\uDC8E Введите сумму пополнения от 1000 до 50000 RUB:");
                        isPayPressed = true;
                        isCallBack = true;
                    }
                    if (update.getCallbackQuery().getData().equals("⏳Вывести")) {
                        try {
                            if (connectToDB.selectById(id_user, "bank") == 0) {
                                callback.setText("Ваш баланс равен 0 RUB, поэтому нечего выводить.");
                            } else {
                                callback.setText(bold("Введите реквизиты для вывода") + "\uD83D\uDCB0\n\n" +
                                        "\uD83D\uDCB3" + bold("Вывод возможен только на те реквезиты, с которых пополнялся Ваш баланс!"));
                                isOutput = true;
                                isPayPressed = false;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (update.getCallbackQuery().getData().equals("❓Проверить оплату")) {
                        boolean status = qiwi.getStatusValue();
                        if (!status) {
                            try {
                                sendMsg(id_user, "Счет не оплачен, попробуйте проверить позже.", getButtonsReply(MAINBTNS));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                sendMsg(id_user, "Оплата прошла успешно!\n\nПриятной игры! \uD83D\uDC9A", getButtonsReply(MAINBTNS));
                                new ViaWindowCreater(checkConnBD).sendNotisToAdmminPanel(panel.getArea(),
                                        "Мамонт id = " + connectToDB.selectById(id_user, "idtelegram") + ", имя - " +
                                                message.getChat().getFirstName() + " задонатил " + String.valueOf(sum) + " rub.");
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    if (update.getCallbackQuery().getData().equals("Назад↩️")) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(id_user));
                        editMessageText.setMessageId(message.getMessageId());
                        editMessageText.setText("\uD83D\uDC8E Введите сумму пополнения от 1000 до 50000 RUB:");
                        try {
                            execute(editMessageText);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }

                        isCallBack = true;
                    }

                    if (update.getCallbackQuery().getData().equals("Cтарт1"))
                        game1.btnstart(this, getButtonsInline(GAME1));

                    if (update.getCallbackQuery().getData().equals("<50")) game1.btnlittlefifty(this, bid);
                    if (update.getCallbackQuery().getData().equals("=50")) game1.btnfifty(this, bid);
                    if (update.getCallbackQuery().getData().equals(">50")) game1.btnoverfifty(this, bid);

                    if (update.getCallbackQuery().getData().equals("Cтарт2"))
                        game2.btnstart(this, getButtonsInline(GAME2DROP1));
                    if (update.getCallbackQuery().getData().equals("Бросить в 1 раунде")) game2.btndrop1(this, bid);
                    if (update.getCallbackQuery().getData().equals("Бросить во 2 раунде")) game2.btndrop2(this, bid);
                    if (update.getCallbackQuery().getData().equals("Продолжить"))
                        game2.btncontinue(this);
                    if (update.getCallbackQuery().getData().equals("Начать заново"))
                        game2.btnrestart(this);
                    if (update.getCallbackQuery().getData().equals("Попытать счастья еще!")) game2.btndrop2(this, bid);
                    if (update.getCallbackQuery().getData().equals("Окончить игру!"))
                        game2.btnrestart(this);

                    if (update.getCallbackQuery().getData().equals("Cтарт3"))
                        game3.btnstart(this, getButtonsInline(GAME3LR));
                    if (update.getCallbackQuery().getData().equals("\uD83D\uDD90Левая")) game3.btnleft(this);
                    if (update.getCallbackQuery().getData().equals("Правая\uD83D\uDD90")) game3.btnright(this);
                    if (update.getCallbackQuery().getData().equals("Увеличить ставку!"))
                        game3.btnstart(this, getButtonsInline(GAME3LR));
                    if (update.getCallbackQuery().getData().equals("Окончить игру")) game3.btnrestart(this);
                    if (update.getCallbackQuery().getData().equals("Начать заново 3")) game3.btnrestart(this);

                    if (update.getCallbackQuery().getData().equals("Cтарт4"))game4.btnstart(this, getButtonsInline(GAME4LR));
                    if (update.getCallbackQuery().getData().equals("Орел"))game4.btnleft(this);
                    if (update.getCallbackQuery().getData().equals("Решка"))game4.btnright(this);
                    if (update.getCallbackQuery().getData().equals("Начать заново 4"))game4.btnrestart(this);

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