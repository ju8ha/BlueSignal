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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //홈 화면 엑티비티

    Button visit_log_button;
    Button bluetooth_start_button;

    BluetoothManager manager;
    MyBluetoothLeScanner scanner;

    GuestInfo guestInfo = GuestInfo.getInstance();

    String guest_id, host_id, time1, date1, guest_status, host_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visit_log_button = (Button)findViewById(R.id.visit_log_button);
        bluetooth_start_button = (Button)findViewById(R.id.bluetooth_start_button);

        //GetGuestInfoByServer();

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
                        bluetooth_start_button.setEnabled(true);
                    }
                },10000);


                guest_id = guestInfo.getId();
                host_id = "1";
                time1 = "2";
                date1 = "3";
                guest_status = "4";
                host_status = "5";

                        Response.Listener<String> responseListener=new Response.Listener<String>() {//volley
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jasonObject=new JSONObject(response);//Register2 php에 response
                                    boolean success=jasonObject.getBoolean("success");//Register2 php에 sucess
                                    if (success) {//회원등록 성공한 경우
                                    }
                                    else{//회원등록 실패한 경우
                                        Toast.makeText(getApplicationContext(),"회원 등록 실패",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //서버로 volley를 이용해서 요청을 함
                    RecordRequestActivity registerRequest=new RecordRequestActivity(guest_id, host_id, time1, date1, guest_status,host_status, responseListener);
                        RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
                        queue.add(registerRequest);
                    }
                });

            }

    private Boolean WriteReport() {
        // 리포트(문진표) 액티비티 띄우기
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        startActivityForResult(intent,0);
        return true;
    }

    private void OpenVisitCard() {
        Intent intent = new Intent(MainActivity.this, VisitCardActivity.class);
        startActivity(intent);
    }

    private boolean IsThereAnyReport() {
        return false;
    }

    private boolean IsThereAnyInput(String input){
        return true;
    }
}