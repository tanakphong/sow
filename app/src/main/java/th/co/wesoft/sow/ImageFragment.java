package th.co.wesoft.sow;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;

import th.co.wesoft.sow.Class.PlayList;
import th.co.wesoft.sow.Class.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {


    private ImageView mImageAd;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_image, container, false);

        String appPath = Utils.GetAppPath(getActivity().getApplicationContext());
        String imageFolder = Prefs.getString(ConfigBean.COLUMN_IMAGE_FILE, "");
        final int imageLoop = Integer.valueOf(Prefs.getString(ConfigBean.COLUMN_IMAGE_LOOP, "10")) * 1000;
        final String imagePath = appPath + "/" + imageFolder;
        File folder = new File(imagePath);
        final File file[] = folder.listFiles();
        if (file != null) {
            String[] ext = {"jpg", "png"};
            final PlayList pl = new PlayList(file, ext);

            mImageAd = (ImageView) v.findViewById(R.id.image_ad);

            // Hide after some seconds
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Log.i("dlg", "Playlist getName : " + pl.getCurrent().getName());
                    Bitmap bmp = BitmapFactory.decodeFile(imagePath + "/" + pl.Next().getName());
                    mImageAd.setImageBitmap(bmp);
                    handler.postDelayed(this, imageLoop);
                }
            };

            handler.postDelayed(runnable, imageLoop);
        }
        return v;
    }
}
