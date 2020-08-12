package com.example.bluesignal;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class SignUpRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://seatrea.dothome.co.kr/Register2.php";
    private Map<String,String> map;

    public SignUpRequest(String userID, String userPassword, String userName, String userBirth, String userNumber, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

        map=new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword",userPassword);
        map.put("userName",userName);
        map.put("userBirth",userBirth);
        map.put("userNumber",userNumber);
        //map.put("userState",userMajor);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

