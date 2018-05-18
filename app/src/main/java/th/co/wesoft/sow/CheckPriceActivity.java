package th.co.wesoft.sow;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

public class CheckPriceActivity extends AppCompatActivity {

    public static final int RequestPermissionCode = 1;

    private TextView mLblCardNameValue;
    private TextView mLblCardTypeValue;
    private TextView mLblCardDescValue;
    private TextView mLblBalance;
    private TextView mLblBalanceValue;
    private static EditText mTxtBarcode;
    private boolean lockScreen = true;

    VDOFragment vdo = new VDOFragment();
    ImageFragment img = new ImageFragment();
    private View mFragment;
    private String deviceId;
    private String barcode;

    private String WCFHost;
    private String WCFPost;
    private String EncodePWD;
    private CountDownTimer countDownTimer;
    private TextView mLblCompName;
    private Handler handler;
    private Runnable runClearLabel;

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean GetAccountPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (GetAccountPermission) {

                    }

                    if (ReadPhoneStatePermission) {

                    }

                    if (GetAccountPermission && ReadPhoneStatePermission) {
                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        deviceId = telephonyManager != null ? telephonyManager.getDeviceId() : null;
                        Toast.makeText(CheckPriceActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CheckPriceActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }

                }

                break;
        }
    }


    protected class GetDataRep_ProdInfoJS extends AsyncTask<String, Void, String> {
//        private ProgressDialog dialog = new ProgressDialog(CheckPriceActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            dialog.setMessage("Please wait , verify terminal.");
//            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("Return")) {
                    if (json.getString("Exception").equals("")) {

                        JSONObject data = new JSONObject(json.getString("Data"));
                        String CompanyName = data.getString("CompanyName");
                        String CardType = data.getString("ProdCode");
                        String CardDesc = data.getString("ProdName");
                        String PriceLabel = data.getString("PriceLabel");
                        String PricePos = data.getString("PricePos");

                        mLblCardTypeValue.setText(CardType);
                        mLblCardTypeValue.setText(CardType);
                        mLblCardDescValue.setText(CardDesc);
                        mLblBalance.setText(PriceLabel);
                        mLblBalanceValue.setText(PricePos);
                    } else {
                        mLblCardTypeValue.setText("");
                        mLblCardDescValue.setText(getResources().getString(R.string.alert_barcode_not_found));
                        mLblBalance.setText("");
                        mLblBalanceValue.setText("");
//                        showDialogDataInfo(getResources().getString(R.string.app_name), "Card not found.");
                    }
                    if (barcode.equals(Prefs.getString(ConfigBean.COLUMN_PWD_TOUCHLOCK, "USER275"))) {
                        hideSystemUI(true);
                        lockScreen = !lockScreen;
                        blockTouch(lockScreen);
                        if (lockScreen) {
                            Toast.makeText(CheckPriceActivity.this, "Touch screen lock.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CheckPriceActivity.this, "Touch screen unlock.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.i("dlg", json.getString("Exception"));
                    showDialogDataInfo(getResources().getString(R.string.app_name), json.getString("Exception"));
                }
                mLblCardNameValue.setText(barcode);
                mTxtBarcode.setText("");
                mTxtBarcode.requestFocus();

            } catch (Exception aE) {
                Log.d("dlg", "onPostExecute  Exception : " + aE.getMessage());
            }

//            dialog.dismiss();

        }

        @Override
        protected String doInBackground(String... params) {
            String output = CallWebService.GetDataRep_ProdInfoJS(params[0], params[1], params[2], params[3], params[4]);
            Log.i("dlg", "doInBackground: " + output);
            return output;
        }
    }

    protected class CloseLicModule extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("Return")) {
                    if (json.getString("Exception").equals("")) {

                    } else {
                        showDialogDataInfo(getResources().getString(R.string.app_name), json.getString("Exception"));
                    }
                } else {
                    Log.i("dlg", json.getString("Exception"));
                    showDialogDataInfo(getResources().getString(R.string.app_name), json.getString("Exception"));
                }

            } catch (Exception aE) {
                Log.d("dlg", "onPostExecute  Exception : " + aE.getMessage());
            }

