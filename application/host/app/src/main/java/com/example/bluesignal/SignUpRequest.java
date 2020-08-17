package com.example.bluesignal;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignUpRequest extends StringRequest {

    final static  private String URL="http://seatrea.dothome.co.kr/Register.php";
    private Map<String,String> map;

    public SignUpRequest(String hostID, String hostPassword, String hostName, String hostNumber, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

        map=new HashMap<>();
        map.put("hostID",hostID);
        map.put("hostPassword",hostPassword);
        map.put("hostName",hostName);
        map.put("hostNumber",hostNumber);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

