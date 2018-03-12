package th.co.wesoft.sow;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.HashSet;

import th.co.wesoft.sow.Class.MyContextWrapper;

public class ConfigActivity extends AppCompatActivity {

    private EditText mTxtSMBServer;
    private EditText mTxtImageFile;
    private EditText mTxtVDOFile;
    private EditText mTxtMarQueeFile;
    private EditText mTxtMarQueeLoop;
    //    private EditText mTxtProductType;
    private EditText mTxtWCFHost;
    private EditText mTxtWCFPost;
    private Spinner spnDelimiter;
    private EditText mTxtDelimiter;
    private EditText mTxtEncodePWD;
    private EditText mTxtSocketPort;
    private Spinner spnTheme;
    private Button mBtnSave;
    private AwesomeValidation awesomeValidation;
    private EditText mTxtSMBUser;
    private EditText mTxtSMBPass;
    private RadioButton mRdoImage;
    private RadioButton mRdoVDO;
    private EditText mTxtProgramFolder;
    private EditText mTxtMarqueeFolder;
    private EditText mTxtImageLoop;
    private EditText mTxtPwdTouchLock;
    private Spinner mSpnProductType;
    private RadioButton mEnglish;
    private RadioButton mThailand;
    private LinearLayout mLayWCFHost;
    private LinearLayout mLayPWD;
    private LinearLayout mLaySocket;
    private LinearLayout mSocketDelimiter;
    private TextView mTvWCFHost;
    private TextView mTvSocketPort;
    private SeekBar marqueeSpeed;
    private int speed;
    private TextView marqueeSpeedValue;
    //    private String theme;
//    private String delimiter;


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
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config);

        new Prefs.Builder()
                .setContext(getApplicationContext())
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        mTvWCFHost = (TextView) findViewById(R.id.tvWCFHost);
        mLayWCFHost = (LinearLayout) findViewById(R.id.layWCFHost);
        mLayPWD = (LinearLayout) findViewById(R.id.layEncodePWD);
        mTvSocketPort = (TextView) findViewById(R.id.tvSocketPort);
        mLaySocket = (LinearLayout) findViewById(R.id.laySocketPort);
        mSocketDelimiter = (LinearLayout) findViewById(R.id.laySocketDelimiter);


        mEnglish = (RadioButton) findViewById(R.id.rdoEnglish);
        mThailand = (RadioButton) findViewById(R.id.rdoThailand);
        mTxtSMBServer = (EditText) findViewById(R.id.txtSMBServer);
        mTxtSMBUser = (EditText) findViewById(R.id.txtSMBUser);
        mTxtSMBPass = (EditText) findViewById(R.id.txtSMBPass);
        mRdoImage = (RadioButton) findViewById(R.id.rdoImage);
        mRdoVDO = (RadioButton) findViewById(R.id.rdoVDO);
        mTxtVDOFile = (EditText) findViewById(R.id.txtVDOFile);
        mTxtProgramFolder = (EditText) findViewById(R.id.txtProgramFolder);
        mTxtMarqueeFolder = (EditText) findViewById(R.id.txtMarqueeFolder);
        mTxtImageFile = (EditText) findViewById(R.id.txtImageFile);
        mTxtImageLoop = (EditText) findViewById(R.id.txtImageLoop);
        mTxtMarQueeFile = (EditText) findViewById(R.id.txtMarQueeFile);
        mTxtMarQueeLoop = (EditText) findViewById(R.id.txtMarQueeLoop);
//        mTxtProductType = (EditText) findViewById(R.id.txtProductType);
        mTxtWCFHost = (EditText) findViewById(R.id.txtWCFHost);
        mTxtWCFPost = (EditText) findViewById(R.id.txtWCFPost);
        mTxtEncodePWD = (EditText) findViewById(R.id.txtEncodePWD);
        mTxtSocketPort = (EditText) findViewById(R.id.txtSocketPort);
        mTxtPwdTouchLock = (EditText) findViewById(R.id.txtPwdTouchLock);

        marqueeSpeedValue = (TextView) findViewById(R.id.sb_value);
        marqueeSpeed = (SeekBar) findViewById(R.id.sbMarqueeSpeed);

        marqueeSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                progressChangedValue = progress;
