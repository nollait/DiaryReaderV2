package com.example.diaryreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // запуск окна с логином
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}