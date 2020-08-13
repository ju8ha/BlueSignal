package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VisitCardActivity extends MainActivity {
    TextView name_text;
    TextView phone_number_text;
    TextView time;
    GuestInfo guestInfo = GuestInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_card);

        //이름 설정
        name_text = (TextView) findViewById(R.id.name_text);
        name_text.setText(guestInfo.getName());

        //전화번호 설정
        phone_number_text = (TextView) findViewById(R.id.phone_number_text);
        phone_number_text.setText(guestInfo.getPhnNumber()); //전화번호 가져와야함

        time = (TextView) findViewById(R.id.time);
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm:ss");
        String getTime = simpleDate.format(mDate);
        time.setText(getTime);
    }

    //x 누르면 뒤로 감
    public void onXbuttonClicked(View v){
        startActivity(new Intent(VisitCardActivity.this, MainActivity.class));
    }


}