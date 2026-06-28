package bot;

import bot.commands.PermissionsChecker;
import bot.commands.StartCommand;
import db.AnalyticsApi;
import http.UrlValidator;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
            AnalyticsApi.createEvent(update.getMessage().getFrom().getId(), update.getMessage().getMessageId().toString(), "", update.getMessage().getText(), "");
            if (PermissionsChecker.isAllowed(update.getMessage().getFrom().getId()))
                processMessage(update);
            else
                sendMessage(update.getMessage().getChatId(), ReplyConstants.NOT_ALLOWED, false, null);
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

        //Get and validate URL
        String url = update.getMessage().getText();
        if (UrlValidator.isYoutubeVideo(url)) {
            int msgId = sendMessageAndGetId(update.getMessage().getChatId(), ReplyConstants.TRYING_TO_DOWNLOAD, false, null);

            //Run downloading process
            String currentPath = "";
            String filename = "";
            String command = MessageFormat.format("./yt-dlp_macos \"{0}\" -P \"~/storage\" -o \"%(id)s.%(ext)s\"  --print after_move:filepath", url);
            try {
                currentPath = new File(".").getCanonicalPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                filename = runCommand(new File(currentPath + "/yt"), command);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //Get the result and respond back
            if (!filename.isEmpty()) {
                editMessage(update.getMessage().getChatId(), msgId, ReplyConstants.SUCCESSFULLY_DOWNLOADED, false, null);
                sendDocument(update.getMessage().getChatId(), filename);
            } else {
                editMessage(update.getMessage().getChatId(), msgId, ReplyConstants.ERROR_OCCURRED_WHILE_DOWNLOADING, false, null);
            }
        } else {
            sendMessage(update.getMessage().getChatId(), ReplyConstants.LINK_IS_INCORRECT, false, null);
        }
    }

    public static String runCommand(File whereToRun, String command) throws Exception {
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
            return "";
        }

        if (outputLines.isEmpty()) {
            return "";
        }

        return outputLines.get(outputLines.size() - 1);
    }


    private void sendMessage(long chatId, String text, boolean htmlParseMode, InlineKeyboardMarkup keyboard) {
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

    private int sendMessageAndGetId(long chatId, String text, boolean htmlParseMode, InlineKeyboardMarkup keyboard) {
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

    private void editMessage(long chatId, int messageId, String text, boolean htmlParseMode, InlineKeyboardMarkup keyboard) {
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

    private void sendDocument(long chatId, String filename) {
        File file = new File(filename);
        InputFile inputFile = new InputFile(file);
        SendDocument document = new SendDocument();
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
