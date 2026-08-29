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

            //for now downloading only reels
            //later need to keep only /reel/ and /p/
            //for /p/ need to make one more additional check if the post contains videos
            //need to search all yt-dlp output outputLines and return list of videos
            //iterate through this list and upload all videos
            //Command: /Users/srs/bots/dowmywi/yt/yt-dlp_macos "https://www.instagram.com/p/Dbv5_TxDVQ5/" -S "vcodec:h264,acodec:aac,ext:mp4:m4a" -P "~/storage" --windows-filenames --remux-video mp4 --print after_move:filepath
            //WARNING: [Instagram] Dbv5_TxDVQ5: No CSRF token set by Instagram API
            ///Users/srs/storage/Video by svetaripan [Dbv5ORNDYUk].mp4
            ///Users/srs/storage/Video by svetaripan [Dbv5OVSjfb5].mp4
            ///Users/srs/storage/Video by svetaripan [Dbv5ObuDTEU].mp4
            if (!url.contains("/reel/"))
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

//test for instagram
//19:55:22: Executing ':Main.main()'…
//
//> Task :compileJava UP-TO-DATE
//> Task :processResources UP-TO-DATE
//> Task :classes UP-TO-DATE
//
//> Task :Main.main()
//SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
//SLF4J: Defaulting to no-operation (NOP) logger implementation
//SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
//Running in: /Users/srs/codebase/bts/dowmywi/yt
//Command: /Users/srs/bots/dowmywi/yt/yt-dlp_macos "https://www.instagram.com/reel/DLeOo3NRcoL/?igsh=MXFmbm41bmx6dTllag==" -S "vcodec:h264,acodec:aac,ext:mp4:m4a" -P "~/storage" --windows-filenames --remux-video mp4 --print after_move:filepath
//WARNING: [Instagram] DLeOo3NRcoL: No CSRF token set by Instagram API
/// Users/srs/storage/Video by ivan_krasavin_ [DLeOo3NRcoL].mp4
//Running in: /Users/srs/codebase/bts/dowmywi/yt
//Command: /Users/srs/bots/dowmywi/yt/yt-dlp_macos "https://www.instagram.com/reel/DZtZNQDMOH9/" -S "vcodec:h264,acodec:aac,ext:mp4:m4a" -P "~/storage" --windows-filenames --remux-video mp4 --print after_move:filepath
//WARNING: [Instagram] DZtZNQDMOH9: No CSRF token set by Instagram API
///Users/srs/storage/Video by life_07n [DZtZNQDMOH9].mp4
//Running in: /Users/srs/codebase/bts/dowmywi/yt
//Command: /Users/srs/bots/dowmywi/yt/yt-dlp_macos "https://www.instagram.com/reel/DbBKFRJMqYY/" -S "vcodec:h264,acodec:aac,ext:mp4:m4a" -P "~/storage" --windows-filenames --remux-video mp4 --print after_move:filepath
//WARNING: [Instagram] DbBKFRJMqYY: No CSRF token set by Instagram API
///Users/srs/storage/Video by fitbypeople [DbBKFRJMqYY].mp4
//Running in: /Users/srs/codebase/bts/dowmywi/yt
//Command: /Users/srs/bots/dowmywi/yt/yt-dlp_macos "https://www.instagram.com/p/DckCCI6B_RJ/" -S "vcodec:h264,acodec:aac,ext:mp4:m4a" -P "~/storage" --windows-filenames --remux-video mp4 --print after_move:filepath
//WARNING: [Instagram] DckCCI6B_RJ: No CSRF token set by Instagram API
//ERROR: [Instagram] DckCCI6B_RJ: There is no video in this post
//Running in: /Users/srs/codebase/bts/dowmywi/yt
//Command: /Users/srs/bots/dowmywi/yt/yt-dlp_macos "https://www.instagram.com/reel/DckxyoXKxfd/" -S "vcodec:h264,acodec:aac,ext:mp4:m4a" -P "~/storage" --windows-filenames --remux-video mp4 --print after_move:filepath
//WARNING: [Instagram] DckxyoXKxfd: No CSRF token set by Instagram API
///Users/srs/storage/Video by dmitry_vkp [DckxyoXKxfd].mp4
//Running in: /Users/srs/codebase/bts/dowmywi/yt
//Command: /Users/srs/bots/dowmywi/yt/yt-dlp_macos "https://www.instagram.com/p/Dcl-lZADu-c/?img_index=3" -S "vcodec:h264,acodec:aac,ext:mp4:m4a" -P "~/storage" --windows-filenames --remux-video mp4 --print after_move:filepath
//WARNING: [Instagram] Dcl-lZADu-c: No CSRF token set by Instagram API
//ERROR: [Instagram] Dcl9wNcu-oo: No video formats found!; please report this issue on  https://github.com/yt-dlp/yt-dlp/issues?q= , filling out the appropriate issue template. Confirm you are on the latest version using  yt-dlp -U
///Users/srs/storage/Video by irina.silistraru [Dcl9vi_jsTd].mp4
//ERROR: [Instagram] Dcl9wkmOjvn: No video formats found!; please report this issue on  https://github.com/yt-dlp/yt-dlp/issues?q= , filling out the appropriate issue template. Confirm you are on the latest version using  yt-dlp -U
///Users/srs/storage/Video by irina.silistraru [Dcl9vx9Du7z].mp4
//ERROR: [Instagram] Dcl9w0wOLvJ: No video formats found!; please report this issue on  https://github.com/yt-dlp/yt-dlp/issues?q= , filling out the appropriate issue template. Confirm you are on the latest version using  yt-dlp -U
///Users/srs/storage/Video by irina.silistraru [Dcl9wEwDlNe].mp4
//ERROR: [Instagram] Dcl9xGrO7HB: No video formats found!; please report this issue on  https://github.com/yt-dlp/yt-dlp/issues?q= , filling out the appropriate issue template. Confirm you are on the latest version using  yt-dlp -U
///Users/srs/storage/Video by irina.silistraru [Dcl9wdOjlA1].mp4
//ERROR: [Instagram] Dcl9xKsu5bs: No video formats found!; please report this issue on  https://github.com/yt-dlp/yt-dlp/issues?q= , filling out the appropriate issue template. Confirm you are on the latest version using  yt-dlp -U
///Users/srs/storage/Video by irina.silistraru [Dcl9wsODmfd].mp4
//ERROR: [Instagram] Dcl9xSpu2qd: No video formats found!; please report this issue on  https://github.com/yt-dlp/yt-dlp/issues?q= , filling out the appropriate issue template. Confirm you are on the latest version using  yt-dlp -U
///Users/srs/storage/Video by irina.silistraru [Dcl9w7NDlfT].mp4
//ERROR: [Instagram] Dcl9xebuN0y: No video formats found!; please report this issue on  https://github.com/yt-dlp/yt-dlp/issues?q= , filling out the appropriate issue template. Confirm you are on the latest version using  yt-dlp -U
///Users/srs/storage/Video by irina.silistraru [Dcl9xPHjrc5].mp4
//ERROR: [Instagram] Dcl9xnZugx2: No video formats found!; please report this issue on  https://github.com/yt-dlp/yt-dlp/issues?q= , filling out the appropriate issue template. Confirm you are on the latest version using  yt-dlp -U
///Users/srs/storage/Video by irina.silistraru [Dcl9xaBDo5_].mp4
//ERROR: [Instagram] Dcl9xzhumRx: No video formats found!; please report this issue on  https://github.com/yt-dlp/yt-dlp/issues?q= , filling out the appropriate issue template. Confirm you are on the latest version using  yt-dlp -U
///Users/srs/storage/Video by irina.silistraru [Dcl9xpfDorF].mp4
//ERROR: [Instagram] Dcl9yAJuvI2: No video formats found!; please report this issue on  https://github.com/yt-dlp/yt-dlp/issues?q= , filling out the appropriate issue template. Confirm you are on the latest version using  yt-dlp -U
///Users/srs/storage/Video by irina.silistraru [Dcl9yKdDjT_].mp4
//Running in: /Users/srs/codebase/bts/dowmywi/yt
//Command: /Users/srs/bots/dowmywi/yt/yt-dlp_macos "https://www.instagram.com/p/Dbv5_TxDVQ5/?img_index=2" -S "vcodec:h264,acodec:aac,ext:mp4:m4a" -P "~/storage" --windows-filenames --remux-video mp4 --print after_move:filepath
//WARNING: [Instagram] Dbv5_TxDVQ5: No CSRF token set by Instagram API
///Users/srs/storage/Video by svetaripan [Dbv5ORNDYUk].mp4
///Users/srs/storage/Video by svetaripan [Dbv5OVSjfb5].mp4
///Users/srs/storage/Video by svetaripan [Dbv5ObuDTEU].mp4
//Running in: /Users/srs/codebase/bts/dowmywi/yt
//Command: /Users/srs/bots/dowmywi/yt/yt-dlp_macos "https://www.instagram.com/p/Dbv5_TxDVQ5/?img_index=1" -S "vcodec:h264,acodec:aac,ext:mp4:m4a" -P "~/storage" --windows-filenames --remux-video mp4 --print after_move:filepath
//WARNING: [Instagram] Dbv5_TxDVQ5: No CSRF token set by Instagram API
///Users/srs/storage/Video by svetaripan [Dbv5ORNDYUk].mp4
///Users/srs/storage/Video by svetaripan [Dbv5OVSjfb5].mp4
///Users/srs/storage/Video by svetaripan [Dbv5ObuDTEU].mp4
//Running in: /Users/srs/codebase/bts/dowmywi/yt
//Command: /Users/srs/bots/dowmywi/yt/yt-dlp_macos "https://www.instagram.com/p/Dbv5_TxDVQ5/" -S "vcodec:h264,acodec:aac,ext:mp4:m4a" -P "~/storage" --windows-filenames --remux-video mp4 --print after_move:filepath
//WARNING: [Instagram] Dbv5_TxDVQ5: No CSRF token set by Instagram API
///Users/srs/storage/Video by svetaripan [Dbv5ORNDYUk].mp4
///Users/srs/storage/Video by svetaripan [Dbv5OVSjfb5].mp4
///Users/srs/storage/Video by svetaripan [Dbv5ObuDTEU].mp4
//Running in: /Users/srs/codebase/bts/dowmywi/yt
//Command: /Users/srs/bots/dowmywi/yt/yt-dlp_macos "https://www.instagram.com/reel/Dcem2jfxxs-/" -S "vcodec:h264,acodec:aac,ext:mp4:m4a" -P "~/storage" --windows-filenames --remux-video mp4 --print after_move:filepath
//WARNING: [Instagram] Dcem2jfxxs-: No CSRF token set by Instagram API
///Users/srs/storage/Video by sergei.swat [Dcem2jfxxs-].mp4