//            dialog.dismiss();

        }

        @Override
        protected String doInBackground(String... params) {
            String output = CallWebService.CloseLicModule(params[0], params[1], params[2], params[3]);
            Log.i("dlg", "CloseLicModule: " + output);
            return output;
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
        setContentView(R.layout.activity_price_check);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        hideSystemUI(lockScreen);
        blockTouch(lockScreen);

        handler = new Handler();

        //EnableRuntimePermission();
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        WCFHost = Prefs.getString(ConfigBean.COLUMN_WCF_HOST, "");
        WCFPost = Prefs.getString(ConfigBean.COLUMN_WCF_PORT, "");
        EncodePWD = Prefs.getString(ConfigBean.COLUMN_ENCODE_PWD, "");

        //declare
        String appPath = Utils.GetAppPath(getApplicationContext());
        String marqueeFolder = Prefs.getString(ConfigBean.COLUMN_MARQUEE_FOLDER, "");
        String MarQueeFile = Prefs.getString(ConfigBean.COLUMN_MARQUEE_FILE, "");
        int MarQueeLoop = Integer.valueOf(Prefs.getString(ConfigBean.COLUMN_MARQUEE_LOOP, "1"));
        String SDCardMarqueefile = appPath + "/" + marqueeFolder + "/" + MarQueeFile;

        //Bingdin Widget
        mLblCompName = findViewById(R.id.lblCompName);
        mLblCardNameValue = findViewById(R.id.lblCardNameValue);
        mLblCardTypeValue = findViewById(R.id.lblCardTypeValue);
        mLblCardDescValue = findViewById(R.id.lblCardDescValue);
        mLblBalance = findViewById(R.id.lblBalance);
        mLblBalanceValue = findViewById(R.id.lblBalanceValue);
        mTxtBarcode = findViewById(R.id.txtBarcode);

        mFragment = findViewById(R.id.fragment);

        if (Prefs.getBoolean(ConfigBean.COLUMN_IS_USE_VDO, true)) {
            openFragment(img);
        } else {
            openFragment(vdo);
        }

//        mLblCompName.setText(Prefs.getString(TerminalBean.COLUMN_COMPANY_NAME, ""));

        mTxtBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    //do something here
                    barcode = mTxtBarcode.getText().toString();
                    Log.d("dlg", "deviceId: " + deviceId);
                    new GetDataRep_ProdInfoJS().execute(WCFHost, WCFPost, EncodePWD, deviceId, barcode);
//                    countDownTimer.cancel();

                    handler.removeCallbacks(runClearLabel);
                    runClearLabel = new Runnable() {
                        @Override
                        public void run() {
                            mLblCardNameValue.setText("");
                            mLblCardDescValue.setText("");
                            mLblCardTypeValue.setText("");
                            mLblBalance.setText("");
                            mLblBalanceValue.setText("");
                            handler.removeCallbacks(runClearLabel);
                        }
                    };
                    handler.postDelayed(runClearLabel, 20000);
                    return true;
                }
                return false;
            }
        });

        File file_marquee = new File(SDCardMarqueefile);
        if (file_marquee.exists()) {
            String text = readTextFilePath(SDCardMarqueefile, MarQueeLoop);
            MarqueeTextFragment marqueeTextFragment = new MarqueeTextFragment();
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

//        getCurrentFocus();
//        View current = getCurrentFocus();
//        if (current != null) current.clearFocus();
//
        hideVirtualKeyboard(mTxtBarcode);
        mTxtBarcode.setText("");
//        mTxtBarcode.requestFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestFocusEditText();
    }

    @Override
    protected void onStop() {
        new CloseLicModule().execute(WCFHost, WCFPost, EncodePWD, deviceId);
        super.onStop();
    }

    public void requestFocusEditText() {
        mTxtBarcode.requestFocus();
    }


    public void EnableRuntimePermission() {

        ActivityCompat.requestPermissions(CheckPriceActivity.this, new String[]
                {
                        GET_ACCOUNTS,
                        READ_PHONE_STATE
                }, RequestPermissionCode);

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
            }
        });

        handler.postDelayed(runnable, 3000);
    }

    private void hideVirtualKeyboard(EditText editText) {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextIsSelectable(true);
    }

    public void hideSystemUI(boolean b) {
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
