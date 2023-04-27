package com.example.ta_sinarmas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ta_sinarmas.activity.MainActivity;

public class LoginActivity extends AppCompatActivity {

    TextView regis;
    EditText user,pass;
    Button log;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        regis = findViewById(R.id.yuk_daftar);
        log = findViewById(R.id.button);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);

        db = new DBHelper(this);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString().trim();
                String password = pass.getText().toString().trim();

                Boolean res = db.checkUser(username,password);
                if(res == true){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regis = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regis);
            }
        });
    }
}