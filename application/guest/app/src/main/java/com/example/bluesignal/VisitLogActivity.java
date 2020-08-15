package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VisitLogActivity extends AppCompatActivity {

    private VisitRecycler adapter;//방문내역
    private TextView editText;//생일나타내는 text
    private Date currentDate;//현재날짜를 나타냄
    private int iYear, iMonth, iDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_log);

        init();

        getData();
        editText=(TextView)findViewById(R.id.visit_date);

        getDateToday();


    }
    protected void getDateToday(){
        currentDate=new Date();
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfMon = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");

        editText.setText(sdfYear.format(currentDate)+"년"+sdfMon.format(currentDate)+"월"+sdfDay.format(currentDate)+"일");
    }

    protected void updateEditText(){
        StringBuffer sb =new StringBuffer();
        editText.setText(sb.append(iYear+"년").append(iMonth+"월").append(iDay+"일"));
    }


    public void onText3Clicked(View v){
        String strDate = editText.getText().toString();
        strDate=strDate.replace("년","/").replace("월","/").replace("일","/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

        try{
            Date pickDate = new Date(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(pickDate);
            int yy=cal.get(Calendar.YEAR);
            int mm=cal.get(Calendar.MONTH);
            int dd=cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    iYear = year;
                    iMonth = month+1;
                    iDay = day;
                    updateEditText();
                }
            }, yy, mm, dd);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }



    private void init() {
        RecyclerView recyclerView = findViewById(R.id.visit_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new VisitRecycler();
        recyclerView.setAdapter(adapter);

    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList("문진표");
        List<String> listContent = Arrays.asList(
                "3:20"

        );
        List<String> listResId = Arrays.asList(
                "정상"

        );
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            VisitData data = new VisitData();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setState(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }





}