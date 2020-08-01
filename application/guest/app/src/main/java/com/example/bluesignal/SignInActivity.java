package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {
    //로그인 activity
    Button sign_in_button;
    Button sign_up_button;
    TextView id;
    TextView pswd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sign_in_button = (Button)findViewById(R.id.sign_in_button);

        sign_in_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                id = (TextView)findViewById(R.id.id_text);
                pswd = (TextView)findViewById(R.id.password_text);
                if(check(id.getText().toString(), pswd.getText().toString())){
                    //서버에 데이터가 존재할 경우
                    //현재 id값과 정보를 다음 엑티비티에 넘겨주자
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(intent,1);
                }else{
                    Toast_error_id_pswd();
                }
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
        return false;
    }

    private void Toast_error_id_pswd(){
        Toast.makeText(this,"로그인에 문제가 발생하였습니다.",Toast.LENGTH_SHORT).show();
    }
}