package bot.commands;

import bot.DownloadBot;

public class PermissionsChecker {

    public static boolean isAllowed(Long userId) {
        boolean allowed = false;
        for (long l : DownloadBot.allowedUsers) {
            if (userId == l) {
                allowed = true;
            }
        }
        return allowed;
    }
}
