package com.example.mandalart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment  implements OnBackPressedListener{

    TextView sub1, sub2, sub3, sub4, sub5, sub6, sub7, sub8;
    Button button_sub1, button_sub2, button_sub3, button_sub4, button_sub5, button_sub6, button_sub7, button_sub8;
    TextView ssub1, ssub2, ssub3, ssub4, ssub5, ssub6, ssub7, ssub8;
    TextView sub_topic, main_theme;
    FrameLayout frameLayout;
    LayoutInflater layoutInflater;
    View frameView, fragmentView;

    static final int MAIN_MODE = 0;
    static final int SUB_MODE = 1;

    int currentMode = MAIN_MODE;

    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_table, container, false);
        fragmentView = view;
        mainActivity = (MainActivity)getActivity();
        layoutInflater = (LayoutInflater)((MainActivity)getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        frameLayout = view.findViewById(R.id.insert_mandalart_table_framelayout);
        getMandalArtView();
        return view;
    }

    void getMainMandalArt(String id){


    }

    void getSubMandalArt(String id){

    }

    void mainInit(){
        sub1 = (TextView) fragmentView.findViewById(R.id.add_sub1);
        sub2 = (TextView) fragmentView.findViewById(R.id.add_sub2);
        sub3 = (TextView) fragmentView.findViewById(R.id.add_sub3);
        sub4 = (TextView) fragmentView.findViewById(R.id.add_sub4);
        sub5 = (TextView) fragmentView.findViewById(R.id.add_sub5);
        sub6 = (TextView) fragmentView.findViewById(R.id.add_sub6);
        sub7 = (TextView) fragmentView.findViewById(R.id.add_sub7);
        sub8 = (TextView) fragmentView.findViewById(R.id.add_sub8);
        button_sub1 = (Button) fragmentView.findViewById(R.id.button_add_sub1);
        button_sub2 = (Button) fragmentView.findViewById(R.id.button_add_sub2);
        button_sub3 = (Button) fragmentView.findViewById(R.id.button_add_sub3);
        button_sub4 = (Button) fragmentView.findViewById(R.id.button_add_sub4);
        button_sub5 = (Button) fragmentView.findViewById(R.id.button_add_sub5);
        button_sub6 = (Button) fragmentView.findViewById(R.id.button_add_sub6);
        button_sub7 = (Button) fragmentView.findViewById(R.id.button_add_sub7);
        button_sub8 = (Button) fragmentView.findViewById(R.id.button_add_sub8);

        main_theme = (TextView)fragmentView.findViewById(R.id.add_main_theme);

        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });
        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });
        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });
        sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });
        sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });
        sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });
        sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });
        sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        button_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        button_sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        button_sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        button_sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        button_sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        button_sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        button_sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
        button_sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });
    }

    void subInit(){
        ssub1 = (TextView) fragmentView.findViewById(R.id.add_ssub1);
        ssub2 = (TextView) fragmentView.findViewById(R.id.add_ssub2);
        ssub3 = (TextView) fragmentView.findViewById(R.id.add_ssub3);
        ssub4 = (TextView) fragmentView.findViewById(R.id.add_ssub4);
        ssub5 = (TextView) fragmentView.findViewById(R.id.add_ssub5);
        ssub6 = (TextView) fragmentView.findViewById(R.id.add_ssub6);
        ssub7 = (TextView) fragmentView.findViewById(R.id.add_ssub7);
        ssub8 = (TextView) fragmentView.findViewById(R.id.add_ssub8);

        sub_topic = (TextView)fragmentView.findViewById(R.id.add_sub_topic);

        sub_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackView();
            }
        });
    }

    void insertData(){
        /*기간이랑 내용초기화*/
        /*현재 클릭한 곳에 입력되게 해야한다*/
    }

    void changeView(){
        ///
        /*sub1 저장*/
        ///

        currentMode = SUB_MODE;
        getMandalArtView();
    }

    void changeBackView(){
        ///
        /*ssub들 저장*/
        ///

        currentMode = MAIN_MODE;
        getMandalArtView();
    }
    void getMandalArtView(){
        if(frameLayout.getChildCount() > 0) frameLayout.removeViewAt(0);
        if(currentMode == MAIN_MODE){
            frameView = layoutInflater.inflate(R.layout.add_main_table, frameLayout, false);
            frameLayout.addView(frameView);
            mainInit();
        }
        else if(currentMode == SUB_MODE){
            frameView = layoutInflater.inflate(R.layout.add_sub_table, frameLayout, false);
            frameLayout.addView(frameView);
            subInit();
        }
    }

    @Override
    public void onBackPressed(){
        if(currentMode == SUB_MODE){
            currentMode = MAIN_MODE;
            getMandalArtView();
        }
        else mainActivity.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setOnBackPressedListener(this);
    }
}
