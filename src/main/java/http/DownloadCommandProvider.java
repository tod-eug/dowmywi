package http;

import java.text.MessageFormat;

public class DownloadCommandProvider {

    public static String buildDownloadCommand(String url) {
        String space = " ";
        String commandName = "./yt-dlp_macos";
        String videoUrl = MessageFormat.format("\"{0}\"", url);
        String destinationFolder = "-P \"~/storage\"";
        String quality = "-S \"res:480\"";
        String fileName = "-o \"%(id)s.%(ext)s\"";
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
                .append(quality)
                .append(space)
                .append(fileName)
                .append(space)
                .append(outputFormat)
                .append(space)
                .append(printFilenameCommand)
                .toString();
    }
}
