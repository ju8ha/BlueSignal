package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ChangeInfoActivity extends AppCompatActivity {

    GuestInfo guestInfo = GuestInfo.getInstance();

    private EditText id_text;
    private EditText name_text;
    private EditText password_text;
    private EditText editTextDate_text;
    private TextView birthday_text;
    private EditText phnNumber_text;

    private Date currentDate;
    private int iYear, iMonth, iDay;

    Button back_button;
    Button modify_button;
    Button change_password_button;

    int mYear, mMonth, mDay;

//    private RequestQueue queue;
//    private Response.Listener<String> responseListener;
//    private ChangeRequest changeRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        id_text = (EditText) findViewById(R.id.id_text); //변경불가
        password_text = (EditText) findViewById(R.id.password_text); //비밀번호 맞아야 변경 가능
        name_text = (EditText) findViewById(R.id.name_text);
        editTextDate_text = (EditText)findViewById(R.id.editTextDate);
        birthday_text= (TextView) findViewById(R.id.birthday_text);
        phnNumber_text = (EditText) findViewById(R.id.phone_number_text);
        phnNumber_text.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        id_text.setText(guestInfo.getId());
        id_text.setEnabled(false);
        name_text.setText(guestInfo.getName());
        editTextDate_text.setText(guestInfo.getBirthday());
        phnNumber_text.setText(guestInfo.getPhnNumber());

        back_button = (Button)findViewById(R.id.back_button);
        modify_button = (Button)findViewById(R.id.modify_detail_button);
        change_password_button = (Button)findViewById(R.id.change_password_button);

        birthday_text.bringToFront();

        //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언

        Calendar cal = new GregorianCalendar();

        mYear = cal.get(Calendar.YEAR);

        mMonth = cal.get(Calendar.MONTH);

        mDay = cal.get(Calendar.DAY_OF_MONTH);


        modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input=password_text.getText().toString();

                if (input.equals(guestInfo.getPswd())){ //비밀번호가 맞을 경우
                    guestInfo.setName(name_text.getText().toString());
                    guestInfo.setBirthday(editTextDate_text.getText().toString());
                    guestInfo.setPhnNumber(phnNumber_text.getText().toString());

                    Response.Listener<String> responseListener=new Response.Listener<String>() {//volley
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jasonObject=new JSONObject(response);//Register2 php에 response
                                boolean success=jasonObject.getBoolean("success");//Register2 php에 sucess
                                if (success) {//회원등록 성공한 경우
                                    Toast.makeText(getApplicationContext(), "회원정보 변경 성공!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ChangeInfoActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                }
                                else{//회원등록 실패한 경우
                                    Toast.makeText(getApplicationContext(), "회원정보 변경 실패!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //서버로 volley를 이용해서 요청을 함
                    ChangeRequest changeRequest=new ChangeRequest(guestInfo.getName(), guestInfo.getBirthday(), guestInfo.getPhnNumber(),responseListener);
                    RequestQueue queue= Volley.newRequestQueue(ChangeInfoActivity.this);
                    queue.add(changeRequest);


                }else{//비밀번호를 잘못 입력하였습니다 ~
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    protected void updateEditText(){
        StringBuffer sb =new StringBuffer();
        editTextDate_text.setText(sb.append(iYear+"년").append(iMonth+"월").append(iDay+"일"));
    }


    public void onText3Clicked(View v){
        currentDate=new Date();
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfMon = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        String strDate = sdfYear.format(currentDate)+"년"+sdfMon.format(currentDate)+"월"+sdfDay.format(currentDate)+"일";
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


    public void onChangePswdButtonClicked(View v){
        startActivity(new Intent(ChangeInfoActivity.this, ChangePassword.class));
    }

    public void onBackButtonClicked(View v){ //드로어로 가야함 수정해줄 것
        startActivity(new Intent(ChangeInfoActivity.this, MainActivity.class));
    }

}