package com.example.bluesignal;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    Button bluetooth_start_button;
    Button visit_log_button;
    ImageView drawer_image;
    TextView main_id_text;
    TextView main_name_text;
    TextView main_phnNumber_text;

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
                R.id.nav_change_info,R.id.nav_withdrawal,R.id.nav_sign_out)
                .setDrawerLayout(drawer)
                .build();


        bluetooth_start_button = (Button)findViewById(R.id.bluetooth_start_button);
        visit_log_button = (Button)findViewById(R.id.visit_log_button);
        drawer_image = (ImageView)findViewById(R.id.drawerImage);

        main_name_text = (TextView)findViewById(R.id.main_name_text);
        main_phnNumber_text = (TextView)findViewById(R.id.main_phnNumber_text);

        main_name_text.setText(hostInfo.getName());
        main_phnNumber_text.setText(hostInfo.getPhnNumber());

        manager = (BluetoothManager)this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        advertiser = new MyBluetoothLeAdvertiser(this.getApplicationContext(),this);

        drawer_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);

            }
        });

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                main_id_text = (TextView)findViewById(R.id.main_id_text);
                main_id_text.setText(hostInfo.getId());
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                main_id_text = (TextView)findViewById(R.id.main_id_text);
                main_id_text.setText(hostInfo.getId());
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                main_id_text = (TextView)findViewById(R.id.main_id_text);
                main_id_text.setText(hostInfo.getId());
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                main_id_text = (TextView)findViewById(R.id.main_id_text);
                main_id_text.setText(hostInfo.getId());
            }
        });

        bluetooth_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ButtonClickedState==false){
                    bluetooth_start_button.setSelected(true);
                    bluetooth_start_button.setText("신호 전송 중지");
                    bluetooth_start_button.setBackgroundColor(Color.parseColor("#58ACFA"));
                    advertiser.startAdvertise(hostInfo.getId());
                    ButtonClickedState = true;
                }else{
                    bluetooth_start_button.setSelected(false);
                    bluetooth_start_button.setText("신호 전송 시작");
                    advertiser.stopAdvertise();
                    bluetooth_start_button.setBackgroundColor(Color.parseColor("#4486c0"));
                    ButtonClickedState = false;
                }

            }
        });

        visit_log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,VisitLogActivity.class);
                startActivity(intent);
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
                    case R.id.nav_withdrawal:
                        Intent intent2 = new Intent(MainActivity.this, WithdrawalActivity.class);
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