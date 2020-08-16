package com.example.bluesignal;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
    //회원 가입 activity
    Button sign_up_button;
    Button validateButton;
    Button back_button;

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
        //birthday_button = (Button)findViewById(R.id.birthday_button);
        phone_number_text = (EditText)findViewById(R.id.phone_number_text);
        phone_number_text.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        final String hostID=id_text.getText().toString();
        validateButton=findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {//id중복체크
            @Override
            public void onClick(View view) {

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
                final String hostPSWD=password_text.getText().toString();
                String hostName=name_text.getText().toString();
                String hostPhnNumber=phone_number_text.getText().toString();

                if(hostID.equals("")||hostPSWD.equals("")||hostName.equals("")||hostPhnNumber.equals("")){
                    Toast.makeText(getApplicationContext(), "입력란을 모두 채워주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                SignUpRequest registerRequest=new SignUpRequest(hostID,hostPSWD, hostName, hostPhnNumber,responseListener);
                RequestQueue queue= Volley.newRequestQueue(SignUpActivity.this);
                queue.add(registerRequest);}
        });

        back_button = (Button)findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent,1);
            }
        });

    }

}