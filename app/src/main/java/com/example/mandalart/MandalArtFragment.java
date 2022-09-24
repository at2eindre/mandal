package com.example.mandalart;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MandalArtFragment extends Fragment {

    TextView sub1, sub2, sub3, sub4, sub5, sub6, sub7, sub8;
    TextView ssub1, ssub2, ssub3, ssub4, ssub5, ssub6, ssub7, ssub8;
    TextView sub_topic;
    FrameLayout frameLayout;
    LayoutInflater layoutInflater;
    View frameView, fragmentView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mandalart, container, false);
        fragmentView = view;
        layoutInflater = (LayoutInflater)((MainActivity)getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        frameLayout = view.findViewById(R.id.mandalart_table_framelayout);
        frameView = layoutInflater.inflate(R.layout.main_table, frameLayout, false);
        frameLayout.addView(frameView);
        init();
        return view;
    }

    void getMandalArt(String id){


    }

    void init(){
        sub1 = (TextView) fragmentView.findViewById(R.id.sub_square_1);
        sub2 = (TextView) fragmentView.findViewById(R.id.sub2);
        sub3 = (TextView) fragmentView.findViewById(R.id.sub3);
        sub4 = (TextView) fragmentView.findViewById(R.id.sub4);
        sub5 = (TextView) fragmentView.findViewById(R.id.sub5);
        sub6 = (TextView) fragmentView.findViewById(R.id.sub6);
        sub7 = (TextView) fragmentView.findViewById(R.id.sub7);
        sub8 = (TextView) fragmentView.findViewById(R.id.sub8);

        ssub1 = (TextView) fragmentView.findViewById(R.id.ssub1);
        ssub2 = (TextView) fragmentView.findViewById(R.id.ssub2);
        ssub3 = (TextView) fragmentView.findViewById(R.id.ssub3);
        ssub4 = (TextView) fragmentView.findViewById(R.id.ssub4);
        ssub5 = (TextView) fragmentView.findViewById(R.id.ssub5);
        ssub6 = (TextView) fragmentView.findViewById(R.id.ssub6);
        ssub7 = (TextView) fragmentView.findViewById(R.id.ssub7);
        ssub8 = (TextView) fragmentView.findViewById(R.id.ssub8);

        sub_topic = (TextView) fragmentView.findViewById(R.id.sub_topic);

        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
    }

    void changeView(){
        if(frameLayout.getChildCount() > 0) frameLayout.removeViewAt(0);
        frameView = layoutInflater.inflate(R.layout.sub_table, frameLayout, false);
        frameLayout.addView(frameView);

    }

}
