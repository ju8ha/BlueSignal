package com.example.bluesignal;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class ReportRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static  private String URL="http://seatrea.dothome.co.kr/Survey.php";
    private Map<String,String> map;

    public ReportRequest(String userID,  String report, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

        map=new HashMap<>();
        map.put("userID",userID);
        map.put("is_survey",report);
        //map.put("userState",userMajor);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
