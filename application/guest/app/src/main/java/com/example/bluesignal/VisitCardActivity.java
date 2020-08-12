package com.example.bluesignal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VisitCardActivity extends MainActivity {
    TextView name_text;
    TextView date;
    TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_card);


        name_text = (TextView) findViewById(R.id.name_text);

        Intent intent = getIntent();

        String name = intent.getExtras().getString("name");
        name_text.setText(name); //이름 가져와야함


        date = (TextView) findViewById(R.id.date);

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

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