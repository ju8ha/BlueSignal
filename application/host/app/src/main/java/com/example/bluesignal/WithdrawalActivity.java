package com.example.bluesignal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WithdrawalActivity extends AppCompatActivity {


    HostInfo hostInfo = HostInfo.getInstance();

    EditText withdrawal__password_text;
    Button withdrawal_check_button;

    Button withdrawal_back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        withdrawal__password_text = (EditText)findViewById(R.id.withdrawal__password_text);
        withdrawal_check_button= (Button)findViewById(R.id.withdrawal_check_button);
        withdrawal_back_button= (Button)findViewById(R.id.withdrawal_back_button);


        withdrawal_check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String current_password;//현재 비밀번호에 입력한 값

                current_password = withdrawal__password_text.getText().toString();

                if (current_password.equals(hostInfo.getPswd())){ //비밀번호가 맞을 경우

                    Response.Listener<String> responseListener=new Response.Listener<String>() {//volley
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jasonObject=new JSONObject(response);//Register2 php에 response
                                boolean success=jasonObject.getBoolean("success");//Register2 php에 sucess
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "회원탈퇴 완료!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(WithdrawalActivity.this, SignInActivity.class);
                                    startActivity(intent);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "회원탈퇴 실패!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //서버로 volley를 이용해서 요청을 함
                    DeleteRequest deleteRequest=new DeleteRequest(hostInfo.getId(),responseListener);
                    RequestQueue queue= Volley.newRequestQueue(WithdrawalActivity.this);
                    queue.add(deleteRequest);

                    hostInfo.deleteAllInfo();
                    Intent intent2 = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivityForResult(intent2,1);


                }else{//비밀번호를 잘못 입력하였습니다 ~
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void onBackButtonClicked(View v){
        startActivity(new Intent(WithdrawalActivity.this, MainActivity.class));
    }


}