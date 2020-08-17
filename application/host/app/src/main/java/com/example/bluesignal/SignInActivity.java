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

public class SignInActivity extends AppCompatActivity {
    Button sign_in_button;
    Button sign_up_button;

    private EditText id_text, password_text;

    HostInfo hostInfo = HostInfo.getInstance();

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
                final String hostID=id_text.getText().toString();
                final String hostPswd=password_text.getText().toString();

                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jasonObject=new JSONObject(response);
                            boolean success=jasonObject.getBoolean("success");

                            if (success) {
                                String hostID = jasonObject.getString("hostID");
                                String hostPswd = jasonObject.getString("hostPassword");
                                String hostName = jasonObject.getString("hostName");
                                String hostNumber = jasonObject.getString("hostNumber");
                                String hostState = jasonObject.getString("hostState");

                                Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);

                                hostInfo.setAllInfo(hostID, hostPswd, hostName, hostNumber, hostState);

                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_SHORT).show();
                                return;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                SignInRequest loginRequest=new SignInRequest(hostID, hostPswd, responseListener);
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
}