package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangeRequest extends StringRequest {

    GuestInfo guestInfo = GuestInfo.getInstance();

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://seatrea.dothome.co.kr/change.php";
    final static  private String URL2="http://seatrea.dothome.co.kr/change2.php";
    private Map<String,String> map;

    public ChangeRequest(String userName, String userBirth, String userNumber, Response.Listener<String>listener){
        super(Request.Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

        map=new HashMap<>();
        map.put("userName",userName);
        map.put("userBirth",userBirth);
        map.put("userNumber",userNumber);
        map.put("userID",guestInfo.getId());

    }

    public ChangeRequest(String password,Response.Listener<String>listener){
        super(Request.Method.POST,URL2,listener,null);//위 url에 post방식으로 값을 전송
        map=new HashMap<>();
        map.put("userPassword",password);
        map.put("userID",guestInfo.getId());
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}