package th.co.wesoft.sow;


import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;

import th.co.wesoft.sow.Class.PlayList;
import th.co.wesoft.sow.Class.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class VDOFragment extends Fragment {


    private VideoView mVideoView;

    public VDOFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_vdo, container, false);

        String appPath = Utils.GetAppPath(getActivity().getApplicationContext());
        String vdoFolder = Prefs.getString(ConfigBean.COLUMN_VDO_FILE, "");
        final String vdoPath = appPath + "/" + vdoFolder;
        File folder = new File(vdoPath);
        final File file[] = folder.listFiles();
        String[] ext = {"mp4"};
        if (file != null) {
            final PlayList pl = new PlayList(file, ext);

            mVideoView = (VideoView) v.findViewById(R.id.vidView);

            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mVideoView.stopPlayback();
                    mVideoView.setVideoPath(vdoPath + "/" + pl.Next().getName());
//                    mVideoView.setFocusable(false);
//                    mVideoView.setFocusableInTouchMode(false);
                    mVideoView.start();
                }
            });


            mVideoView.setVideoPath(vdoPath + "/" + pl.Next().getName());
//            MediaController mc = new MediaController(getActivity());
//            mc.clearFocus();
//            mVideoView.setMediaController(mc);
//            mVideoView.setFocusable(false);
//            mVideoView.setFocusableInTouchMode(false);
            mVideoView.start();
        }
        return v;
    }

}
