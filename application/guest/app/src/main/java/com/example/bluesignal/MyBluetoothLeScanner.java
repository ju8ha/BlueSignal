package com.example.bluesignal;

import android.Manifest;
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
import android.os.ParcelUuid;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

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

    private boolean findHostInServer(String hostName){
        if(hostName.equals("test")){
            return true;
        }
        return false;
    }
}