package com.example.bluesignal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.ParcelUuid;

import java.nio.charset.Charset;

public class MyBluetoothLeAdvertiser {
    public final String UUID ="CDB7950D-73F1-4D4D-8E47-C090502DBD63";
    ConnectivityManager conManager;
    Context context;

    BluetoothAdapter adapter;
    BluetoothLeAdvertiser advertiser;
    AdvertiseData.Builder builder;
    AdvertiseData data;
    AdvertiseSettings settings;

    AdvertiseCallback callback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            System.out.println("advertising---O");
        }

        @Override
        public void onStartFailure(int errorCode){
            super.onStartFailure(errorCode);
            System.out.println("advertising---X");
        }
    };

    public MyBluetoothLeAdvertiser(Context context){
        conManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        this.context = context;
    }

    public void startAdvertise(String data){
        adapter = BluetoothAdapter.getDefaultAdapter();
        advertiser = adapter.getBluetoothLeAdvertiser();
        builder = new AdvertiseData.Builder();
        builder.addServiceData(ParcelUuid.fromString(UUID),data.getBytes(Charset.forName("UTF-8")));
        builder.setIncludeTxPowerLevel(true);
        this.data = builder.build();
        settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY )
                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
                .setConnectable(false)
                .build();
        advertiser.startAdvertising(settings,this.data,callback);
    }

    public void stopAdvertise(){
        advertiser.stopAdvertising(callback);
    }
}