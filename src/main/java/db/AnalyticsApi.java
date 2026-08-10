package db;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AnalyticsApi {

    public static String createEvent(Long userId, String messageId, String command, String text, String callback) {

        String createdDate = new SimpleDateFormat(DatabaseHelper.createDateDefaultPattern).format(new Date());
        UUID id = UUID.randomUUID();
        String normalizedText = text.replace("'", "");

        String insertQuery = String.format("insert into analytics (id, user_id, command, message_id, text, callback, create_date) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                id, userId, command, messageId, normalizedText, callback, createdDate);

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.getPreparedStatement(insertQuery).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeConnections();
        }
        return id.toString();
    }

    public static void setMessageId(String analyticsId, String messageId) {
        String insertQuery = String.format("update public.analytics set message_id = '%s' where id = '%s';", messageId, analyticsId);

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.getPreparedStatement(insertQuery).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeConnections();
        }
    }
}
