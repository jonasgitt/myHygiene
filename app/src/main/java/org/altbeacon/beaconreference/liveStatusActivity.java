package org.altbeacon.beaconreference;

import android.animation.Animator;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class liveStatusActivity extends AppCompatActivity {
    private TextView mTextMessage;

    Boolean hasDispensed = false;

    LottieAnimationView alert_symbol;
    LottieAnimationView checked_done;
    LottieAnimationView loading_rainbow;
    LottieAnimationView toggle;

    int flag = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_status);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




        //ANIMATIONS
        loading_rainbow = findViewById(R.id.animation_loading_rainbow);
        alert_symbol = findViewById(R.id.animation_alert_symbol);
        checked_done = findViewById(R.id.animation_checked_done);

        loading_rainbow.setRepeatCount(5);
        loading_rainbow.addAnimatorListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("Animation:","end");
                loading_rainbow.setVisibility(View.INVISIBLE);
                if (hasDispensed){
                    checked_done.setVisibility(View.VISIBLE);
                }
                else {
                    alert_symbol.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("Animation:","cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("Animation:","repeat");
            }
        });

        alert_symbol.setVisibility(View.INVISIBLE);
        checked_done.setVisibility(View.INVISIBLE);
        //loading_rainbow.setVisibility(View.INVISIBLE);
    }


    private void animationProgression(){



    }



    private void changeState() {
        if (flag == 0) {
            toggle.setMinAndMaxProgress(0f, 0.43f); //Here, calculation is done on the basis of start and stop frame divided by the total number of frames
            toggle.playAnimation();
            flag = 1;
            //---- Your code here------
        } else {
            toggle.setMinAndMaxProgress(0.5f, 1f);
            toggle.playAnimation();
            flag = 0;
            //---- Your code here------
        }
    }

}
