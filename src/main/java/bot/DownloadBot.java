package bot;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import util.PropertiesProvider;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DownloadBot extends TelegramLongPollingCommandBot {
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
        if (update.hasMessage()) {
            String url = update.getMessage().getText();
            Runtime rt = Runtime.getRuntime();

            String currentPath = "";
            try {
                currentPath = new File(".").getCanonicalPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                runCommand(new File(currentPath + "/yt"), "./yt-dlp_macos \"https://www.youtube.com/watch?v=dXJbVOxwXYs&pp=0gcJCUELAYcqIYzv\" -P \"~/test\" -o \"%(id)s.%(ext)s\"  --print after_move:filepath");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

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

    public static void runCommand(File whereToRun, String command) throws Exception {
        System.out.println("Running in: " + whereToRun);
        System.out.println("Command: " + command);

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(whereToRun);

        builder.command("sh", "-c", command);

        Process process = builder.start();

        OutputStream outputStream = process.getOutputStream();
        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();

        printStream(inputStream);
        printStream(errorStream);

        boolean isFinished = process.waitFor(30, TimeUnit.SECONDS);
        outputStream.flush();
        outputStream.close();

        if(!isFinished) {
            process.destroyForcibly();
        }
    }

    private static void printStream(InputStream inputStream) throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

        }
    }
}