//                speed = progress;
                marqueeSpeedValue.setText(String.valueOf(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() < 1) {
                    seekBar.setProgress(1);
                }
                marqueeSpeedValue.setText(String.valueOf(seekBar.getProgress()));
                Toast.makeText(ConfigActivity.this, String.valueOf(seekBar.getProgress()), Toast.LENGTH_SHORT).show();

            }
        });


        mBtnSave = (Button) findViewById(R.id.btnSave);


        mSpnProductType = (Spinner) findViewById(R.id.spnProductType);
        ArrayAdapter<CharSequence> adpProgramType = ArrayAdapter.createFromResource(this,
                R.array.programstype_array, android.R.layout.simple_spinner_item);
        adpProgramType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnProductType.setAdapter(adpProgramType);

        mSpnProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()) {
                    case "WF-DISP-SHOP-FC":
                    case "WF-DISP-SHOP-FL":
                    case "WP-DISP-POS":
                        mTvSocketPort.setVisibility(View.VISIBLE);
                        mLaySocket.setVisibility(View.VISIBLE);
                        mSocketDelimiter.setVisibility(View.VISIBLE);
                        mTvWCFHost.setVisibility(View.GONE);
                        mLayWCFHost.setVisibility(View.GONE);
                        mLayPWD.setVisibility(View.GONE);
                        break;
                    case "WF-CHECKPOINT-FC":
                    case "WF-CHECKPOINT-FL":
                    case "WP-CHECKPOINT":
                        mTvSocketPort.setVisibility(View.GONE);
                        mLaySocket.setVisibility(View.GONE);
                        mSocketDelimiter.setVisibility(View.GONE);
                        mTvWCFHost.setVisibility(View.VISIBLE);
                        mLayWCFHost.setVisibility(View.VISIBLE);
                        mLayPWD.setVisibility(View.VISIBLE);
                        break;
                    default:
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnDelimiter = (Spinner) findViewById(R.id.delimiter_spinner);
        ArrayAdapter<CharSequence> adpDelimiter = ArrayAdapter.createFromResource(this,
                R.array.delimiter_array, android.R.layout.simple_spinner_item);
        adpDelimiter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDelimiter.setAdapter(adpDelimiter);

        spnDelimiter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                boolean bo = false;
//                switch (parent.getItemAtPosition(position).toString()) {
//                    case "Comma":
//                        delimiter = ",";
//                        break;
//                    case "Tab":
//                        delimiter = "\t";
//                        break;
//                    case "SemiColon":
//                        delimiter = ";";
//                        break;
//                    case "Space":
//                        delimiter = " ";
//                        break;
//                    case "Pipe":
//                        delimiter = "\\|";
//                        break;
//                    case "Manual":
//                        bo = true;
//                        break;
//                    default:
//                        delimiter = "";
//                }
                if (parent.getItemAtPosition(position).toString().equals("Manual")) {
                    mTxtDelimiter.setVisibility(View.VISIBLE);
                } else {
                    mTxtDelimiter.setText("");
                    mTxtDelimiter.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnTheme = (Spinner) findViewById(R.id.spnColor);
        ArrayAdapter<CharSequence> adpTheme = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        adpTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTheme.setAdapter(adpTheme);

        mTxtDelimiter = (EditText) findViewById(R.id.txtDelimiter);
        mTxtDelimiter.setVisibility(View.INVISIBLE);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.txtSMBServer, "[a-zA-Z0-9./\\\\]+", R.string.validate_text);
//        awesomeValidation.addValidation(this, R.id.txtSMBUser, "[a-zA-Z][a-zA-Z0-9]*", R.string.validate_text);
//        awesomeValidation.addValidation(this, R.id.txtSMBPass, "[a-zA-Z0-9.!@#$%&/\\\\]+", R.string.validate_text);
        awesomeValidation.addValidation(this, R.id.txtProgramFolder, "[a-zA-Z0-9.]+", R.string.validate_text);
        awesomeValidation.addValidation(this, R.id.txtMarqueeFolder, "[a-zA-Z0-9.]+", R.string.validate_text);
        awesomeValidation.addValidation(this, R.id.txtImageFile, "[a-zA-Z0-9.]+", R.string.validate_text);
        awesomeValidation.addValidation(this, R.id.txtImageLoop, "\\d+", R.string.validate_text);
        awesomeValidation.addValidation(this, R.id.txtVDOFile, "[a-zA-Z0-9.]+", R.string.validate_text);
        awesomeValidation.addValidation(this, R.id.txtMarQueeFile, "[a-zA-Z0-9.]+", R.string.validate_text);
        awesomeValidation.addValidation(this, R.id.txtMarQueeLoop, "\\d+", R.string.validate_text);
//        awesomeValidation.addValidation(this, R.id.txtProductType, "[a-zA-Z]+", R.string.validate_text);
//        awesomeValidation.addValidation(this, R.id.txtWCFHost, "[a-zA-Z0-9.]+", R.string.validate_text);
//        awesomeValidation.addValidation(this, R.id.txtWCFPost, "\\d+", R.string.validate_text);
//        awesomeValidation.addValidation(this, R.id.txtEncodePWD, "[a-zA-Z0-9.!@#$%&/\\\\]+", R.string.validate_text);
//        awesomeValidation.addValidation(this, R.id.txtSocketPort, "\\d+", R.string.validate_text);
        awesomeValidation.addValidation(this, R.id.txtPwdTouchLock, "[a-zA-Z0-9.!@#$%&/\\\\]+", R.string.validate_text);

        if (Prefs.getString(ConfigBean.COLUMN_LANGUAGE, "th").equals("en")) {
            mEnglish.setChecked(true);
        } else {
            mThailand.setChecked(true);
        }

        mTxtSMBServer.setText(Prefs.getString(ConfigBean.COLUMN_SMB_HOST, ""));
        mTxtSMBUser.setText(Prefs.getString(ConfigBean.COLUMN_SMB_USER, ""));
        mTxtSMBPass.setText(Prefs.getString(ConfigBean.COLUMN_SMB_PASS, ""));

        if (Prefs.getBoolean(ConfigBean.COLUMN_IS_USE_VDO, true)) {
            mRdoImage.setChecked(true);
        } else {
            mRdoVDO.setChecked(true);
        }

        mTxtProgramFolder.setText(Prefs.getString(ConfigBean.COLUMN_PROGRAM_FOLDER, "Programs"));
        mTxtMarqueeFolder.setText(Prefs.getString(ConfigBean.COLUMN_MARQUEE_FOLDER, "Marquee"));
        mTxtImageFile.setText(Prefs.getString(ConfigBean.COLUMN_IMAGE_FILE, "Photo"));
        mTxtImageLoop.setText(Prefs.getString(ConfigBean.COLUMN_IMAGE_LOOP, "10"));
        mTxtVDOFile.setText(Prefs.getString(ConfigBean.COLUMN_VDO_FILE, "VDO"));
        mTxtMarQueeFile.setText(Prefs.getString(ConfigBean.COLUMN_MARQUEE_FILE, "marquee.txt"));
        mTxtMarQueeLoop.setText(Prefs.getString(ConfigBean.COLUMN_MARQUEE_LOOP, "1"));
        marqueeSpeed.setProgress(Prefs.getInt(ConfigBean.COLUMN_MARQUEE_SPEED, 8));
//        mTxtProductType.setText(Prefs.getString(ConfigBean.COLUMN_PROD_TYPE, ""));
        mSpnProductType.setSelection(adpProgramType.getPosition(Prefs.getString(ConfigBean.COLUMN_PROD_TYPE, "WF-DISP-SHOP-FC")));
        mTxtWCFHost.setText(Prefs.getString(ConfigBean.COLUMN_WCF_HOST, "192.168.1.1"));
        mTxtWCFPost.setText(Prefs.getString(ConfigBean.COLUMN_WCF_PORT, "1002"));
        spnDelimiter.setSelection(adpDelimiter.getPosition(Prefs.getString(ConfigBean.COLUMN_DELIMITER, "Pipe")));
        mTxtEncodePWD.setText(Prefs.getString(ConfigBean.COLUMN_ENCODE_PWD, ""));
        mTxtSocketPort.setText(Prefs.getString(ConfigBean.COLUMN_SOCKET_PORT, "8889"));
        spnTheme.setSelection(adpTheme.getPosition(Prefs.getString(ConfigBean.COLUMN_THEME, "Dark Blue")));
        mTxtDelimiter.setText(Prefs.getString(ConfigBean.COLUMN_DELIMITER_TEXT, ""));
        mTxtPwdTouchLock.setText(Prefs.getString(ConfigBean.COLUMN_PWD_TOUCHLOCK, "123456"));

//        mTxtProductType.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    if (spnDelimiter.getSelectedItem().toString().equals("Manual")) {
                        if (mTxtDelimiter.getText().toString().trim().equalsIgnoreCase("")) {
                            mTxtDelimiter.setError(getString(R.string.validate_text));
                            return;
                        }
                    }

                    switch (mSpnProductType.getSelectedItem().toString()) {
                        case "WF-DISP-SHOP-FC":
                        case "WF-DISP-SHOP-FL":
                        case "WP-DISP-POS":
                            if (mTxtSocketPort.getText().toString().trim().equalsIgnoreCase("")) {
                                mTxtSocketPort.setError(getString(R.string.validate_text));
                                return;
                            }
                            break;
                        case "WF-CHECKPOINT-FC":
                        case "WF-CHECKPOINT-FL":
                        case "WP-CHECKPOINT":
                            if (mTxtWCFHost.getText().toString().trim().equalsIgnoreCase("")) {
                                mTxtWCFHost.setError(getString(R.string.validate_text));
                                return;
                            }
                            if (mTxtWCFPost.getText().toString().trim().equalsIgnoreCase("")) {
                                mTxtWCFPost.setError(getString(R.string.validate_text));
                                return;
                            }
                            if (mTxtEncodePWD.getText().toString().trim().equalsIgnoreCase("")) {
                                mTxtEncodePWD.setError(getString(R.string.validate_text));
                                return;
                            }
                            break;
                        default:
                    }

