package th.co.wesoft.sow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private AppCompatButton mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTxtUsername = (EditText) findViewById(R.id.txtUsername);
        mTxtPassword = (EditText) findViewById(R.id.txtPassword);
        mBtnLogin = (AppCompatButton) findViewById(R.id.btnLogin);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mTxtUsername.getText().toString();
                String pass = mTxtPassword.getText().toString();

                if (Objects.equals("admin", user) && Objects.equals("admin", pass)) {
                    Intent i = new Intent(getApplicationContext(), ConfigActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "username or password incorrect.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
