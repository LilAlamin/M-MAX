package com.example.ta_sinarmas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {
    EditText user , pass,etconfpass;
    Button Reg;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        db = new DBHelper(this);
        etconfpass = (EditText) findViewById(R.id.etConfPass);
        Reg = findViewById(R.id.register);

        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String cnfpas = etconfpass.getText().toString().trim();

                if(password.equals(cnfpas)){

                    long val = db.addUser(username,password);
                    if (val > 0) {
                        Toast.makeText(RegisterActivity.this, "Anda Telah Terdafar", Toast.LENGTH_SHORT).show();
                        Intent moveTologin =  new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(moveTologin);
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Pendaftaran Gagal", Toast.LENGTH_SHORT).show();
                    }

                }

                else{
                    Toast.makeText(RegisterActivity.this, "Password Tidak Sesuai", Toast.LENGTH_SHORT).show();

                }

            }
        });;
    }
}