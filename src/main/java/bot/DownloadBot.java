package bot;

import bot.commands.PermissionsChecker;
import bot.commands.StartCommand;
import db.AnalyticsApi;
import dto.Type;
import http.DownloadCommandProvider;
import http.UrlValidator;
import http.YoutubeValidator;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import util.PropertiesProvider;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DownloadBot extends TelegramLongPollingCommandBot {

    public static List<Long> allowedUsers = PropertiesProvider.getAllowedUsers();

    public DownloadBot(DefaultBotOptions botOptions) {
        super(botOptions);
        register(new StartCommand());
    }

    @Override
    public String getBotUsername() {
        return PropertiesProvider.configurationProperties.get("BotName");
    }

    @Override
    public String getBotToken() {
        return PropertiesProvider.configurationProperties.get("BotToken");
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasCallbackQuery()) {
            processCallbackQuery(update);
        }

        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            Long userId = update.getMessage().getFrom().getId();
            Integer messageId = update.getMessage().getMessageId();
            AnalyticsApi.createEvent(userId, messageId.toString(), "", update.getMessage().getText(), "");
            if (PermissionsChecker.isAllowed(userId))
                processMessage(update);
            else
                sendMessage(chatId, userId, messageId + 1, ReplyConstants.NOT_ALLOWED, false, null);
        }
    }

    @Override
    public void processInvalidCommandUpdate(Update update) {
        super.processInvalidCommandUpdate(update);
    }

    @Override
    public boolean filter(Message message) {
        return super.filter(message);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    private void processCallbackQuery(Update update) {
        AnalyticsApi.createEvent(update.getCallbackQuery().getFrom().getId(), update.getCallbackQuery().getMessage().getMessageId().toString(), "", "", update.getCallbackQuery().getData());
    }

    private void processMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();
        Integer messageId = update.getMessage().getMessageId();

        //Get and validate URL
        String url = update.getMessage().getText();
        if (UrlValidator.isUrlValid(url)) {
            Type type = UrlValidator.detectType(url);
            int msgId = sendMessageAndGetId(chatId, userId, messageId + 1, ReplyConstants.TRYING_TO_DOWNLOAD, false, null);

            //Run downloading process
            String command = "";
            if (type == Type.YOUTUBE)
                command = DownloadCommandProvider.buildDownloadYoutubeCommand(YoutubeValidator.cleanUrl(url));
            else if (type == Type.INSTAGRAM) {
                command = DownloadCommandProvider.buildDownloadInstagramCommand(url);
            }
            String currentPath = "";
            List<String> output = new ArrayList<>();
            AnalyticsApi.createEvent(userId, "", "bot", "", command);

            try {
                currentPath = new File(".").getCanonicalPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                output = runCommand(new File(currentPath + "/yt"), command);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //Filter downloaded files and errors and respond back
            List<String> files = new ArrayList<>();
            if (!output.isEmpty()) {
                for (String s: output) {
                    if (s.startsWith("/Users/srs/storage/"))
                        files.add(s);
                }
                if (!files.isEmpty()) {
                    editMessage(chatId, msgId, userId, String.format(ReplyConstants.SUCCESSFULLY_DOWNLOADED, files.size()), false, null);
                    for (int i = 0; i< files.size(); i++) {
                        sendDocument(chatId, userId, messageId + i + 2, files.get(i));
                    }
                } else if (output.get(output.size() - 1).contains("Instagram sent an empty media response") || output.get(output.size() - 1).contains("No video")) {
                    editMessage(chatId, msgId, userId, ReplyConstants.NO_VIDEO_FOUND, false, null);
                } else {
                    editMessage(chatId, msgId, userId, ReplyConstants.ERROR_OCCURRED_WHILE_DOWNLOADING, false, null);
                }
            } else {
                editMessage(chatId, msgId, userId, ReplyConstants.ERROR_OCCURRED_WHILE_DOWNLOADING, false, null);
            }
        } else {
            sendMessage(chatId, userId, messageId + 1, ReplyConstants.LINK_IS_INCORRECT + ReplyConstants.GIVE_ME_THE_LINK, false, null);
        }
    }

    public static List<String> runCommand(File whereToRun, String command) throws Exception {
        System.out.println("Running in: " + whereToRun);
        System.out.println("Command: " + command);

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(whereToRun);
        builder.command("sh", "-c", command);

        Process process = builder.start();

        List<String> outputLines = Collections.synchronizedList(new ArrayList<>());
        List<String> errorLines = Collections.synchronizedList(new ArrayList<>());

        Thread stdoutThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    outputLines.add(line);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Thread stderrThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println(line);
                    errorLines.add(line);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        stdoutThread.start();
        stderrThread.start();

        boolean isFinished = process.waitFor(300, TimeUnit.SECONDS);

        if (!isFinished) {
            process.destroyForcibly();

            stdoutThread.join();
            stderrThread.join();
            return new ArrayList<>();
        }

        if (outputLines.isEmpty()) {
            if (!errorLines.isEmpty())
                return errorLines;
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();
        for (String s: outputLines) {
            if (s.startsWith("/Users/srs/storage/"))
                result.add(s);
        }
        return result;
    }


    private void sendMessage(long chatId, long userId, Integer messageId, String text, boolean htmlParseMode, InlineKeyboardMarkup keyboard) {
        AnalyticsApi.createEvent(userId, messageId.toString(), "bot", text, "");
        SendMessage sm = new SendMessage();
        sm.setChatId(Long.toString(chatId));
        sm.setText(text);
        if (keyboard != null)
            sm.setReplyMarkup(keyboard);
        if (htmlParseMode)
            sm.setParseMode(ParseMode.HTML);
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private int sendMessageAndGetId(long chatId, long userId, Integer analyticsMessageId, String text, boolean htmlParseMode, InlineKeyboardMarkup keyboard) {
        AnalyticsApi.createEvent(userId, analyticsMessageId.toString(), "bot", text, "");
        int messageId = 0;
        SendMessage sm = new SendMessage();
        sm.setChatId(Long.toString(chatId));
        sm.setText(text);
        if (keyboard != null)
            sm.setReplyMarkup(keyboard);
        if (htmlParseMode)
            sm.setParseMode(ParseMode.HTML);
        try {
            messageId = execute(sm).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return messageId;
    }

    private void editMessage(long chatId, Integer messageId, long userId, String text, boolean htmlParseMode, InlineKeyboardMarkup keyboard) {
        AnalyticsApi.createEvent(userId, messageId.toString(), "bot", text, "");
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(text);
        if (keyboard != null)
            editMessageText.setReplyMarkup(keyboard);
        if (htmlParseMode)
            editMessageText.setParseMode(ParseMode.HTML);
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(Long.toString(chatId));
        deleteMessage.setMessageId(messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendDocument(long chatId, long userId, Integer messageId, String filename) {
        AnalyticsApi.createEvent(userId, messageId.toString(), "bot", filename, "");
        File file = new File(filename);
        InputFile inputFile = new InputFile(file);
        SendDocument document = new SendDocument();
        document.setDisableContentTypeDetection(true);
        document.setChatId(chatId);
        document.setDocument(inputFile);
        document.setCaption(file.getName());
        try {
            execute(document);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
