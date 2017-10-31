package com.example.opilane.bluetoothapp;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity"; //Lisab viisi monitorisse märgete lisamiseks

    ToggleButton wifiOnOff;

    //Bluetoothadapteri muutuja
    BluetoothAdapter mBluetoothAdapter;

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //when discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.i(TAG,"onReceive:STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.i(TAG,"mBroadcastReceiver1:STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.i(TAG,"mBroadCastReceiver1:STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.i(TAG,"mBroadCastReceiver1:STATE OFF");
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiOnOff= (ToggleButton) findViewById(R.id.toggleButton);
        wifiOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    WifiManager onwifi=(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    onwifi.setWifiEnabled(true);
                }
                else{
                    WifiManager offwifi=(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    offwifi.setWifiEnabled(false);
                }
            }
        });

        //buttoni lähtestamine/Initializing
        Button ONOFF=(Button) findViewById(R.id.ONOFF);

        //Lisame projektli default bluetooth adapteri
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        //Lisame tingimuse, mis teavitab kasutajat kas seadmel on olemas bluetoothi võimalus või mitte
        if(mBluetoothAdapter==null){
            Toast.makeText(MainActivity.this, "No bluetooth adapter found", Toast.LENGTH_LONG).show();
        }

        //lisame buttonile onsetclicklisteneri
        ONOFF.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                enableDisableBT();
            }
        });
    }
    public void enableDisableBT(){
        if(mBluetoothAdapter==null){
            Log.i(TAG, "enableDisableBT: disabling BT.");
        }
        if (!mBluetoothAdapter.isEnabled()){
            Intent enableBTIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();

            IntentFilter BTIntent=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
    }
}
