package org.frieling.myHygiene;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class aboutFragment extends Fragment {

    public aboutFragment(){};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);;


//        View aboutPage = new AboutPage(this)
//                .isRTL(false)
//                .addGroup("Connect with us")
//                .addEmail("elmehdi.sakout@gmail.com")
//                .addWebsite("http://medyo.github.io/")
//                .addFacebook("the.medy")
//                .addTwitter("medyo80")
//                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
//                .addPlayStore("com.ideashower.readitlater.pro")
//                .addInstagram("medyo80")
//                .addGitHub("medyo")
//                .create();
//
//        view.setContentView(aboutPage);

        return view;
    }
}
