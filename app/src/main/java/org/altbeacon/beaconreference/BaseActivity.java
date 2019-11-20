package org.altbeacon.beaconreference;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.altbeacon.beaconreference.R;

public class BaseActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "myChannelID";

    BottomNavigationView bottomNav;
    String bundleStatus = null;

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
                        selectedFragment.setArguments(bundle);
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
                    Toast.makeText(this, R.string.entered_room_toast, Toast.LENGTH_LONG).show();
                    bundleStatus =  "loading";
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
}