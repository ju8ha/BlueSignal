package com.example.bluesignal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VisitCardActivity extends MainActivity {
    TextView name_text;
    TextView phone_number_text;
    TextView date;
    TextView time;
    GuestInfo guestInfo = GuestInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_card);

        //이름 설정
        name_text = (TextView) findViewById(R.id.guest_name_text);
        name_text.setText(guestInfo.getName());


        long now = System.currentTimeMillis();
        Date mDate = new Date(now);


        date = (TextView) findViewById(R.id.date);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
        String getDate = simpleDate.format(mDate);
        date.setText(getDate); //전화번호 가져와야함


        time = (TextView) findViewById(R.id.time);
        SimpleDateFormat simpleDate1 = new SimpleDateFormat("hh:mm:ss");
        String getTime = simpleDate1.format(mDate);
        time.setText(getTime);

    }

    //x 누르면 뒤로 감
    public void onXbuttonClicked(View v){
        startActivity(new Intent(VisitCardActivity.this, MainActivity.class));
    }


}