package org.altbeacon.beaconreference;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.Collection;

import java.util.Queue;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.text.DecimalFormat;

import android.app.Activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;

import org.altbeacon.beacon.AltBeacon;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.distance.DistanceCalculator;
import org.altbeacon.beacon.service.ArmaRssiFilter;
import org.altbeacon.beacon.service.RunningAverageRssiFilter;

public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
    }

    @Override 
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override 
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

    @Override 
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
    }

    DistanceCalculator customDist = new customDistanceCalculator(3.303801672, 4.219149161, 0.063776869);

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setForegroundScanPeriod(100l); // 200ms
        beaconManager.setForegroundBetweenScanPeriod(0l); // 0ms


//        ByteBuffer vbb = ByteBuffer.allocateDirect(50); //use ByteBuffer first b/c allocateDirect not available for floats
//        vbb.order(ByteOrder.nativeOrder());    // use the device hardware's native byte order
//        final DoubleBuffer buf = vbb.asDoubleBuffer();  // create a floating point buffer from the ByteBuffer

        final Queue<Double> fifo = new CircularFifoQueue<Double>(5);


        RangeNotifier rangeNotifier = new RangeNotifier() {
           @Override
           public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
              if (beacons.size() > 0) {
                  Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
                  Beacon beacon = beacons.iterator().next();

                  beacon.setDistanceCalculator(customDist);
//                  logToDisplay(firstBeacon.getIdentifiers().get(1).toString());
//                  if (firstBeacon.getIdentifiers().get(1).toString().equals("0xabcde0ab012e")){
                        fifo.add(beacon.getDistance());
                        logToDisplay(fifo.toString());
//                        logToDisplay("Distance :" + beacon.getDistance());
//                        logToDisplay("RSSI: " + beacon.getRssi());
//                        logToDisplay("AVG: " + beacon.getRunningAverageRssi());
//                  }
//                  firstBeacon.getRunningAverageRssi()

              }
           }

        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);

            BeaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
            RunningAverageRssiFilter.setSampleExpirationMilliseconds(1000l);

         //   beaconManager.getBeaconParsers().add(new BeaconParser()
//                    .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        } catch (RemoteException e) {   }
    }

    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
                editText.append(line+"\n");
            }
        });
    }
}
