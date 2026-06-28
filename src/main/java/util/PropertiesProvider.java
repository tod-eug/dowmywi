package util;

import exceptions.ConfigNotFoundException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class PropertiesProvider {

    public static Map<String, String> configurationProperties = new HashMap<>();

    public static void setup() throws ConfigNotFoundException {
        loadConfigs("src/main/resources/bot.properties");
        loadConfigs("src/main/resources/db.properties");
    }

    public static List<Long> getAllowedUsers() {
        List<Long> result = new ArrayList<>();
        List<String> strings = List.of(configurationProperties.get("allowedUsers").split(","));

        for (String s: strings) {
            result.add(Long.parseLong(s));
        }
        return result;
    }

    private static void loadConfigs(String configFile) throws ConfigNotFoundException {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(configFile));
        } catch (IOException e) {
            throw new ConfigNotFoundException();
        }
        properties.forEach((key, value) -> configurationProperties.put(key.toString().trim(), value.toString().trim()));
    }
}
