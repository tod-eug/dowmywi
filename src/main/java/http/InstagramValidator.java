package http;

import java.net.URI;

public class InstagramValidator {

    public static boolean isInstagramUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        try {
            URI uri = URI.create(url.trim());

            String scheme = uri.getScheme();
            String host = uri.getHost();

            if (scheme == null || host == null) {
                return false;
            }

            if (!scheme.equalsIgnoreCase("http")
                    && !scheme.equalsIgnoreCase("https")) {
                return false;
            }

            host = host.toLowerCase();

            if (!url.contains("/reel/") && !url.contains("/p/"))
                return false;

            return host.equals("instagram.com")
                    || host.endsWith(".instagram.com")
                    || host.equals("instagr.am")
                    || host.endsWith(".instagr.am");

        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}