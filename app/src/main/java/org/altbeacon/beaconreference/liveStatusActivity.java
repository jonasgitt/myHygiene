package org.altbeacon.beaconreference;

import android.animation.Animator;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class liveStatusActivity extends AppCompatActivity {
    private TextView mTextMessage;

    Boolean hasDispensed = false;

    TextView status_text;

    LottieAnimationView alert_symbol;
    LottieAnimationView checked_done;
    LottieAnimationView loading_rainbow;
    LottieAnimationView tracking_symbol;

    LottieAnimationView toggle;

    //Timeline Stuff
    private timelineViewAdapter mAdapter;
    private  List<timelineViewModel> mDataList = new ArrayList<timelineViewModel>();
    private LinearLayoutManager mLayoutManager;

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


        //Initialize Timeline
        setDataListItems();
        initRecyclerView();


        status_text = findViewById(R.id.status_text);

        //ANIMATIONS
        loading_rainbow = findViewById(R.id.animation_loading_rainbow);
        alert_symbol = findViewById(R.id.animation_alert_symbol);
        checked_done = findViewById(R.id.animation_checked_done);
        tracking_symbol = findViewById(R.id.animation_tracking_symbol);

        loading_rainbow.setRepeatCount(5);
        loading_rainbow.addAnimatorListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                status_text.setText(R.string.loading_text);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("Animation:","end");
                loading_rainbow.setVisibility(View.GONE);
                if (hasDispensed){
                    checked_done.setVisibility(View.VISIBLE);
                    status_text.setText(R.string.disinfected_text);
                }
                else {
                    alert_symbol.setVisibility(View.VISIBLE);
                    status_text.setText(R.string.alert_text);
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

        alert_symbol.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                status_text.setText(R.string.alert_text);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        });

        tracking_symbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracking_symbol.setVisibility(View.GONE);
                loading_rainbow.setVisibility(View.VISIBLE);
                loading_rainbow.playAnimation();
                //---- Your code here------
            }
        });
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

    private void initRecyclerView(){

        mLayoutManager =  new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new timelineViewAdapter(this, mDataList);

        recyclerView.setAdapter(mAdapter);

    }

    private void setDataListItems(){

        mDataList.add(new timelineViewModel("Visit to Room 123","09:00","10","HHE detected 30s after entry"));
        mDataList.add(new timelineViewModel("Visit to Room 123","09:00","10","HHE detected 30s after entry"));
        mDataList.add(new timelineViewModel("Visit to Room 123","09:00","10","HHE detected 30s after entry"));
        mDataList.add(new timelineViewModel("Visit to Room 123","09:00","10","HHE detected 30s after entry"));
        mDataList.add(new timelineViewModel("Visit to Room 123","09:00","10","HHE detected 30s after entry"));


    }
}
