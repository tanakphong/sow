package th.co.wesoft.sow;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import th.co.wesoft.sow.Class.MyContextWrapper;
import th.co.wesoft.sow.Class.Utils;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

public class CheckFoodCourtActivity extends AppCompatActivity {

    private TextView mLblCompName;
    private TextView mLblProductOrService1;
    private TextView mLblProductOrService2;
    private TextView mLblCardNameValue;
    private TextView mLblCardTypeValue;
    private TextView mLblCardDescValue;
    private TextView mLblUse;
    private TextView mLblUseValue;
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


    protected class GetCardRemainOrUsedTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLblCardNameValue.setText("");
            mLblCardDescValue.setText("");
            mLblCardTypeValue.setText("");
            mLblUse.setText("");
            mLblUseValue.setText("");
            mLblBalance.setText("");
            mLblBalanceValue.setText("");
        }

        @Override
        protected String doInBackground(String... params) {
            String output = CallWebService.GetCardRemainOrUsed(params[0], params[1], params[2]);
            Log.i("dlg", "doInBackground: " + output);
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("dlg", "onPostExecute: " + s);
            try {
//                XMLParser parser = new XMLParser();
//                Document doc = parser.getDomElement(s);
//                NodeList nl = doc.getElementsByTagName("GetCardRemainOrUsedResponse");
//                Element item = (Element) nl.item(0)
                JSONObject json = new JSONObject(s);
                if (json.getString("Return").equals("true")) {
                    if (!json.getString("MemName").equals("anyType{}")) {

                        String CardTypeDesc = json.getString("CardTypeDesc");
                        String MemName = json.getString("MemName");
                        String CardExpText = json.getString("CardExpText");
                        String CardExp = json.getString("CardExp");
                        String RemainWithReserveText = json.getString("RemainWithReserveText");
                        String RemainWithReserve = json.getString("RemainWithReserve");
                        Log.i("dlg", "data : " + CardTypeDesc + ", " + MemName + ", " + CardExpText + ", " + ", " + CardExp + ", " + ", " + RemainWithReserveText + ", " + ", " + RemainWithReserve);

//                String cardno = mTxtBarcode.getText().toString();
                        mLblCardTypeValue.setText(CardTypeDesc);
                        mLblCardDescValue.setText(MemName);
                        mLblUse.setText(CardExpText);
                        mLblUseValue.setText(CardExp);
                        mLblBalance.setText(RemainWithReserveText);
                        mLblBalanceValue.setText(RemainWithReserve);
                    } else {
                        mLblCardDescValue.setText(getResources().getString(R.string.alert_card_not_found));
//                        showDialogDataInfo(getResources().getString(R.string.app_name), "Card not found.");
                    }
                    if (cardno.equals(Prefs.getString(ConfigBean.COLUMN_PWD_TOUCHLOCK, "USER275"))) {
                        hideSystemUI(true);
                        lockScreen = !lockScreen;
                        blockTouch(lockScreen);
                        if(lockScreen) {
                            Toast.makeText(CheckFoodCourtActivity.this, "Touch screen lock.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CheckFoodCourtActivity.this, "Touch screen unlock.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.i("dlg", json.getString("Exception"));
                    showDialogDataInfo(getResources().getString(R.string.app_name), json.getString("Exception"));
                }
                mLblCardNameValue.setText(cardno);
                mTxtBarcode.setText("");
                mTxtBarcode.requestFocus();

            } catch (Exception aE) {
                Log.d("dlg", "onPostExecute  Exception : " + aE.getMessage());
            }

        }
    }

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
        setContentView(R.layout.activity_food_court_check);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hideSystemUI(lockScreen);
        blockTouch(lockScreen);

        //declare
        String appPath = Utils.GetAppPath(getApplicationContext());
        String marqueeFolder = Prefs.getString(ConfigBean.COLUMN_MARQUEE_FOLDER, "");
        String MarQueeFile = Prefs.getString(ConfigBean.COLUMN_MARQUEE_FILE, "");
        int MarQueeLoop = Integer.valueOf(Prefs.getString(ConfigBean.COLUMN_MARQUEE_LOOP, "1"));
        String WCFHost = Prefs.getString(ConfigBean.COLUMN_WCF_HOST, "");
        String WCFPost = Prefs.getString(ConfigBean.COLUMN_WCF_PORT, "");
        final String EncodePWD = Prefs.getString(ConfigBean.COLUMN_ENCODE_PWD, "");
        final String url = "http://" + WCFHost + ":" + WCFPost + "/WFservice30?wsdl";
        SocketPort = Integer.valueOf(Prefs.getString(ConfigBean.COLUMN_SOCKET_PORT, "8889"));
        String SDCardMarqueefile = appPath + "/" + marqueeFolder + "/" + MarQueeFile;

        //Bingdin Widget
        mLblCompName = (TextView) findViewById(R.id.lblCompName);
        mLblProductOrService1 = (TextView) findViewById(R.id.lblProductOrService1);
        mLblProductOrService2 = (TextView) findViewById(R.id.lblProductOrService2);
        mLblCardNameValue = (TextView) findViewById(R.id.lblCardNameValue);
        mLblCardTypeValue = (TextView) findViewById(R.id.lblCardTypeValue);
        mLblCardDescValue = (TextView) findViewById(R.id.lblCardDescValue);
        mLblUse = (TextView) findViewById(R.id.lblUse);
        mLblUseValue = (TextView) findViewById(R.id.lblUseValue);
        mLblBalance = (TextView) findViewById(R.id.lblBalance);
        mLblBalanceValue = (TextView) findViewById(R.id.lblBalanceValue);
        mTxtBarcode = (EditText) findViewById(R.id.txtBarcode);
//        View viewById = findViewById(R.id.layoutRoot);
//        viewById.setFocusable(false);
//        viewById.setFocusableInTouchMode(false);
        mFragment = findViewById(R.id.fragment);
//        mFragment.setFocusable(false);
//        mFragment.setFocusableInTouchMode(false);
        mFragment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String label;
                if (Prefs.getBoolean(ConfigBean.COLUMN_IS_USE_VDO, true)) {
                    label = "Image";
                } else {
                    label = "Video";
                }

                Toast.makeText(getApplicationContext(), label + " Size Width :" + (mFragment.getWidth() + ", Height :" + +mFragment.getHeight()), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        if (Prefs.getBoolean(ConfigBean.COLUMN_IS_USE_VDO, true)) {
            openFragment(img);
        } else {
            openFragment(vdo);
        }

        mLblCompName.setText(Prefs.getString(TerminalBean.COLUMN_COMPANY_NAME, ""));
        mLblProductOrService1.setText(Prefs.getString(TerminalBean.COLUMN_TERM_DESC, ""));
        mLblProductOrService2.setText(Prefs.getString(TerminalBean.COLUMN_SRC_WELCOME, ""));

        mTxtBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;


                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    cardno = mTxtBarcode.getText().toString();
                    new GetCardRemainOrUsedTask().execute(url, EncodePWD, cardno);
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
                            mLblUse.setText("");
                            mLblUseValue.setText("");
                            mLblBalance.setText("");
                            mLblBalanceValue.setText("");
                            hideSystemUI(true);
                            blockTouch(true);

                        }
                    }.start();
                    return true;
                }
                return false;
            }
        });

        File file_marquee = new File(SDCardMarqueefile);
        if (file_marquee.exists()) {
            String text = readTextFilePath(SDCardMarqueefile, Integer.valueOf(MarQueeLoop));
            marqueeTextFragment = new MarqueeTextFragment();
            marqueeTextFragment.setMarqueeInfo(
                    new MarqueeInfo(text, Utils.getTextColor(theme), 0.08f, Typeface.DEFAULT, false)
            );
            getFragmentManager().beginTransaction()
                    .replace(R.id.fraMarquee, marqueeTextFragment)
                    .commit();
        }

        getCurrentFocus();

        hideVirtualKeyboard(mTxtBarcode);
        mTxtBarcode.setText("");
        mTxtBarcode.requestFocus();

//        InputMethodManager imm = (InputMethodManager) getSystemService(CheckFoodCourtActivity.this.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mTxtBarcode.getWindowToken(), 0);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        hideSoftKeyboard();

//        mTxtBarcode.setInputType(InputType.TYPE_CLASS_TEXT);
//        mTxtBarcode.requestFocus();
//        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        mgr.showSoftInput(mTxtBarcode, InputMethodManager.SHOW_FORCED);
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
        final Handler handler = new Handler();
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
                hideSystemUI(lockScreen);
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
