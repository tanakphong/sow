package th.co.wesoft.sow;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import th.co.wesoft.sow.Class.MyContextWrapper;
import th.co.wesoft.sow.Class.Utils;
import th.co.wesoft.sow.model.Display;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

public class CashierActivity extends AppCompatActivity {
    private static final String TAG = "dlg";
    private static final String DISPLAY = "Display";

    private VideoView mVideoView;
    private TextView mLblCompName;
    private TextView mLblProductOrService1;
    private TextView mLblCardNameValue;
    private TextView mLblCardTypeValue;
    private TextView mLblCardDescValue;
    private TextView mLblUse;
    private TextView mLblUseValue;
    private TextView mLblBalance;
    private TextView mLblBalanceValue;
    private MarqueeTextFragment marqueeTextFragment;
    private SimpleTCPServer server;
    private int SocketPort;
    private boolean lockScreen = true;
    Display display;

    VDOFragment vdo = new VDOFragment();
    ImageFragment img = new ImageFragment();
    private View mFragment;

    @Override
    protected void attachBaseContext(Context newBase) {
        String lang;
        if (Prefs.getString(ConfigBean.COLUMN_LANGUAGE, "th").equals("en")) {
            lang = "en";
        } else {
            lang = "th";
        }
        super.attachBaseContext(MyContextWrapper.wrap(newBase, lang));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Prefs.Builder()
                .setContext(getApplicationContext())
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        String theme = Prefs.getString(ConfigBean.COLUMN_THEME, "Dark Blue");
        setTheme(Utils.setTheme(theme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        display = new Display();

        hideSystemUI(lockScreen);
//        blockTouch(lockScreen);

        //declare
        String appPath = Utils.GetAppPath(getApplicationContext());
        String marqueeFolder = Prefs.getString(ConfigBean.COLUMN_MARQUEE_FOLDER, "");
        String MarQueeFile = Prefs.getString(ConfigBean.COLUMN_MARQUEE_FILE, "");
        int MarQueeLoop = Integer.valueOf(Prefs.getString(ConfigBean.COLUMN_MARQUEE_LOOP, "1"));
        SocketPort = Integer.valueOf(Prefs.getString(ConfigBean.COLUMN_SOCKET_PORT, "8889"));
        String SDCardMarqueefile = appPath + "/" + marqueeFolder + "/" + MarQueeFile;

        //Bingdin Widget
        mLblCompName = (TextView) findViewById(R.id.lblCompName);
        mLblProductOrService1 = (TextView) findViewById(R.id.lblProductOrService1);
        mLblCardNameValue = (TextView) findViewById(R.id.lblCardNameValue);
        mLblCardTypeValue = (TextView) findViewById(R.id.lblCardTypeValue);
        mLblCardDescValue = (TextView) findViewById(R.id.lblCardDescValue);
        mLblUse = (TextView) findViewById(R.id.lblUse);
        mLblUseValue = (TextView) findViewById(R.id.lblUseValue);
        mLblBalance = (TextView) findViewById(R.id.lblBalance);
        mLblBalanceValue = (TextView) findViewById(R.id.lblBalanceValue);

        mFragment = findViewById(R.id.fragment);

        mFragment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String label;
                if (Prefs.getBoolean(ConfigBean.COLUMN_IS_USE_VDO, true)) {
                    label = "Image";
                } else {
                    label = "Video";
                }
                hideSystemUI(lockScreen);
                Toast.makeText(getApplicationContext(), label + " Size Width :" + (mFragment.getWidth() + ", Height :" + +mFragment.getHeight()), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        if (Prefs.getBoolean(ConfigBean.COLUMN_IS_USE_VDO, true)) {
            openFragment(img);
        } else {
            openFragment(vdo);
        }

//        mLblCompName.setText(Prefs.getString(TerminalBean.COLUMN_COMPANY_NAME, ""));
//        mLblProductOrService1.setText(Prefs.getString(TerminalBean.COLUMN_TERM_DESC, ""));

        //setEvent
        File file_marquee = new File(SDCardMarqueefile);
        float speed = (float) Prefs.getInt(ConfigBean.COLUMN_MARQUEE_SPEED, 8) / 100;
        Log.d(TAG, "COLUMN_MARQUEE_SPEED: " + Prefs.getInt(ConfigBean.COLUMN_MARQUEE_SPEED, 8));
        Log.d(TAG, "Speed: " + speed);
        if (file_marquee.exists()) {
            String text = readTextFilePath(SDCardMarqueefile, MarQueeLoop);
            marqueeTextFragment = new MarqueeTextFragment();
            marqueeTextFragment.setMarqueeInfo(
                    new MarqueeInfo(text, Utils.getTextColor(theme), speed, Typeface.DEFAULT, false)
            );
            getFragmentManager().beginTransaction()
                    .replace(R.id.fraMarquee, marqueeTextFragment)
                    .commit();
        }


        //-----------------------socket server----------------------------

        server = new SimpleTCPServer(Integer.valueOf(SocketPort));
        server.setOnDataReceivedListener(new SimpleTCPServer.OnDataReceivedListener() {
            public void onDataReceived(String message, String ip) {
                Log.i("dlg", "onDataReceived: " + message + ":" + ip);
                String delimeter = Prefs.getString(ConfigBean.COLUMN_DELIMITER_TEXT, "");
                String[] line = message.split(delimeter, -1);
                for (int i = 0; i < line.length; i++) {
                    Log.i(TAG, String.format("line[%s]: %s", String.valueOf(i), line[i]));
                }
                if (line.length == 9) {
//                    display = new Display(line[0], line[1], line[3], line[4],
//                            line[5], line[6], line[7], line[8], line[9]);
                    mLblCompName.setText(line[0]);
                    mLblProductOrService1.setText(line[1]);
                    mLblCardNameValue.setText(line[2]);
                    mLblCardTypeValue.setText(line[3]);
                    mLblCardDescValue.setText(line[4]);
                    mLblUse.setText(line[5]);
                    mLblUseValue.setText(line[6]);
                    mLblBalance.setText(line[7]);
                    mLblBalanceValue.setText(line[8]);
                }
            }
        });

        //------------------------------------------------------------------

    }

    public static String readTextFilePath(String path, int loop) {
        try {

            File myFile = new File(path);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader bufferedreader = new BufferedReader(
                    new InputStreamReader(fIn));

            String line;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedreader.readLine()) != null) {
                for (int i = 0; i < loop; i++) {
                    stringBuilder.append(line);
                    stringBuilder.append('\t');
                }
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_work, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_get_size:
                Toast.makeText(this, "Video Size Width :" + (mFragment.getWidth() + ", Height :" + +mFragment.getHeight()), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_get_display_metrics:
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int widthPixels = metrics.widthPixels;
                int heightPixels = metrics.heightPixels;
                float scaleFactor = metrics.density;
                float widthDp = widthPixels / scaleFactor;
                float heightDp = heightPixels / scaleFactor;
                Toast.makeText(this, "(widthPixels,heightPixels):" + (widthDp + ", " + heightDp), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onResume() {
        super.onResume();
        server.start();
    }

    public void onStop() {
        super.onStop();
        server.stop();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

//        display = savedInstanceState.getParcelable(DISPLAY);
//        Log.d(TAG, "onRestoreInstanceState: " + display.getLine1());

//        mLblCompName.setText(display.getLine1());
//        mLblProductOrService1.setText(display.getLine2());
//
//        mLblCardNameValue.setText(display.getLine4());
//        mLblCardTypeValue.setText(display.getLine5());
//        mLblCardDescValue.setText(display.getLine6());
//        mLblUse.setText(display.getLine71());
//        mLblUseValue.setText(display.getLine72());
//        mLblBalance.setText(display.getLine81());
//        mLblBalanceValue.setText(display.getLine82());
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putParcelable(DISPLAY, display);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    private void hideSystemUI(boolean b) {
        if (b) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }

    private void blockTouch(boolean b) {
        if (b) {
            getWindow().setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().clearFlags(FLAG_NOT_TOUCHABLE);
        }

    }
}
