import bot.DownloadBot;
import exceptions.ConfigNotFoundException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import util.PropertiesProvider;

public class Main {

    public static void main(String[] args) throws ConfigNotFoundException {

        PropertiesProvider.setup();

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new DownloadBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
