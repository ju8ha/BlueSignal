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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class ChangeInfoActivity extends AppCompatActivity {

    HostInfo hostInfo = HostInfo.getInstance();

    private EditText id_text;
    private EditText name_text;
    private EditText password_text;
    private EditText phnNumber_text;

    Button back_button;
    Button modify_button;
    Button change_password_button;

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
        phnNumber_text = (EditText) findViewById(R.id.phone_number_text);
        phnNumber_text.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        id_text.setText(hostInfo.getId());
        id_text.setEnabled(false);
        name_text.setText(hostInfo.getName());
        phnNumber_text.setText(hostInfo.getPhnNumber());

        back_button = (Button)findViewById(R.id.back_button);
        modify_button = (Button)findViewById(R.id.modify_detail_button);
        change_password_button = (Button)findViewById(R.id.change_password_button);

        modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input=password_text.getText().toString();

                if (input.equals(hostInfo.getPswd())){ //비밀번호가 맞을 경우
                    hostInfo.setName(name_text.getText().toString());
                    hostInfo.setPhnNumber(phnNumber_text.getText().toString());
                    Toast.makeText(getApplicationContext(), "modify success", Toast.LENGTH_SHORT).show();

                    Response.Listener<String> responseListener=new Response.Listener<String>() {//volley
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jasonObject=new JSONObject(response);//Register2 php에 response
                                boolean success=jasonObject.getBoolean("success");//Register2 php에 sucess
                                if (success) {//회원등록 성공한 경우
                                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ChangeInfoActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else{//회원등록 실패한 경우
                                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //서버로 volley를 이용해서 요청을 함
                    ChangeRequest changeRequest=new ChangeRequest(hostInfo.getName(), hostInfo.getPhnNumber(),responseListener);
                    RequestQueue queue= Volley.newRequestQueue(ChangeInfoActivity.this);
                    queue.add(changeRequest);


                }else{//비밀번호를 잘못 입력하였습니다 ~
                    Toast.makeText(getApplicationContext(), "password error", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void onChangePswdButtonClicked(View v){
        startActivity(new Intent(ChangeInfoActivity.this, ChangePassword.class));
    }
    public void onclearButtonClicked(View v){
        startActivity(new Intent(ChangeInfoActivity.this,  MainActivity.class));
    }

    public void onBackButtonClicked(View v){ //드로어로 가야함 수정해줄 것
        startActivity(new Intent(ChangeInfoActivity.this, MainActivity.class));
    }
}