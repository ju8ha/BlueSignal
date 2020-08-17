package com.example.bluesignal;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignInRequest extends StringRequest {

    final static  private String URL="http://seatrea.dothome.co.kr/Login.php";
    private Map<String,String> map;

    public SignInRequest(String hostID, String hostPassword, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("hostID",hostID);
        map.put("hostPassword",hostPassword);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
