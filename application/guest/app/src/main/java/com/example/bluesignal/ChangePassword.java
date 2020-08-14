package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {


    GuestInfo guestInfo = GuestInfo.getInstance();

    EditText current_password_text;
    EditText modify_password_text;

    Button modify_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        current_password_text = (EditText)findViewById(R.id.current_password_text);
        modify_password_text= (EditText)findViewById(R.id.modify_password_text);
        modify_button = (Button)findViewById(R.id.modify_button);

    }

    //현재 비밀번호 입력
    //바꿀 비밀번호 입력
    //변경 버튼 누르면 변경

    public void onModifyButtonClicked(View v) {

        String current_password;//현재 비밀번호에 입력한 값
        String modify_password;//변경할 비밀번호에 입력한 값

        current_password = current_password_text.getText().toString();
        modify_password = modify_password_text.getText().toString();

        if (current_password.equals(guestInfo.getPswd())){ //비밀번호가 맞을 경우
            guestInfo.setPswd(modify_password);
            Toast.makeText(getApplicationContext(), "modify success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChangePassword.this, ChangePassword.class));

        }else{//비밀번호를 잘못 입력하였습니다 ~
            Toast.makeText(getApplicationContext(), "password error", Toast.LENGTH_SHORT).show();
        }

    }

    public void onBackButtonClicked(View v){
        startActivity(new Intent(ChangePassword.this, ChangeDetail.class));
    }
}