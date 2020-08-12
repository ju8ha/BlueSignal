package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //홈 화면 엑티비티

    Button visit_log_button;
    Button bluetooth_start_button;

    BluetoothManager manager;
    MyBluetoothLeScanner scanner;

    //문진표 activity에서 해야함
    boolean visit_available = true; // 문진표 작성 -> 방문 가능-> 테스트를 위해 true로 해놓음

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visit_log_button = (Button)findViewById(R.id.visit_log_button);
        bluetooth_start_button = (Button)findViewById(R.id.bluetooth_start_button);

        manager = (BluetoothManager)this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        scanner = new MyBluetoothLeScanner(manager,this.getApplicationContext(), this);


        visit_log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여긴 이제 사용자 일정 리스트 엑티비티 띄워주는 기능을 하면 됨
            }
        });

        bluetooth_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner.startScan();


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scanner.stopScan();
//                        printText.setText(scanner.result());
                        System.out.println(scanner.result());
                    }
                },10000);



                //문진표 작성 activity에서 해야함  --> 문진표 작성 후 확인 눌렀을 때 !
                //-->방문 가능할 경우
                if(visit_available==true){
                    //방문증으로 전환
                    startActivity(new Intent(MainActivity.this, VisitCardActivity.class));
                }



            }
        });


    }
}