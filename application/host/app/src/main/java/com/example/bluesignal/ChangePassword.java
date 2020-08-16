package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity {

    HostInfo hostInfo = HostInfo.getInstance();

    EditText current_password_text;
    EditText modify_password_text;

    Button modify_button;

    private Response.Listener<String> responseListener;
    private RequestQueue queue;
    private ChangeRequest changeRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        current_password_text = (EditText)findViewById(R.id.current_password_text);
        modify_password_text= (EditText)findViewById(R.id.modify_password_text);
        modify_button = (Button)findViewById(R.id.modify_button);


        modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String current_password;//현재 비밀번호에 입력한 값
                String modify_password;//변경할 비밀번호에 입력한 값

                current_password = current_password_text.getText().toString();
                modify_password = modify_password_text.getText().toString();

                if (current_password.equals(hostInfo.getPswd())){ //비밀번호가 맞을 경우
                    hostInfo.setPswd(modify_password);

                    responseListener=new Response.Listener<String>() {//volley
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jasonObject=new JSONObject(response);//Register2 php에 response
                                boolean success=jasonObject.getBoolean("success");//Register2 php에 sucess
                                if (success) {//성공한 경우
                                    Toast.makeText(getApplicationContext(), "modify success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ChangePassword.this, SignInActivity.class);
                                    startActivity(intent);
                                }
                                else{//실패한 경우
                                    Toast.makeText(getApplicationContext(),"modify fail",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //서버로 volley를 이용해서 요청을 함
                    changeRequest=new ChangeRequest(modify_password,responseListener);
                    queue= Volley.newRequestQueue(ChangePassword.this);
                    queue.add(changeRequest);


                }else{//비밀번호를 잘못 입력하였습니다 ~
                    Toast.makeText(getApplicationContext(), "password error", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    //현재 비밀번호 입력
    //바꿀 비밀번호 입력
    //변경 버튼 누르면 변경

    public void onBackButtonClicked(View v){
        startActivity(new Intent(ChangePassword.this, ChangeInfoActivity.class));
    }
}