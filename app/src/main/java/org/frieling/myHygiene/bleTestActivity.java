package org.frieling.myHygiene;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class bleTestActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter  = null;
    private BluetoothLeScanner mBluetoothLeScanner = null;

    public static final int REQUEST_BT_PERMISSIONS = 0;
    public static final int REQUEST_BT_ENABLE = 1;

    private boolean mScanning = false;
    private Handler mHandler = null;

    private Button btnScan = null;

    private ScanCallback mLeScanCallback =
            new ScanCallback() {

                @Override
                public void onScanResult(int callbackType, final ScanResult result) {
                    //super.onScanResult(callbackType, result);
                    String str = result.getDevice().getName();

                    if (str==null) {
                        str = "no name found";
                    }

                    Log.d("myTag2", str);

                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    Log.d("myTag2", "error");
                }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);


        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        this.mHandler = new Handler();

        checkBtPermissions();
        enableBt();

        enableScan(true);
    }


    public void checkBtPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN
                    },
                    REQUEST_BT_PERMISSIONS);
        }
    }

    public void enableBt(){
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_BT_ENABLE);
        }
    }

    public void enableScan(final boolean enable) {
        //ScanSettings mScanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES).build();

        if (enable) {
            mScanning = true;
            Log.d("myTag2", "start");
            mBluetoothLeScanner.startScan(mLeScanCallback);
        } else {
            Log.d("myTag2", "stop");
            mScanning = false;
            mBluetoothLeScanner.stopScan(mLeScanCallback);
        }
    }


}
