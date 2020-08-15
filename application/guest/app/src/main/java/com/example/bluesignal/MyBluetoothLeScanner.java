package com.example.bluesignal;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.fragment.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBluetoothLeScanner {
    public final String UUID ="CDB7950D-73F1-4D4D-8E47-C090502DBD63";
    BluetoothAdapter adapter;
    ScanCallback callback;
    BluetoothLeScanner scanner;
    FragmentActivity fragmentActivity;
    Context context;

    String myResult;

    private static String TAG = "phptest_MainActivity";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_ID = "hostID";

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;

    public MyBluetoothLeScanner(BluetoothManager manager, Context applicationContext, FragmentActivity activity){
        this.adapter = manager.getAdapter();
        this.context = applicationContext;
        this.fragmentActivity = activity;
    }

    public void startScan(){
        if(adapter == null || !adapter.isEnabled()){
            int REQUEST_ENABLE_BT = 1;
            Intent ble_enable_intent= new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
            fragmentActivity.startActivityForResult( ble_enable_intent, REQUEST_ENABLE_BT );
            return;
        }
        if(context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            int REQUEST_FINE_LOCATION = 2;
            fragmentActivity.requestPermissions( new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION );
            return;
        }
        List<ScanFilter> filters = new ArrayList<>();
        ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        Map<String, BluetoothDevice> result = new HashMap<>();
        callback = new myScanCallback(result);
        scanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        scanner.startScan(filters, settings, callback);
    }

    public void stopScan(){
        scanner.stopScan(callback);
    }

    public String result(){
        return myResult;
    }

    private class myScanCallback extends ScanCallback{
        private Map<String, BluetoothDevice> results;

        myScanCallback(Map<String, BluetoothDevice> scanResults) {
            this.results = scanResults;
        }

        @Override
        public void onScanResult(int _callback_type, ScanResult result) {
            super.onScanResult(_callback_type, result);
            addScanResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                addScanResult(result);
            }
        }

        @Override
        public void onScanFailed(int _error) {

        }

        private void addScanResult(ScanResult result) {
            BluetoothDevice device = result.getDevice();
            String data="NONE";

            //*****************************************************************************************************************
            byte[] sdata;

            Map<ParcelUuid,byte[]> tempMap= result.getScanRecord().getServiceData();
            if(tempMap.containsKey(ParcelUuid.fromString(UUID))){
                sdata=result.getScanRecord().getServiceData().get(ParcelUuid.fromString(UUID));
                data=new String(sdata, StandardCharsets.UTF_8);
                if(findHostInServer(data)){
                    myResult = data;
                }
                return;
            }
        }
    }
// host_id 리스트 배열 저장
    private boolean findHostInServer(String hostName){

        mArrayList = new ArrayList<>(); // host 정보 저장

        GetData task = new GetData();
        task.execute("http://seatrea.dothome.co.kr/host_info.php");

        for(int i = 0; i<mArrayList.size(); i++){
            if(mArrayList.get(i))
        }
    }

    private class GetData extends AsyncTask<String, Void, String> {
        //ProgressDialog progressDialog;
        String errorString = null;

       /* @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MyBluetoothLeScanner.this,
                    "Please Wait", null, true, true);
        }*/

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //progressDialog.dismiss();
            //mTextViewResult.setText(result);
            //Log.d(TAG, "response  - " + result);
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

                String id = item.getString(TAG_ID);
                //String name = item.getString(TAG_NAME);
                //String address = item.getString(TAG_ADDRESS);

                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put(TAG_ID, id);
                //hashMap.put(TAG_NAME, name);
                //hashMap.put(TAG_ADDRESS, address);

                mArrayList.add(hashMap);
            }

            /*ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, mArrayList, R.layout.item_list,
                    new String[]{TAG_ID},
                    new int[]{R.id.textView_list_id}
            );*/


        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}