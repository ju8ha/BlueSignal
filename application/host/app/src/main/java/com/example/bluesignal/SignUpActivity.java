package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SignUpActivity extends AppCompatActivity {
    //회원 가입 activity
    Button sign_up_button;
    Button back_button;

    private TextView editText;
    private Date currentDate;
    private int iYear, iMonth, iDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign_up_button = (Button)findViewById(R.id.sign_up_button);

        sign_up_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent,1);
            }
        });

        back_button = (Button)findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent,1);
            }
        });

        editText=(TextView)findViewById(R.id.birthday_text);

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




}