//                    Toast.makeText(ConfigActivity.this, spnDelimiter.getSelectedItem().toString()+", "+ getDelimiter(spnDelimiter.getSelectedItem().toString()), Toast.LENGTH_SHORT).show();
                    Prefs.putString(ConfigBean.COLUMN_SMB_HOST, mTxtSMBServer.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_SMB_USER, mTxtSMBUser.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_SMB_PASS, mTxtSMBPass.getText().toString());
                    Prefs.putBoolean(ConfigBean.COLUMN_IS_USE_VDO, mRdoImage.isChecked());
                    Prefs.putString(ConfigBean.COLUMN_PROGRAM_FOLDER, mTxtProgramFolder.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_MARQUEE_FOLDER, mTxtMarqueeFolder.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_IMAGE_FILE, mTxtImageFile.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_IMAGE_LOOP, mTxtImageLoop.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_VDO_FILE, mTxtVDOFile.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_MARQUEE_FILE, mTxtMarQueeFile.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_MARQUEE_LOOP, mTxtMarQueeLoop.getText().toString());
                    Prefs.putInt(ConfigBean.COLUMN_MARQUEE_SPEED, marqueeSpeed.getProgress());
                    Prefs.putString(ConfigBean.COLUMN_PROD_TYPE, mSpnProductType.getSelectedItem().toString());
                    Prefs.putString(ConfigBean.COLUMN_WCF_HOST, mTxtWCFHost.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_WCF_PORT, mTxtWCFPost.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_DELIMITER, spnDelimiter.getSelectedItem().toString());
                    Prefs.putString(ConfigBean.COLUMN_DELIMITER_TEXT, getDelimiter(spnDelimiter.getSelectedItem().toString()));
                    Prefs.putString(ConfigBean.COLUMN_ENCODE_PWD, mTxtEncodePWD.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_SOCKET_PORT, mTxtSocketPort.getText().toString());
                    Prefs.putString(ConfigBean.COLUMN_THEME, spnTheme.getSelectedItem().toString());
                    Prefs.putString(ConfigBean.COLUMN_PWD_TOUCHLOCK, mTxtPwdTouchLock.getText().toString());

                    HashSet<String> strSet = new HashSet<String>();
                    strSet.add(mTxtProgramFolder.getText().toString());
                    strSet.add(mTxtMarqueeFolder.getText().toString());
                    strSet.add(mTxtImageFile.getText().toString());
                    strSet.add(mTxtVDOFile.getText().toString());

                    Prefs.putStringSet(ConfigBean.COLUMN_FOLDER, strSet);

                    Toast.makeText(ConfigActivity.this, "Updated Config ", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });

