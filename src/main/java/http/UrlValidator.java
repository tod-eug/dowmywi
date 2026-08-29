package http;

import dto.Type;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class UrlValidator {

    public static boolean isUrlValid(String url) {
        return YoutubeValidator.isYoutubeVideo(url) || InstagramValidator.isInstagramUrl(url);
    }

    public static Type detectType(String url) {
        if (YoutubeValidator.isYoutubeVideo(url))
            return Type.YOUTUBE;
        else if (InstagramValidator.isInstagramUrl(url))
            return Type.INSTAGRAM;
        else
            return null;
    }
}