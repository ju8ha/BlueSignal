package com.example.bluesignal;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
    //홈 화면 엑티비티
    Button bluetooth_start_button;
    Button visit_log_button;
    String myID;
    ImageView drawer_image;

    BluetoothManager manager;
    MyBluetoothLeAdvertiser advertiser;

    HostInfo hostInfo = HostInfo.getInstance();

    private boolean ButtonClickedState = false;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_change_info,R.id.nav_setting,R.id.nav_sign_out)
                .setDrawerLayout(drawer)
                .build();


        bluetooth_start_button = (Button)findViewById(R.id.bluetooth_start_button);
        visit_log_button = (Button)findViewById(R.id.visit_log_button);
        drawer_image = (ImageView)findViewById(R.id.drawerImage);

        manager = (BluetoothManager)this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        advertiser = new MyBluetoothLeAdvertiser(this.getApplicationContext());

        myID = "test"; //이부분은 추후에 인텐트로 로그인 시 해당정보를 메인엑티비티에 저장하도록 해야함

        drawer_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

        bluetooth_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertiser.startAdvertise(myID);


                if(ButtonClickedState==false){
                    bluetooth_start_button.setSelected(true);
                    bluetooth_start_button.setText("신호 전송 중지");
                    ButtonClickedState=true;
                }else{
                    bluetooth_start_button.setSelected(false);
                    bluetooth_start_button.setText("신호 전송 시작");
                    ButtonClickedState=false;
                }



                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        advertiser.stopAdvertise();
                        bluetooth_start_button.setSelected(false);
                        ButtonClickedState=false;
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_change_info:
                        Intent intent1 = new Intent(getApplicationContext(), ChangeInfoActivity.class);
                        startActivityForResult(intent1,1);
                        break;
                    case R.id.nav_setting:
                        Intent intent2 = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivityForResult(intent2,1);
                        break;
                    case R.id.nav_sign_out:
                        hostInfo.deleteAllInfo();
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






}