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
    String guest_name ="홍길동";
    String guest_phnNumber="010-1111-1111";

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

                //




            }
        });


    }

    private Boolean WriteReport() {
        // 리포트(문진표) 액티비티 띄우기
        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        startActivityForResult(intent,0);
        return true;
    }

    private void OpenVisitCard() {
        Intent intent = new Intent(MainActivity.this, VisitCardActivity.class);

        intent.putExtra("name",guest_name);
        intent.putExtra("phone_number",guest_phnNumber);

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

                if(test.equals("UnInfected")){ //문진표 띄우고 상태가 문진표 정상 상태일 경우
                        OpenVisitCard();    //출입증 띄우기
                }else if(test.equals("Infected")) {
                        //감염됨-> 방문불가
                    Toast.makeText(this,"방문이 불가능합니다",Toast.LENGTH_SHORT).show();
                }
                else{
                    //error// 문진표에서 문제가 발생함! 토스트 메세지? 또는 알림창?
                    Toast.makeText(this,"치명적인 에러 발생!",Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}