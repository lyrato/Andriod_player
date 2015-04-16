package yliu.andriod_player;

import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yliu on 15-4-15.
 */
public class UsbMoiveDetect {
    private static final String USB_PATH = "/storage/external_storage/sda1/";
    private static final String ACTIVITY_TAG="UsbMoiveDetect";
    public List<UsbEntry> parse()
    {
        List<UsbEntry> entries = new ArrayList<UsbEntry>();
        Log.e(UsbMoiveDetect.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        //
        File vPath = new File(USB_PATH);
        //File vPath = new File(  vSDCard+"/" );
        FilenameFilter filter = new MovFilter();
        File[] file = vPath.listFiles(filter);
        String link = null;
        int i = 0;

        for(i=0;i<file.length;i++) {
            //  if(file[i]!=null)
            {
                // Log.e(UsbMoiveDetect.ACTIVITY_TAG, "" + file[i].getName());
                entries.add(new UsbEntry(file[i].getName(),file[i].getAbsolutePath()));
            }
            //else
            // break;
            // Log.e(UsbMoiveDetect.ACTIVITY_TAG, "" + file[i].length());

            //if(file[i].length() >max_byte ) {
            //    max = i;
            //   max_byte = file[i].length();
            // }i<16;i++




        }


        //
        return entries;
    }
    public static class UsbEntry {
        public final String title;
        public final String link;

        private UsbEntry(String title, String link) {
            this.title = title;
            this.link = link;
        }
    }
    public class MovFilter implements FilenameFilter {

        @Override
        public boolean accept(File directory, String fileName) {
            if (fileName.endsWith(".mkv")) {
                return true;
            }else if (fileName.endsWith(".MP4")) {
                return true;
            }
            else if (fileName.endsWith(".mp4")) {
                return true;
            }
            return false;
        }
    }
}
