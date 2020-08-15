package com.example.bluesignal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bluesignal.ui.changeInfo.ChangeInfoFragment;
import com.example.bluesignal.ui.setting.SettingFragment;
import com.example.bluesignal.ui.signOut.SignOutFragment;
import com.google.android.material.navigation.NavigationView;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {
    //홈 화면 엑티비티

    Button visit_log_button;
    Button bluetooth_start_button;
    ImageView drawer_image;

    BluetoothManager manager;
    MyBluetoothLeScanner scanner;

    GuestInfo guestInfo = GuestInfo.getInstance();

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

        visit_log_button = (Button)findViewById(R.id.visit_log_button);
        bluetooth_start_button = (Button)findViewById(R.id.bluetooth_start_button);
        drawer_image = (ImageView)findViewById(R.id.drawerImage);

        GetGuestInfoByServer();

        manager = (BluetoothManager)this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        scanner = new MyBluetoothLeScanner(manager,this.getApplicationContext(), this);

        drawer_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast();
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

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

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_change_info:
                        Intent intent1 = new Intent(getApplicationContext(), ChangeInfoFragment.class);
                        startActivityForResult(intent1,1);
                        break;
                    case R.id.nav_setting:
                        Intent intent2 = new Intent(getApplicationContext(), SettingFragment.class);
                        startActivityForResult(intent2,1);
                        break;
                    case R.id.nav_sign_out:
                        Intent intent3 = new Intent(getApplicationContext(), SignOutFragment.class);
                        startActivityForResult(intent3,1);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
                }
                return true;
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void GetGuestInfoByServer() {
        //게스트 정보 서버에서 가져오기
          String id = guestInfo.getId();


        //해당 정보를 guestInfo에 저장
        guestInfo.setNBPSR("name","birthday","phnNumber","status","report");

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

    private void Toast(){
        Toast.makeText(this,"클릭", LENGTH_SHORT).show();
    }
}