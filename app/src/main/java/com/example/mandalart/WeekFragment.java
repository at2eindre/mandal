package com.example.mandalart;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WeekFragment extends Fragment {
    public static final String LOG = "WeekFragmentLog";


    View fragmentView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_day, container, false);
        init();
        return fragmentView;

    }

    ///구현하시오^__^///


    public void init() {

    }

    public static boolean isChecked(int pos) {
        return true;
    }

    public static void updateRoutine(int pos) {
        
    }
}
