package th.co.wesoft.sow;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

public class CheckPriceActivity extends AppCompatActivity {

    private VideoView mVideoView;
    private TextView mLblCardDesc;
    private TextView mLblCompName;
    private TextView mLblCardNameValue;
    private TextView mLblCardTypeValue;
    private TextView mLblCardDescValue;
    private TextView mLblBalance;
    private TextView mLblBalanceValue;
    private EditText mTxtBarcode;
    private MarqueeTextFragment marqueeTextFragment;
    private SimpleTCPServer server;
    private int SocketPort;
    private String cardno;
    private boolean lockScreen = true;

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
        setContentView(R.layout.activity_price_check);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hideSystemUI(lockScreen);
        blockTouch(lockScreen);

        //declare
        String appPath = Utils.GetAppPath(getApplicationContext());
        String marqueeFolder = Prefs.getString(ConfigBean.COLUMN_MARQUEE_FOLDER, "");
        String MarQueeFile = Prefs.getString(ConfigBean.COLUMN_MARQUEE_FILE, "");
        int MarQueeLoop = Integer.valueOf(Prefs.getString(ConfigBean.COLUMN_MARQUEE_LOOP, "1"));
        SocketPort = Integer.valueOf(Prefs.getString(ConfigBean.COLUMN_SOCKET_PORT, "8888"));
        String SDCardMarqueefile = appPath + "/" + marqueeFolder + "/" + MarQueeFile;

        //Bingdin Widget
        mLblCompName = (TextView) findViewById(R.id.lblCompName);
        mLblCardNameValue = (TextView) findViewById(R.id.lblCardNameValue);
        mLblCardTypeValue = (TextView) findViewById(R.id.lblCardTypeValue);
        mLblCardDescValue = (TextView) findViewById(R.id.lblCardDescValue);
        mLblBalance = (TextView) findViewById(R.id.lblBalance);
        mLblBalanceValue = (TextView) findViewById(R.id.lblBalanceValue);
        mTxtBarcode = (EditText) findViewById(R.id.txtBarcode);

        mFragment = findViewById(R.id.fragment);

        if (Prefs.getBoolean(ConfigBean.COLUMN_IS_USE_VDO, true)) {
            openFragment(img);
        } else {
            openFragment(vdo);
        }

        mLblCompName.setText(Prefs.getString(TerminalBean.COLUMN_COMPANY_NAME, ""));

        mTxtBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    //do something here
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        cardno = mTxtBarcode.getText().toString();
//                        new GetCardRemainOrUsedTask().execute(url, EncodePWD, cardno);
                        new CountDownTimer(20000, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onFinish() {
                                // TODO Auto-generated method stub
                                mLblCardNameValue.setText("");
                                mLblCardDescValue.setText("");
                                mLblCardTypeValue.setText("");
                                mLblBalance.setText("");
                                mLblBalanceValue.setText("");

                            }
                        }.start();
                        return true;
                    }
                }
                return false;
            }
        });

        File file_marquee = new File(SDCardMarqueefile);
        if (file_marquee.exists()) {
            String text = readTextFilePath(SDCardMarqueefile, MarQueeLoop);
            marqueeTextFragment = new MarqueeTextFragment();
            marqueeTextFragment.setMarqueeInfo(
                    new MarqueeInfo(text, Utils.getTextColor(theme), 0.08f, Typeface.DEFAULT, false)
            );
            getFragmentManager().beginTransaction()
                    .replace(R.id.fraMarquee, marqueeTextFragment)
                    .commit();
        }

//        mTxtBarcode.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(CheckPriceActivity.this.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mTxtBarcode.getWindowToken(), 0);

        getCurrentFocus();

        hideVirtualKeyboard(mTxtBarcode);
        mTxtBarcode.setText("");
        mTxtBarcode.requestFocus();
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
                for (int i = 0; i < Integer.valueOf(loop); i++) {
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
                Toast.makeText(this, "Video Size Width :" + (mFragment.getWidth() +", Height :" + + mFragment.getHeight()), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_get_display_metrics:
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int widthPixels = metrics.widthPixels;
                int heightPixels = metrics.heightPixels;
                float scaleFactor = metrics.density;
                float widthDp = widthPixels / scaleFactor;
                float heightDp = heightPixels / scaleFactor;
                Toast.makeText(this, "(widthPixels,heightPixels):" + (widthDp +", "+ heightDp), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    public void showDialogDataInfo(String header, String detail) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle(header)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(detail)
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.dia_btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something with parameter.
                        //Toast.makeText(MainActivity.this, "Delete :" + pid, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 3000);
    }

    private void hideVirtualKeyboard(EditText editText) {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextIsSelectable(true);
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
        }else{
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
