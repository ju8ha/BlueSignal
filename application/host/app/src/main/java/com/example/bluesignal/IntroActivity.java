package com.example.bluesignal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    //시작 화면 액티비티
    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onResume(){
        super.onResume();
        handler.postDelayed(r,2000);
    }

    @Override
    protected void onPause(){
        super.onPause();
        handler.removeCallbacks(r);
    }
}