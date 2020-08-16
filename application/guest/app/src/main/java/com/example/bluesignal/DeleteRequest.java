package com.example.bluesignal;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://seatrea.dothome.co.kr/delete.php";

    private Map<String,String> map;

    public DeleteRequest(String id, Response.Listener<String>listener){ //id -> 전체 제거
        super(Request.Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송
        map=new HashMap<>();
        map.put("userID",id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
