package th.co.wesoft.sow.Class;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by USER275 on 10/4/2017.
 */

public class PlayList {
    int index = 0;
    // File playlist[];
    ArrayList<File> playlist = new ArrayList<File>();

    public PlayList(File files[], String ext[]) {
        for (File file : files) {
            if (Arrays.asList(ext).contains(file.getName().split("\\.")[1])) {
                Log.i("dlg", "PlayList: "+file.getName());
                this.playlist.add(file);
            }
        }
    }

    public File Next() {
        int i = index;
        if (index >= playlist.size() - 1) {
            index = 0;
        } else {
            index += 1;
        }
        return playlist.get(i);
    }

    public File getCurrent() {
        return playlist.get(index);
    }

    public int getSize() {
        return playlist.size();
    }
}
