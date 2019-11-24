package org.altbeacon.beaconreference;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.distance.DistanceCalculator;
import org.altbeacon.beacon.service.ArmaRssiFilter;
import org.altbeacon.beacon.service.RunningAverageRssiFilter;
import org.altbeacon.beaconreference.R;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.logging.Logger;

public class BaseActivity extends AppCompatActivity implements BeaconConsumer {

    protected static final String TAG = "myTag";
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    private static final String CHANNEL_ID = "myChannelID";
    public  String startTime = null; // = "10:40:32";
    public  Boolean hasEntered = false;
    public  String endTime = null;

    BottomNavigationView bottomNav;
    String bundleStatus = null;


    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

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

        logToDisplay("i here");


//        ByteBuffer vbb = ByteBuffer.allocateDirect(50); //use ByteBuffer first b/c allocateDirect not available for floats
//        vbb.order(ByteOrder.nativeOrder());    // use the device hardware's native byte order
//        final DoubleBuffer buf = vbb.asDoubleBuffer();  // create a floating point buffer from the ByteBuffer

        final CircularFifoQueue<Double> fifo = new CircularFifoQueue<Double>(5);


        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.d("myTag", "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
                    Beacon beacon = beacons.iterator().next();

                    beacon.setDistanceCalculator(customDist);
//                  logToDisplay(firstBeacon.getIdentifiers().get(1).toString());
//                  if (firstBeacon.getIdentifiers().get(1).toString().equals("0xabcde0ab012e")){
                    fifo.add(beacon.getDistance());

                    if (fifo.size() > 4) {
                        if (fifo.get(0) < 1 && fifo.get(1) < 1 && fifo.get(2) < 1 && fifo.get(3) < 1 && fifo.get(4) < 1) {
                            logToDisplay(fifo.toString());
                        }
                    }
                   // beacon.get
                    logToDisplay(beacon.getDataFields().toString());
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
//        Logger.log
        Log.d("myTAG", line);
    }

    //_______________________________________________________








    //__________________________________________________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setLogo(R.drawable.ic_launcher_dispenser);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new newsFragment()).commit();
//
        createNotificationChannel();
        //I added this if statement to keep the selected fragment when rotating the device
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new HomeFragment()).commit();
//        }

        verifyBluetooth();
        verifyLocation();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new newsFragment();

                            break;
                        case R.id.navigation_dashboard:
                            selectedFragment = new dashboardFragment();
                            break;
                        case R.id.navigation_notifications:
                            selectedFragment = new statsFragment();
                            break;
                    }

                    if (bundleStatus!=null){
                        Bundle bundle = new Bundle();
                        bundle.putString("status",bundleStatus);
                        bundle.putString("startTime", startTime);
                        bundle.putString("endTime", endTime);

                        selectedFragment.setArguments(bundle);
//                        bundle.putList
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.option_settings:
                selectedFragment = new settingsFragment();
                break;
            case R.id.option_about:
                selectedFragment = new newsFragment();
            default:
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);

        ft.replace(R.id.fragment_container,
                selectedFragment).commit();
        return true;
    }

    private void vibrate() {
        if (Build.VERSION.SDK_INT >= 26) {
            long[] pattern = new long[]{1000, 200, 300, 400, 500, 400, 300, 200, 400};
            int[] amplituds = new int[]{255,255,255,255,255,255,255,255,255};
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createWaveform(pattern, -1));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
        }

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            channel.enableVibration(false);
            channel.setSound(null, null);


            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alert_nohhe)
                .setContentTitle(getString(R.string.notification_title_text))
                .setContentText(getString(R.string.notification_content_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_UP) {

                    String toastString;
                    if (!hasEntered) {
                        toastString = getString(R.string.entered_room_toast);
                        bundleStatus = "loading";
                        hasEntered = true;
                        startTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    }
                    else {
                        toastString = getString(R.string.left_room_toast);
                        bundleStatus = "tracking";
                        hasEntered = false;
                        endTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    }

                    Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
                    navListener.onNavigationItemSelected(bottomNav.getMenu().getItem(1));
                    bottomNav.setSelectedItemId(R.id.navigation_dashboard);
                }
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                        sendNotification();
                        vibrate();
                        bundleStatus =  "alerting";
                        navListener.onNavigationItemSelected(bottomNav.getMenu().getItem(1));
                        bottomNav.setSelectedItemId(R.id.navigation_dashboard);
                }
                return true;

            default:
                return super.dispatchKeyEvent(event);
        }
    }


    public Boolean hasEntered(){
        return hasEntered;
    }

    public String getStartTime(){
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
    String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "fine location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
            case PERMISSION_REQUEST_BACKGROUND_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "background location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }


    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //finish();
                        //System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    //finish();
                    //System.exit(0);
                }

            });
            builder.show();

        }

    }


    private void verifyLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("This app needs background location access");
                            builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @TargetApi(23)
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                            PERMISSION_REQUEST_BACKGROUND_LOCATION);
                                }

                            });
                            builder.show();
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Functionality limited");
                            builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons in the background.  Please go to Settings -> Applications -> Permissions and grant background location access to this app.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }

                            });
                            builder.show();
                        }
                    }
                }
            } else {
                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            PERMISSION_REQUEST_FINE_LOCATION);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }

            }
        }
    }
}
