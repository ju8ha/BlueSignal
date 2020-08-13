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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SignUpActivity extends AppCompatActivity {
    //회원 가입 activity
    Button sign_up_button;
    Button validateButton;
    Button back_button;
    Button birthday_button;
    int mYear, mMonth, mDay;

    private EditText id_text;
    private EditText password_text;
    private EditText name_text;
    private EditText phone_number_text;

    private AlertDialog dialog;
    private boolean validate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //아이디 값 찾아주기
        id_text = (EditText)findViewById(R.id.id_text);
        password_text = (EditText)findViewById(R.id.password_text);
        name_text = (EditText)findViewById(R.id.name_text);
        birthday_button = (Button)findViewById(R.id.birthday_button);
        phone_number_text = (EditText)findViewById(R.id.phone_number_text);

        validateButton=findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {//id중복체크
            @Override
            public void onClick(View view) {
                String hostID=id_text.getText().toString();
                if(validate)
                {
                    return;
                }
                if(hostID.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( SignUpActivity.this );
                    dialog=builder.setMessage("아이디는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( SignUpActivity.this );
                                dialog=builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                id_text.setEnabled(false);
                                validate=true;
                                validateButton.setText("확인");

                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( SignUpActivity.this );
                                dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest=new ValidateRequest(hostID,responseListener);
                RequestQueue queue= Volley.newRequestQueue(SignUpActivity.this);
                queue.add(validateRequest);

            }
        });

        sign_up_button = (Button)findViewById(R.id.sign_up_button);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //editText에 입력되어있는 값을 get(가져온다)해온다
                String hostID=id_text.getText().toString();
                final String hostPass=password_text.getText().toString();
                String hostName=name_text.getText().toString();
                String hostBirth=birthday_button.getText().toString();
                String hostNumber=phone_number_text.getText().toString();

                Response.Listener<String> responseListener=new Response.Listener<String>() {//volley
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jasonObject=new JSONObject(response);//Register2 php에 response
                            boolean success=jasonObject.getBoolean("success");//Register2 php에 sucess
                            if (success) {//회원등록 성공한 경우
                                Toast.makeText(getApplicationContext(), "회원 등록 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                startActivity(intent);
                            }
                            else{//회원등록 실패한 경우
                                Toast.makeText(getApplicationContext(),"회원 등록 실패",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 volley를 이용해서 요청을 함
                SignUpRequest registerRequest=new SignUpRequest(hostID,hostPass, hostName, hostBirth, hostNumber,responseListener);
                RequestQueue queue= Volley.newRequestQueue(SignUpActivity.this);
                queue.add(registerRequest);
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

        //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언

        Calendar cal = new GregorianCalendar();

        mYear = cal.get(Calendar.YEAR);

        mMonth = cal.get(Calendar.MONTH);

        mDay = cal.get(Calendar.DAY_OF_MONTH);

    }

    public void mOnClick(View v){

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