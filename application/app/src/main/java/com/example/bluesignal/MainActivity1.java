package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity1 extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
    }

    public void onSendSingalButtonClicked(View view) {
        startActivity(new Intent(MainActivity1.this, MainActivity2.class));
    }

    public void onVisitLogButtonClicked(View view){

    }


}