//        Button read = (Button) findViewById(R.id.btnRead);
//        read.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                String toast = Prefs.getString(ConfigBean.COLUMN_DELIMITER, "no save");
////                String toast2 = Prefs.getString(ConfigBean.COLUMN_DELIMITER_TEXT, "no save");
////                Toast.makeText(ConfigActivity.this, toast+", "+toast2, Toast.LENGTH_SHORT).show();
//                Toast.makeText(ConfigActivity.this, Prefs.getString(ConfigBean.COLUMN_DELIMITER, "Comma")+", "+Prefs.getString(ConfigBean.COLUMN_DELIMITER_TEXT,""), Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    private String getDelimiter(String value) {
        String ret;
        switch (value.toUpperCase()) {
            case "COMMA":
                ret = ",";
                break;
            case "TAB":
                ret = "\t";
                break;
            case "SEMICOLON":
                ret = ";";
                break;
            case "SPACE":
                ret = " ";
                break;
            case "PIPE":
                ret = "\\|";
                break;
            case "MANUAL":
                ret = mTxtDelimiter.getText().toString();
                break;
            default:
                ret = "";

        }
        return ret;
    }

    public void ChangeLanguage() {

        if (mEnglish.isChecked()) {
            Prefs.putString(ConfigBean.COLUMN_LANGUAGE, "en");
        } else {
            Prefs.putString(ConfigBean.COLUMN_LANGUAGE, "th");
        }
        Intent starterIntent = getIntent();
        finish();
        startActivity(starterIntent);
    }
}
