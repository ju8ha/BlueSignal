package com.example.bluesignal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {
    //홈 화면 엑티비티

    Button visit_log_button;
    Button bluetooth_start_button;
    ImageView drawer_image;
    TextView guest_id_text;
    TextView main_name_text;
    TextView main_phnNumber_text;

    BluetoothManager manager;
    MyBluetoothLeScanner scanner;

    GuestInfo guestInfo = GuestInfo.getInstance();
    Activity thisA = this;

    private AppBarConfiguration mAppBarConfiguration;

    String host_id;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    String currentDateandTime = sdf.format(new Date());

    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
    String currentTime = sdf1.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_change_info,R.id.nav_sign_out,R.id.nav_withdrawal)
                .setDrawerLayout(drawer)
                .build();

        visit_log_button = (Button)findViewById(R.id.visit_log_button);
        bluetooth_start_button = (Button)findViewById(R.id.bluetooth_start_button);
        drawer_image = (ImageView)findViewById(R.id.drawerImage);

        main_name_text = (TextView)findViewById(R.id.main_name_text);
        main_phnNumber_text = (TextView)findViewById(R.id.main_phnNumber_text);

        manager = (BluetoothManager)this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        scanner = new MyBluetoothLeScanner(manager,this.getApplicationContext(), this);
        main_name_text.setText(guestInfo.getName());
        main_phnNumber_text.setText(guestInfo.getPhnNumber());

        drawer_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
                guest_id_text = (TextView)findViewById(R.id.guest_id_text);
                guest_id_text.setText(guestInfo.getId());
            }
        });

        visit_log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여긴 이제 사용자 일정 리스트 엑티비티 띄워주는 기능을 하면 됨
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jasonObject=new JSONObject(response);
                            boolean success=jasonObject.getBoolean("success");

                            if (success) {//회원등록 성공한 경우
                                String guest_id = jasonObject.getString("guest_id");
                                String host_id = jasonObject.getString("host_id");
                                String time1 = jasonObject.getString("time1");
                                String date1 = jasonObject.getString("date1");

                                Intent intent = new Intent(MainActivity.this, VisitLogActivity.class);

                                guestInfo.setVisitInfo(guest_id, host_id, time1, date1);

                                startActivity(intent);
                            }
                            else{//회원등록 실패한 경우
                                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                return;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                VisitRequest visitRequest=new VisitRequest(guestInfo.getId(), responseListener);
                RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
                queue.add(visitRequest);
            }
        });


        bluetooth_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner.startScan();

                bluetooth_start_button.setEnabled(false);
                bluetooth_start_button.setBackgroundColor(Color.parseColor("#58ACFA"));

                Handler handler = new Handler();

                CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        bluetooth_start_button.setText(String.format(Locale.getDefault(), "%d 초", millisUntilFinished / 1000L));
                    }

                    public void onFinish() {
                        bluetooth_start_button.setText("입장권 발급");
                    }
                }.start();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scanner.stopScan();
                        host_id = scanner.result();
                        if(IsThereAnyInput(host_id)){  // input이 적절한 값이 들어왔을 경우
                            if(IsThereAnyReport(currentDateandTime)){ // 문진표를 작성했을 경우
                                OpenVisitCard();
                            }
                            else{   //문진표를 작성하지 못했을 경우
                                WriteReport();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"스캔 실패",Toast.LENGTH_SHORT).show();
                        }
                        bluetooth_start_button.setEnabled(true);
                        bluetooth_start_button.setBackgroundColor(Color.parseColor("#4486c0"));
                    }
                },10000);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_change_info:
                        Intent intent1 = new Intent(getApplicationContext(), ChangeInfoActivity.class);
                        startActivityForResult(intent1,1);
                        break;
                    case R.id.nav_withdrawal:   // 계정 탈퇴
                        Intent intent2 = new Intent(MainActivity.this, WithdrawalActivity.class);
                        startActivityForResult(intent2,1);
                        break;
                    case R.id.nav_sign_out:
                        guestInfo.deleteAllInfo();
                        Intent intent3 = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivityForResult(intent3,1);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
                }
                return true;
            }
        });
    }

    private Boolean WriteReport() {
        //서버에 정보 보내기!
        // 리포트(문진표) 액티비티 띄우기
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        startActivityForResult(intent,0);
        return true;
    }

    private void OpenVisitCard() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {//회원등록 성공한 경우
                        Intent intent = new Intent(MainActivity.this, VisitCardActivity.class);
                        startActivity(intent);
                    }
                    else{//회원등록 실패한 경우
                        Toast.makeText(getApplicationContext(),"기록 실패",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RecordRequest validateRequest = new RecordRequest(guestInfo.getId(), "host_id", currentTime, currentDateandTime, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(validateRequest);
    }

    private boolean IsThereAnyReport(String data) {
        if(guestInfo.getReport().equals(data)){
            return true;
        }else{
            return false;
        }
    }

    private boolean IsThereAnyInput(String input){
    return  true;
    }

}