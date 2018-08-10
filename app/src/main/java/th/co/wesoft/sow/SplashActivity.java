package th.co.wesoft.sow;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import th.co.wesoft.sow.Class.Utils;
import th.co.wesoft.sow.Interface.AsyncResponse;

public class SplashActivity extends AppCompatActivity {

    private File dir;
    private DownloadTask downloadTask;
    private boolean isAppUpdate;
    private File appFile;
    private HashSet folders;
    private String programFolder;


    class DownloadTask extends AsyncTask<String, Void, String> {

        public AsyncResponse delegate = null;

        public DownloadTask(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... strings) {
//            Log.i("dlg", "doInBackground: start");
            UpdateSource(strings[0], strings[1], strings[2], strings[3]);
//            Log.i("dlg", "doInBackground: finish");
            return "return doInBackground";
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
//            Log.i("dlg", "onPostExecute: start");
            delegate.processFinish(s);
//            Log.i("dlg", "onPostExecute: finish");
        }
    }

    // Check Runtime Permission -- BEGIN
    public void checkRuntimPermission() {
        Nammu.init(this);
        // Check Runtime Permission
        Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                Log.i("dlg", "permissionGranted: "+"Manifest.permission.WRITE_EXTERNAL_STORAGE - Granted");
//                Toast.makeText(MainActivity.this, "Manifest.permission.WRITE_EXTERNAL_STORAGE - Granted", Toast.LENGTH_SHORT).show();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        checkRuntimPermission();

        new Prefs.Builder()
                .setContext(getApplicationContext())
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

//        Config Folder
//        HashSet<String> strSet = new HashSet<String>();
//        strSet.add("Programs");
//        strSet.add("VDO");
//        strSet.add("Photo");
//        strSet.add("Marquee");
//
//        Prefs.putString(ConfigBean.COLUMN_PROGRAM_FOLDER, "");
//        Prefs.putString(ConfigBean.COLUMN_MARQUEE_FOLDER, "");
//        Prefs.putString(ConfigBean.COLUMN_IMAGE_FILE, "");
//        Prefs.putString(ConfigBean.COLUMN_VDO_FILE, "");
//        Prefs.putStringSet(ConfigBean.COLUMN_FOLDER, new HashSet<String>());

        programFolder = Prefs.getString(ConfigBean.COLUMN_PROGRAM_FOLDER, "");
        String SMBServer = Prefs.getString(ConfigBean.COLUMN_SMB_HOST, "");
        String SMBUser = Prefs.getString(ConfigBean.COLUMN_SMB_USER, "");
        String SMBPass = Prefs.getString(ConfigBean.COLUMN_SMB_PASS, "");

        folders = (HashSet) Prefs.getStringSet(ConfigBean.COLUMN_FOLDER, new HashSet<String>());

        if (folders.isEmpty()) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(i);
            finish();
            return;
        }

        for (Object folder : folders) {
            Log.i("dlg", "Folder: " + String.valueOf(folder));
        }
        blink();
        String appPath = Utils.GetAppPath(getApplicationContext());
        dir = new File(appPath);
        Utils.CreateDirectory(dir);

//        Uri myuri;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
//            myuri = Uri.parse("file://"+outapk);
//        } else {
//            File o = new File(outapk);
//            myuri = FileProvider.getUriForFile(con, con.getApplicationContext().getPackageName() + ".provider", o);
//        }
//        Intent promptInstall = new Intent(Intent.ACTION_VIEW).setDataAndType(myuri,"application/vnd.android.package-archive");
//        con.startActivity(promptInstall);

        downloadTask = new DownloadTask(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                if (isAppUpdate) {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                    finish();
                    System.exit(0);
                } else {
//                    Toast.makeText(SplashActivity.this, "Download Complete.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });
        downloadTask.execute("smb://" + SMBServer + "/", SMBUser, SMBPass, appPath);
    }


    public void UpdateSource(String strSMBUrl, String strSMBUser, String strSMBPass, String appPath) {
        try {
            SmbFile SremoteFile;
            if (strSMBUser.trim() == "") {
                SremoteFile = new SmbFile(strSMBUrl);
            } else {
                NtlmPasswordAuthentication Sauth = new NtlmPasswordAuthentication(null, strSMBUser, strSMBPass);
                SremoteFile = new SmbFile(strSMBUrl, Sauth);
            }
            SmbFile[] files = SremoteFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    String folder = files[i].getName().replace("/", "");
                    if (folders.contains(folder)) {
                        Log.i("dlg", "Folder: " + folder);
                        File dir = new File(appPath + "/" + folder);
                        Utils.CreateDirectory(dir);
                        List<String> servFile=new ArrayList<>();
                        for (SmbFile file : files[i].listFiles()) {
                            String filename = file.getName();
                            servFile.add(filename);
                            File sdfile = new File(dir, filename);
                            boolean bo = Utils.DownloadFileFormSMB(file, sdfile);
                            if (folder.toLowerCase().contains(programFolder.toString().toLowerCase())) {
                                appFile = sdfile;
                                isAppUpdate = bo;
                            }
                        }
                        for (File file : dir.listFiles()) {
                            if (!servFile.contains(file.getName())) {
                                file.delete();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void blink() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 350;    //in milissegunds
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView txt = findViewById(R.id.txtBlink);
                        if (txt.getVisibility() == View.VISIBLE) {
                            txt.setVisibility(View.INVISIBLE);
                        } else {
                            txt.setVisibility(View.VISIBLE);
                        }
                        blink();
                    }
                });
            }
        }).start();
    }
}
