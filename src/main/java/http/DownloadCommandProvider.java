package http;

import java.text.MessageFormat;

public class DownloadCommandProvider {

    public static String buildDownloadYoutubeCommand(String url) {
        String space = " ";
        String commandName = "/Users/srs/bots/dowmywi/yt/yt-dlp_macos";
        String videoUrl = MessageFormat.format("\"{0}\"", url);
        String destinationFolder = "-P \"~/storage\"";
        String codec = "-S \"vcodec:h264,acodec:aac,ext:mp4:m4a\"";
        String quality = "-S \"res:480\"";
        String fileName = "-o \"%(title)s.%(ext)s\"";
        String cleanFileName = "--windows-filenames";
        String outputFormat = "--remux-video mp4";
        String printFilenameCommand = "--print after_move:filepath";
        //String result = MessageFormat.format("./yt-dlp_macos \"{0}\" -P \"~/storage\" -o \"%(id)s.%(ext)s\"  --print after_move:filepath", url);

        StringBuilder sb = new StringBuilder();
        return sb
                .append(commandName)
                .append(space)
                .append(videoUrl)
                .append(space)
                .append(destinationFolder)
                .append(space)
                .append(codec)
                .append(space)
                .append(quality)
                .append(space)
                .append(fileName)
                .append(space)
                .append(cleanFileName)
                .append(space)
                .append(outputFormat)
                .append(space)
                .append(printFilenameCommand)
                .toString();
    }

    public static String buildDownloadInstagramCommand(String url) {
        String space = " ";
        String commandName = "/Users/srs/bots/dowmywi/yt/yt-dlp_macos";
        String videoUrl = MessageFormat.format("\"{0}\"", url);
        String destinationFolder = "-P \"~/storage\"";
        String codec = "-S \"vcodec:h264,acodec:aac,ext:mp4:m4a\"";
        String cleanFileName = "--windows-filenames";
        String outputFormat = "--remux-video mp4";
        String printFilenameCommand = "--print after_move:filepath";
        //String result = MessageFormat.format("./yt-dlp_macos \"{0}\" -P \"~/storage\" -o \"%(id)s.%(ext)s\"  --print after_move:filepath", url);

        StringBuilder sb = new StringBuilder();
        return sb
                .append(commandName)
                .append(space)
                .append(videoUrl)
                .append(space)
                .append(codec)
                .append(space)
                .append(destinationFolder)
                .append(space)
                .append(cleanFileName)
                .append(space)
                .append(outputFormat)
                .append(space)
                .append(printFilenameCommand)
                .toString();
    }
}
