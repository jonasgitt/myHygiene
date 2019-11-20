package org.altbeacon.beaconreference;

import android.animation.Animator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link dashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link dashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dashboardFragment extends Fragment {

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
    private List<timelineViewModel> mDataList = new ArrayList<timelineViewModel>();
    private LinearLayoutManager mLayoutManager;

    int flag = 0;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public dashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dashboardFragment newInstance(String param1, String param2) {
        dashboardFragment fragment = new dashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Initialize Timeline
        setDataListItems();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initAnimations(view);
        initRecyclerView(view);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
           // throw new RuntimeException(context.toString()
                 //   + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void initRecyclerView(View view){

        mLayoutManager =  new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new timelineViewAdapter(getActivity(), mDataList);

        recyclerView.setAdapter(mAdapter);

    }

    private void setDataListItems(){

        mDataList.add(new timelineViewModel("Visit to Room A80, Cardiology Clinic","10:30 - 10:35","10",""));
        mDataList.add(new timelineViewModel("Visit to Room A64, Cardiology Clinic","10:12 - 10:15","10","Disinfected 120s after entry"));
        mDataList.add(new timelineViewModel("Visit to Room A63, Cardiology Clinic","10:01 - 10:06","10","Disinfected 3s after entry"));
        mDataList.add(new timelineViewModel("Visit to Room A71, Cardiology Clinic","09:57 - 10:00","10","Disinfected 12s after entry"));
        mDataList.add(new timelineViewModel("Visit to Room A35, Cardiology Clinic","09:50 - 09:55","10","Disinfected 20s after entry"));
        mDataList.add(new timelineViewModel("Visit to Room A35, Cardiology Clinic","09:35 - 09:46","10",""));
        mDataList.add(new timelineViewModel("Visit to Room A47, Cardiology Clinic","09:23 - 09:24","",""));
        mDataList.add(new timelineViewModel("Visit to Room A45, Cardiology Clinic","09:00 - 09:06","","Disinfected 30s after entry"));


    }

    private void initAnimations(View view){

        status_text = view.findViewById(R.id.status_text);

        //ANIMATIONS
        loading_rainbow = view.findViewById(R.id.animation_loading_rainbow);
        alert_symbol = view.findViewById(R.id.animation_alert_symbol);
        checked_done = view.findViewById(R.id.animation_checked_done);
        tracking_symbol = view.findViewById(R.id.animation_tracking_symbol);

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

        tracking_symbol.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                status_text.setText(R.string.tracking_status_text);
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
                mDataList.add(0, new timelineViewModel("Visit to Room HG E41, ETH Zurich Hauptgebäude","10:30 - 10:35","10","Disinfected 15s after entry"));
                mAdapter.notifyDataSetChanged();
            }
        });

    }

}
