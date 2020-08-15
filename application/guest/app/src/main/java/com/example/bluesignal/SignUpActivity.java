package com.example.bluesignal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SignUpActivity extends AppCompatActivity {
    //회원 가입 activity
    Button sign_up_button;
    Button validateButton;
    Button back_button;


    private TextView editText;
    private Date currentDate;
    private int iYear, iMonth, iDay;


    private EditText id_text;
    private EditText password_text;
    private EditText name_text;
    private EditText phone_number_text;
    //private EditText mEditTextState;
    //private EditText mEditTextIssurvey;
    //private TextView mTextViewResult;

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

        phone_number_text = (EditText)findViewById(R.id.phone_number_text);
        //mEditTextState = (EditText)findViewById(R.id.editText_main_state);
        //mEditTextIssurvey= (EditText)findViewById(R.id.editText_main_issurvey);
        //mTextViewResult = (TextView)findViewById(R.id.textView_main_result);

        validateButton=findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {//id중복체크
            @Override
            public void onClick(View view) {
                String userID=id_text.getText().toString();
                if(validate)
                {
                    return;
                }
                if(userID.equals("")){
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
                ValidateRequest validateRequest=new ValidateRequest(userID,responseListener);
                RequestQueue queue= Volley.newRequestQueue(SignUpActivity.this);
                queue.add(validateRequest);

            }
        });

        sign_up_button = (Button)findViewById(R.id.sign_up_button);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //editText에 입력되어있는 값을 get(가져온다)해온다
                String userID=id_text.getText().toString();
                final String userPass=password_text.getText().toString();
                String userName=name_text.getText().toString();

                String userNumber=phone_number_text.getText().toString();
                String userBirth =editText.getText().toString();
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
                SignUpRequest registerRequest=new SignUpRequest(userID,userPass, userName, userBirth, userNumber,responseListener);
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