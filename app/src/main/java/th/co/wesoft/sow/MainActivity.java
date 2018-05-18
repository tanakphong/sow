package th.co.wesoft.sow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import th.co.wesoft.sow.Class.Utils;

public class MainActivity extends AppCompatActivity {

    private Button mBtnProgram;
    boolean doubleBackToExitPressedOnce = false;
    private String TermType, TermCategory, Active;
    private boolean hasTerminal;
    private String WCFHost;
    private String WCFPost;
    private String EncodePWD;
    private String Url;

    // Check Runtime Permission -- BEGIN
    public void checkRuntimPermission() {
        Nammu.init(this);
        // Check Runtime Permission
        Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                Toast.makeText(MainActivity.this, "Manifest.permission.WRITE_EXTERNAL_STORAGE - Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void permissionRefused() {
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    // Check Runtime Persion -- END

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    protected class GetTerminalInfoTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

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
//            dialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String output = CallWebService.GetTerminalInfoJS(params[0], params[1], params[2]);
            Log.i("dlg", "doInBackground: " + output);
            return output;
        }
    }


    protected class GetDataRep_ProdInfoJS extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

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
//            dialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String output = CallWebService.GetDataRep_ProdInfoJS(params[0], params[1], params[2], params[3], params[4]);
            Log.i("dlg", "doInBackground: " + output);
            return output;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Prefs.Builder()
                .setContext(getApplicationContext())
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        WCFHost = Prefs.getString(ConfigBean.COLUMN_WCF_HOST, "");
        WCFPost = Prefs.getString(ConfigBean.COLUMN_WCF_PORT, "");
        EncodePWD = Prefs.getString(ConfigBean.COLUMN_ENCODE_PWD, "");
        Url = "http://" + WCFHost + ":" + WCFPost + "/WFservice30?wsdl";

        mBtnProgram = findViewById(R.id.btnProgram);
        mBtnProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        Prefs.putString(TerminalBean.COLUMN_COMPANY_NAME, "");
        Prefs.putString(TerminalBean.COLUMN_TERM_DESC, "");
        Prefs.putString(TerminalBean.COLUMN_SRC_WELCOME, "");

        String prodType = Prefs.getString(ConfigBean.COLUMN_PROD_TYPE, "");

        HashSet<String> strings = new HashSet<>();
        strings.add("WF-CHECKPOINT-FC");
        strings.add("WF-CHECKPOINT-FL");
//        strings.add("WP-CHECKPOINT");

        if (strings.contains(prodType)) {
            if (checkTerminal(Url, EncodePWD)) {
                if (!hasTerminal) {
                    Log.i("dlg", "Terminal not found. ");
                    showDialogDataInfo(getResources().getString(R.string.app_name), "Terminal not found.");
                    return;
                }

                if (!checkLicence()) {
                    showDialogDataInfo(getResources().getString(R.string.app_name), "not found licence.");
                    return;
                }
                checkMenu(prodType);
            }
        } else {
            checkMenu(prodType);
        }
    }

    private void checkMenu(String prodType) {
        if (prodType == "") {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        } else {
            Intent i;
//            WF-DISP-SHOP-FC
//            WF-DISP-SHOP-FL
//            WF-CHECKPOINT-FC
//            WF-CHECKPOINT-FL
//            WP-CHECKPOINT
//            WP-DISP-POS
            switch (prodType) {
                case "WF-DISP-SHOP-FC":
                case "WF-DISP-SHOP-FL":
                case "WF-DISP-CASHIER":
                case "WP-DISP-POS":
                    i = new Intent(getApplicationContext(), FoodCourtActivity.class);
                    startActivity(i);
                    break;
                case "WF-CHECKPOINT-FC":
                case "WF-CHECKPOINT-FL":
                    i = new Intent(getApplicationContext(), CheckFoodCourtActivity.class);
                    startActivity(i);
                    break;
//                case "WP-DISP-POS":
//                    i = new Intent(getApplicationContext(), CashierActivity.class);
//                    startActivity(i);
//                    break;
                case "WP-CHECKPOINT":
                    i = new Intent(getApplicationContext(), CheckPriceActivity.class);
                    startActivity(i);
                    break;
                default:
                    Toast.makeText(this, "Product type not found.", Toast.LENGTH_SHORT).show();
                    i = new Intent(getApplicationContext(), ConfigActivity.class);
                    startActivity(i);
            }
        }
    }

    private boolean checkTerminal(String Url, String EncodePWD) {
        TermType = TermCategory = Active = "";
        hasTerminal = false;
        String ret = null;
        try {
            ret = new GetTerminalInfoTask().execute(Url, EncodePWD, Utils.getIPAddress()).get();
        } catch (Exception aE) {
            ret = "";
        }
        Log.i("dlg", "GetTerminalInfoTask: " + ret);

        try {
            JSONObject json = new JSONObject(ret);
            if (json.getString("Return").equals("true")) {
                Log.i("dlg", "return true");
                if (!json.getString("Data").trim().equals("")) {
                    Log.i("dlg", "return data" + json.getString("Data"));
                    JSONObject data = new JSONObject(json.getString("Data"));
                    Log.i("dlg", "data: " + data);

                    String CompanyName = data.getString("CompanyName");
                    String TermDesc = data.getString("TermDesc");
                    String ScrWelcome = data.getString("ScrWelcome");
                    TermType = data.getString("TermType");
                    TermCategory = data.getString("TermCategory");
                    Active = data.getString("Active");

                    Prefs.putString(TerminalBean.COLUMN_COMPANY_NAME, CompanyName);
                    Prefs.putString(TerminalBean.COLUMN_TERM_DESC, TermDesc);
                    Prefs.putString(TerminalBean.COLUMN_SRC_WELCOME, ScrWelcome);
//                    Prefs.putString(TerminalBean.COLUMN_TERM_TYPE, TermType);
//                    Prefs.putString(TerminalBean.COLUMN_TERM_CATEGORY, TermCategory);
//                    Prefs.putString(TerminalBean.COLUMN_ACTIVE, Active);

                    hasTerminal = true;
                } else {
//                    Log.i("dlg", "Terminal not found. ");
//                    showDialogDataInfo(getResources().getString(R.string.app_name), "Terminal not found.");
                    hasTerminal = false;
                }
                return true;
            } else {
                showDialogDataInfo(getResources().getString(R.string.app_name), json.getString("Exception"));
                return false;
            }
        } catch (JSONException e) {
            showDialogDataInfo(getResources().getString(R.string.app_name), e.getMessage());
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_get_ip:

                Toast.makeText(this, "IP Address :" + Utils.getIPAddress(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_get_display_metrics:

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int widthPixels = metrics.widthPixels;
                int heightPixels = metrics.heightPixels;
                float scaleFactor = metrics.density;
                float widthDp = widthPixels / scaleFactor;
                float heightDp = heightPixels / scaleFactor;
                Toast.makeText(this, "(widthPixels,heightPixels):" + (widthDp + ", " + heightDp), Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_about:

                new GetDataRep_ProdInfoJS().execute(WCFHost, WCFPost, EncodePWD, "DV01", "200001");

                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;
                    int verCode = pInfo.versionCode;
                    String applicationName = getResources().getString(R.string.app_name);

                    Toast.makeText(this, (applicationName + "@" + version), Toast.LENGTH_LONG).show();
                } catch (Exception aE) {

                }
                return false;
            case R.id.action_config:

                i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private boolean checkLicence() {
        if (TermType.equals("C") &&
                TermCategory.equals("T") &&
                Active.equals("Y")) {
            return true;
        } else {
            return false;
        }

    }
}
