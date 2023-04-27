package com.example.ta_sinarmas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ta_sinarmas.activity.MainActivity;

public class ConfirmLogout extends AppCompatActivity {
    Button Iya,Kembali;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_logout);

        Iya = findViewById(R.id.button2);
        Kembali = findViewById(R.id.button3);

        Iya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keluar = new Intent(ConfirmLogout.this,LoginActivity.class);
                startActivity(keluar);
            }
        });

        Kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kembali = new Intent(ConfirmLogout.this, MainActivity.class);
                startActivity(kembali);
            }
        });
    }
}