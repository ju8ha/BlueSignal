package com.example.bluesignal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ChangeDetail extends AppCompatActivity {

    Button change_detail;
    GuestInfo guestInfo = GuestInfo.getInstance();
    private String id;
    private String pswd;
    private String name;
    private String birthday;
    private String phnNumber;

    private EditText id_text;
    private EditText name_text;
    private EditText password_text;
    private Button birthday_button;
    private EditText phnNumber_text;

    Button back_button;
    Button modify_button;
    Button change_password_button;

    int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_detail);

        id_text = (EditText) findViewById(R.id.id_text); //변경불가
        password_text = (EditText) findViewById(R.id.password_text); //비밀번호 맞아야 변경 가능
        name_text = (EditText) findViewById(R.id.name_text);
        birthday_button= (Button) findViewById(R.id.birthday_button);
        phnNumber_text = (EditText) findViewById(R.id.phone_number_text);

        id_text.setText(guestInfo.getId());
        name_text.setText(guestInfo.getName());
        birthday_button.setText(guestInfo.getBirthday());
        phnNumber_text.setText(guestInfo.getPhnNumber());


        back_button = (Button)findViewById(R.id.back_button);
        modify_button = (Button)findViewById(R.id.modify_button);
        change_password_button = (Button)findViewById(R.id.change_password_button);


        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent,1);
            }
        });

        //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언

        Calendar cal = new GregorianCalendar();

        mYear = cal.get(Calendar.YEAR);

        mMonth = cal.get(Calendar.MONTH);

        mDay = cal.get(Calendar.DAY_OF_MONTH);

    }


    public void onChangePswdButtonClicked(View v){
        startActivity(new Intent(ChangeDetail.this, ChangePassword.class));
    }

    public void onBackButtonClicked(View v){ //드로어로 가야함 수정해줄 것
        startActivity(new Intent(ChangeDetail.this, MainActivity.class));
    }


    public void onModifyButtonClicked(View v){
        String inputPswd=password_text.getText().toString();
        if(inputPswd.equals(guestInfo.getPswd())) {
            guestInfo.setName(name_text.getText().toString());
            guestInfo.setBirthday(birthday_button.getText().toString());
            guestInfo.setPhnNumber(phnNumber_text.getText().toString());
            Toast.makeText(getApplicationContext(), "modify success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChangeDetail.this, ChangeDetail.class));
        }else{
            Toast.makeText(getApplicationContext(), "password error", Toast.LENGTH_SHORT).show();
        }
    }


    public void onBirthdayButtonClicked(View v){

        switch(v.getId()){

            //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌

            case R.id.birthday_button:

                //여기서 리스너도 등록함

                new DatePickerDialog(this, mDateSetListener, mYear,

                        mMonth, mDay).show();

                break;
        }

    }

    DatePickerDialog.OnDateSetListener mDateSetListener =

            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,

                                      int dayOfMonth) {

                    // TODO Auto-generated method stub

                    //사용자가 입력한 값을 가져온뒤
                    mYear = year;

                    mMonth = monthOfYear;

                    mDay = dayOfMonth;
                    birthday_button = (Button)findViewById(R.id.birthday_button);
                    birthday_button.setText(mYear+"년"+mMonth+"월"+mDay+"일");

                }

            };

}