package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {
    //로그인 activity
    Button sign_in_button;
    Button sign_up_button;
//    TextView id;
//    TextView pswd;
    private EditText id_text, password_text;

    GuestInfo guestInfo = GuestInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        id_text=findViewById(R.id.id_text);
        password_text=findViewById(R.id.password_text);

        sign_in_button = (Button)findViewById(R.id.sign_in_button);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID=id_text.getText().toString();
                final String userPswd=password_text.getText().toString();

                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jasonObject=new JSONObject(response);
                            boolean success=jasonObject.getBoolean("success");

                            if (success) {//회원등록 성공한 경우
                                String userID = jasonObject.getString("userID");
                                String userPSWD = jasonObject.getString("userPassword");
                                String userName = jasonObject.getString("userName");
                                String userBirth = jasonObject.getString("userBirth");
                                String userNumber = jasonObject.getString("userNumber");
                                String userState = jasonObject.getString("userState");
                                String report = jasonObject.getString("is_survey");

                                Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);

                                //intent.putExtra("log", "User");

                                guestInfo.setAllInfo(userID,userPSWD,userName,userBirth,userNumber,userState,report);

                                startActivity(intent);
                            }
                            else{//회원등록 실패한 경우
                                Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_SHORT).show();
                                return;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                SignInRequest loginRequest=new SignInRequest(userID, userPswd, responseListener);
                RequestQueue queue= Volley.newRequestQueue(SignInActivity.this);
                queue.add(loginRequest);
            }
        });

        sign_up_button = (Button)findViewById(R.id.sign_up_button);

        sign_up_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    private boolean check(String id, String pswd){
        // 서버에서 id값과 pswd값을 비교해서 존재하면 true, 다르면 false
        return true;
    }

    private void Toast_error_id_pswd(){
        Toast.makeText(this,"로그인에 문제가 발생하였습니다.",Toast.LENGTH_SHORT).show();
    }
}