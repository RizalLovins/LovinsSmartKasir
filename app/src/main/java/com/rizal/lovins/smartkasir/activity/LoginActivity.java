package com.rizal.lovins.smartkasir.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rizal.lovins.smartkasir.R;
import com.rizal.lovins.smartkasir.util.PreferenceUtil;


public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
                    //pengisian data username ke PreferenceUtil...
                    PreferenceUtil.createLoginSession(LoginActivity.this, username);
                    startActivity(new Intent(LoginActivity.this,
                            MainActivity.class));
                    LoginActivity.this.finish();
                } else if (username.equalsIgnoreCase("kasir") && password.equalsIgnoreCase("kasir")) {
                    //pengisian data username ke PreferenceUtil...
                    PreferenceUtil.createLoginSession(LoginActivity.this, username);
                    startActivity(new Intent(LoginActivity.this,
                            KasirActivity.class));
                    LoginActivity.this.finish();
                } else if (username.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Username Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Useranme Dan Passoword tidak sesuai", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
