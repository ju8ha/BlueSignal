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

public class ChangeRequest extends StringRequest{

    HostInfo hostInfo = HostInfo.getInstance();

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://seatrea.dothome.co.kr/change_HOST.php";
    final static  private String URL2="http://seatrea.dothome.co.kr/change2_HOST.php";

    private Map<String,String> map;

    public ChangeRequest(String hostName, String hostNumber, Response.Listener<String>listener){//개인정보 변경
        super(Request.Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

        map=new HashMap<>();
        map.put("hostName",hostName);
        map.put("hostNumber",hostNumber);
        map.put("hostID",hostInfo.getId());

    }

    public ChangeRequest(String password,Response.Listener<String>listener){ //비밀번호 제거위함
        super(Request.Method.POST,URL2,listener,null);//위 url에 post방식으로 값을 전송
        map=new HashMap<>();
        map.put("hostPassword",password);
        map.put("hostID",hostInfo.getId());
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}