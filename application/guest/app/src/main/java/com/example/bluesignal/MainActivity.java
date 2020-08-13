package com.example.bluesignal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //홈 화면 엑티비티

    Button visit_log_button;
    Button bluetooth_start_button;

    BluetoothManager manager;
    MyBluetoothLeScanner scanner;

    String guest_id;
    String guest_pw;
    String guest_name;
    String guest_birth;
    String guest_phnNumber;
    String guest_state;
    String is_survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visit_log_button = (Button)findViewById(R.id.visit_log_button);
        bluetooth_start_button = (Button)findViewById(R.id.bluetooth_start_button);

        Intent intent = getIntent();
        guest_id = intent.getExtras().getString("guest_id");

        GetGuestInfoByServer();

        manager = (BluetoothManager)this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        scanner = new MyBluetoothLeScanner(manager,this.getApplicationContext(), this);

        visit_log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여긴 이제 사용자 일정 리스트 엑티비티 띄워주는 기능을 하면 됨
                Intent intent = new Intent(MainActivity.this, VisitLogActivity.class);
                startActivity(intent);
            }
        });


        bluetooth_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scanner.startScan();
              
                bluetooth_start_button.setEnabled(false);
              
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scanner.stopScan();
//                        printText.setText(scanner.result());
                        System.out.println(scanner.result());
                        if(IsThereAnyInput(scanner.result())){  // input이 적절한 값이 들어왔을 경우
                            if(IsThereAnyReport()){ // 문진표를 작성했을 경우
                                OpenVisitCard();
                            }
                            else{   //문진표를 작성하지 못했을 경우
                                WriteReport();
                            }
                        }
                        else{
                            // Toast Message "스캔 실패"
                        }
                    }
                },10000);

            }
        });
    }

    private void GetGuestInfoByServer() {
        //게스트 정보 서버에서 가져오기
        Intent intent = getIntent();
        guest_id = intent.getStringExtra("guest_id");
        guest_pw = intent.getStringExtra("guest_pw");
        guest_name = intent.getStringExtra("guest_name");
        guest_birth = intent.getStringExtra("guest_birth");
        guest_phnNumber = intent.getStringExtra("guest_phnNumber");
        guest_state = intent.getStringExtra("guest_state");
        is_survey = intent.getStringExtra("is_survey");
    }

    private Boolean WriteReport() {
        // 리포트(문진표) 액티비티 띄우기
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        startActivityForResult(intent,0);
        return true;
    }

    private void OpenVisitCard() {
        Intent intent = new Intent(MainActivity.this, VisitCardActivity.class);

        intent.putExtra("guest_name",guest_name);
        intent.putExtra("guest_phnNumber",guest_phnNumber);

        startActivity(intent);
    }

    private boolean IsThereAnyReport() {
        return false;
    }

    private boolean IsThereAnyInput(String input){
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                String test = data.getStringExtra("result");
                Toast.makeText(this,test,Toast.LENGTH_SHORT).show();

                if(guest_state.equals("UnInfected")){ //문진표 띄우고 상태가 문진표 정상 상태일 경우
                        OpenVisitCard();    //출입증 띄우기
                }else if(guest_state.equals("Infected")) {
                        //감염됨-> 방문불가
                    Toast.makeText(this,"방문이 불가능합니다",Toast.LENGTH_SHORT).show();
                }
                else{
                    //error// 문진표에서 문제가 발생함! 토스트 메세지? 또는 알림창?
                    Toast.makeText(this,"치명적인 에러 발생!",Toast.LENGTH_SHORT).show();
                }
            }
        }
        bluetooth_start_button.setEnabled(true);
    }
}