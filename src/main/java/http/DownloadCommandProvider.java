package http;

import java.text.MessageFormat;

public class DownloadCommandProvider {

    public static String getVideoCommand(String url) {
        String space = " ";
        String commandName = "./yt-dlp_macos";
        String videoUrl = MessageFormat.format("\"{0}\"", url);
        String destinationFolder = "-P \"~/storage\"";
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

    public static String getAudioCommand(String url) {
        String space = " ";
        String commandName = "./yt-dlp_macos";
        String videoUrl = MessageFormat.format("\"{0}\"", url);
        String destinationFolder = "-P \"~/storage\"";
        String fileName = "-o \"%(title)s.%(ext)s\"";
        String ffmpegLocation = "--ffmpeg-location /opt/homebrew/bin";
        String cleanFileName = "--windows-filenames";
        String extractAudio = "--extract-audio";
        String audioFormat = "--audio-format mp3";
        String audioQuality = "--audio-quality 320K";
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
                .append(fileName)
                .append(space)
                .append(ffmpegLocation)
                .append(space)
                .append(cleanFileName)
                .append(space)
                .append(extractAudio)
                .append(space)
                .append(audioFormat)
                .append(space)
                .append(audioQuality)
                .append(space)
                .append(printFilenameCommand)
                .toString();
    }
}
