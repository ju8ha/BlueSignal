package com.example.bluesignal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONArray;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class VisitLogActivity extends AppCompatActivity {

    private static String TAG = "phptest_MainActivity";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_GUEST_ID = "guest_id";
    private static final String TAG_HOST_ID = "host_id";
    private static final String TAG_TIME ="time1";
    private static final String TAG_DATE ="date1";

    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    String mJsonString;

    GuestInfo guestInfo = GuestInfo.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_log);

        mlistView = (ListView) findViewById(R.id.listView_main_list);
        mArrayList = new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://seatrea.dothome.co.kr/test.php");
    }


    private class GetData extends AsyncTask<String, Void, String>{
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mJsonString = result;
            showResult();

        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }

        }
    }

    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String guest_id = item.getString(TAG_GUEST_ID);
                String host_id = item.getString(TAG_HOST_ID);
                String time1 = item.getString(TAG_TIME);
                String date1 = item.getString(TAG_DATE);

                HashMap<String,String> hashMap = new HashMap<>();

                if(guestInfo.getId().equals(guest_id)){
                    hashMap.put(TAG_GUEST_ID, guestInfo.getName());
                    hashMap.put(TAG_TIME, time1);
                    hashMap.put(TAG_DATE, date1);

                    mArrayList.add(hashMap);
                }
            }

            ListAdapter adapter = new SimpleAdapter(
                    VisitLogActivity.this, mArrayList, R.layout.item_list,
                    new String[]{TAG_GUEST_ID, TAG_TIME, TAG_DATE},
                    new int[]{R.id.textView_list_id, R.id.textView_list_name, R.id.textView_list_address}
            );

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}
/*
public class VisitLogActivity extends AppCompatActivity {

    private VisitRecycler adapter;//방문내역
    private TextView editText;//생일나타내는 text
    private Date currentDate;//현재날짜를 나타냄
    private int iYear, iMonth, iDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_log);

        init();

        getData();
        editText=(TextView)findViewById(R.id.visit_date);

        getDateToday();

    }
    protected void getDateToday(){
        currentDate=new Date();
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfMon = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");

        editText.setText(sdfYear.format(currentDate)+"년"+sdfMon.format(currentDate)+"월"+sdfDay.format(currentDate)+"일");
    }

    protected void updateEditText(){
        StringBuffer sb =new StringBuffer();
        editText.setText(sb.append(iYear+"년").append(iMonth+"월").append(iDay+"일"));
    }


    public void onText3Clicked(View v){
        String strDate = editText.getText().toString();
        strDate=strDate.replace("년","/").replace("월","/").replace("일","/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

        try{
            Date pickDate = new Date(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(pickDate);
            int yy=cal.get(Calendar.YEAR);
            int mm=cal.get(Calendar.MONTH);
            int dd=cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    iYear = year;
                    iMonth = month+1;
                    iDay = day;
                    updateEditText();
                }
            }, yy, mm, dd);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }



    private void init() {
        RecyclerView recyclerView = findViewById(R.id.visit_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new VisitRecycler();
        recyclerView.setAdapter(adapter);

    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList("문진표");
        List<String> listContent = Arrays.asList(
                "3:20"

        );
        List<String> listResId = Arrays.asList(
                "정상"

        );
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            VisitData data = new VisitData();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setState(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

}*/