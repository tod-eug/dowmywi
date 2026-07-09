package http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class UrlValidator {

    private static final Pattern YOUTUBE_VIDEO_ID = Pattern.compile("^[a-zA-Z0-9_-]{11}$");

    public static boolean isYoutubeVideo(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        try {
            String trimmedUrl = url.trim();

            if (!trimmedUrl.matches("(?i)^[a-z][a-z0-9+.-]*://.*")) {
                trimmedUrl = "https://" + trimmedUrl;
            }

            URI uri = new URI(trimmedUrl);

            String scheme = uri.getScheme();
            if (scheme == null ||
                    (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
                return false;
            }

            String host = uri.getHost();
            if (host == null) {
                return false;
            }

            host = host.toLowerCase();

            String path = uri.getPath();
            String query = uri.getQuery();

            // https://youtu.be/VIDEO_ID
            if (host.equals("youtu.be")) {
                return path != null && isValidYoutubeVideoId(path.replaceFirst("^/", ""));
            }

            // Разрешаем youtube.com и поддомены, например www.youtube.com, m.youtube.com
            if (!host.equals("youtube.com") && !host.endsWith(".youtube.com")) {
                return false;
            }

            // https://www.youtube.com/watch?v=VIDEO_ID
            if ("/watch".equals(path) && query != null) {
                for (String param : query.split("&")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2 && pair[0].equals("v")) {
                        return isValidYoutubeVideoId(pair[1]);
                    }
                }
            }

            // https://www.youtube.com/embed/VIDEO_ID
            // https://www.youtube.com/shorts/VIDEO_ID
            if (path != null) {
                String[] parts = path.split("/");
                if (parts.length >= 3 &&
                        (parts[1].equals("embed") || parts[1].equals("shorts"))) {
                    return isValidYoutubeVideoId(parts[2]);
                }
            }

            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private static boolean isValidYoutubeVideoId(String id) {
        return id != null && YOUTUBE_VIDEO_ID.matcher(id).matches();
    }
}