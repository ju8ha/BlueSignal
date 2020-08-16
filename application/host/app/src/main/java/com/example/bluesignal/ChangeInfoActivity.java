package com.example.bluesignal;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
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
                    ChangeRequest changeRequest=new ChangeRequest(hostInfo.getName(), hostInfo.getPhnNumber(),responseListener);
                    RequestQueue queue= Volley.newRequestQueue(ChangeInfoActivity.this);
                    queue.add(changeRequest);


                }else{//비밀번호를 잘못 입력하였습니다 ~
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
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