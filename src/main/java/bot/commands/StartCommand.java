package bot.commands;

import bot.ReplyConstants;
import db.AnalyticsApi;
import db.UsersApi;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand implements IBotCommand {

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "start";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        String userId = UsersApi.findUserByTgId(message.getFrom().getId().toString(), message.getFrom(), message.getChatId().toString());
        String analyticsId = AnalyticsApi.createEvent(message.getFrom().getId(), message.getMessageId().toString(), getCommandIdentifier(), "","");

        SendMessage sm = new SendMessage();
        sm.setChatId(message.getChatId());

        if (PermissionsChecker.isAllowed(message.getFrom().getId())) {
            String text = ReplyConstants.START_REPLY_WELCOME + ReplyConstants.GIVE_ME_THE_LINK;
            sm.setText(text);
            AnalyticsApi.createEvent(message.getFrom().getId(), String.valueOf(message.getMessageId() + 1), "bot", text,"");
        } else {
            String text = ReplyConstants.START_REPLY_WELCOME + ReplyConstants.NOT_ALLOWED;
            sm.setText(text);
            AnalyticsApi.createEvent(message.getFrom().getId(), String.valueOf(message.getMessageId() + 1), "bot", text,"");
        }

        MessageProcessor.sendMsg(absSender, sm);
    }
}
