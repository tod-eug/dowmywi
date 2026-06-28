import bot.DownloadBot;
import exceptions.ConfigNotFoundException;
import org.apache.http.client.config.RequestConfig;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import util.PropertiesProvider;

public class Main {

    public static void main(String[] args) throws ConfigNotFoundException {

        PropertiesProvider.setup();

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(900_000)              // waiting the file uploading to the telegram for 15 min max
                .build();

        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setRequestConfig(requestConfig);
        botOptions.setBaseUrl(PropertiesProvider.configurationProperties.get("url"));

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new DownloadBot(botOptions));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
