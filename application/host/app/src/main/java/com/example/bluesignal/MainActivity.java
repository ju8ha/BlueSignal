package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //홈 화면 엑티비티
    Button bluetooth_start_button;
    Button visit_log_button;
    String myID;

    BluetoothManager manager;
    MyBluetoothLeAdvertiser advertiser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetooth_start_button = (Button)findViewById(R.id.bluetooth_start_button);
        visit_log_button = (Button)findViewById(R.id.visit_log_button);

        advertiser = new MyBluetoothLeAdvertiser(this.getApplicationContext());

        myID = "test"; //이부분은 추후에 인텐트로 로그인 시 해당정보를 메인엑티비티에 저장하도록 해야함

        bluetooth_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertiser.startAdvertise(myID);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        advertiser.stopAdvertise();
                    }
                },10000);
            }
        });

        visit_log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //내역 리스트로 이동
            }
        });
    }
}