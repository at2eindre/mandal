package com.example.mandalart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



/*
추가 창에서 추가 누르면 디비에 빈 테이블이 들어간다(새 table id만 정해서!)
일단 첫 창에서 빈 테이블을 출력(왜냐면 들어왔따 나갔다 하면서 입력하려면 부분부분 저장한걸 땡겨와야함!)
 */


public class SettingFragment extends Fragment  implements OnBackPressedListener{

    TextView sub1, sub2, sub3, sub4, sub5, sub6, sub7, sub8;
    TextView mon, tue, wed, thu, fri, sat, sun;
    Button button_sub1, button_sub2, button_sub3, button_sub4, button_sub5, button_sub6, button_sub7, button_sub8;
    TextView ssub1, ssub2, ssub3, ssub4, ssub5, ssub6, ssub7, ssub8;
    TextView sub_topic, main_theme;
    FrameLayout frameLayout;
    LayoutInflater layoutInflater;
    View frameView, fragmentView;
    LinearLayout linearLayout;
    EditText editText;
    int DAYS=0;

    static final int MAIN_MODE = 0;
    static final int SUB_MODE = 1;
    static final int SSUB1 = 1;
    static final int SSUB2 = 2;
    static final int SSUB3 = 3;
    static final int SSUB4 = 4;
    static final int SSUB5 = 5;
    static final int SSUB6 = 6;
    static final int SSUB7 = 7;
    static final int SSUB8 = 8;

    int currentMode = MAIN_MODE;
    int insertWhere=SSUB1;

    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_table, container, false);
        fragmentView = view;
        mainActivity = (MainActivity)getActivity();
        layoutInflater = (LayoutInflater)((MainActivity)getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        frameLayout = view.findViewById(R.id.insert_mandalart_table_framelayout);
        getMandalArtView(0);
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

        //여기서 sub1~8이랑 main_theme 불러오기 필요

        linearLayout=(LinearLayout)fragmentView.findViewById(R.id.term_visible);
        linearLayout.setVisibility(View.INVISIBLE);

        editText=(EditText)fragmentView.findViewById(R.id.content);
        editText.setText(sub1.getText());


        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB1;
                editText.setText(sub1.getText());
            }
        });
        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB2;
                editText.setText(sub2.getText());
            }
        });
        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB3;
                editText.setText(sub3.getText());
            }
        });
        sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB4;
                editText.setText(sub4.getText());
            }
        });
        sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB5;
                editText.setText(sub5.getText());
            }
        });
        sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB6;
                editText.setText(sub6.getText());
            }
        });
        sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB7;
                editText.setText(sub7.getText());
            }
        });
        sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB8;
                editText.setText(sub8.getText());
            }
        });

        button_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                changeView(SSUB1);
            }
        });
        button_sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                changeView(SSUB2);
            }
        });
        button_sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                changeView(SSUB3);
            }
        });
        button_sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                changeView(SSUB4);
            }
        });
        button_sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                changeView(SSUB5);
            }
        });
        button_sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                changeView(SSUB6);
            }
        });
        button_sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                changeView(SSUB7);
            }
        });
        button_sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                changeView(SSUB8);
            }
        });

        mon = (TextView) fragmentView.findViewById(R.id.mon);
        tue = (TextView) fragmentView.findViewById(R.id.tue);
        wed = (TextView) fragmentView.findViewById(R.id.wed);
        thu = (TextView) fragmentView.findViewById(R.id.thu);
        fri = (TextView) fragmentView.findViewById(R.id.fri);
        sat = (TextView) fragmentView.findViewById(R.id.sat);
        sun = (TextView) fragmentView.findViewById(R.id.sun);
        //색 클릭 처리 해줘야함
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 0))==0){
                    mon.setTypeface(Typeface.SANS_SERIF);
                }
                else {
                    mon.setTypeface(Typeface.DEFAULT);
                }
                DAYS=DAYS|(1 << 0);
            }
        });
        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 1))==0){
                    tue.setTypeface(Typeface.SANS_SERIF);
                }
                else {
                    tue.setTypeface(Typeface.DEFAULT);
                }
                DAYS=DAYS|(1 << 1);
            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 2))==0){
                    wed.setTypeface(Typeface.SANS_SERIF);
                }
                else {
                    wed.setTypeface(Typeface.DEFAULT);
                }
                DAYS=DAYS|(1 << 2);
            }
        });
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 3))==0){
                    wed.setTypeface(Typeface.SANS_SERIF);
                }
                else {
                    wed.setTypeface(Typeface.DEFAULT);
                }
                DAYS=DAYS|(1 << 3);
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 4))==0){
                    fri.setTypeface(Typeface.SANS_SERIF);
                }
                else {
                    fri.setTypeface(Typeface.DEFAULT);
                }
                DAYS=DAYS|(1 << 4);
            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 5))==0){
                    sat.setTypeface(Typeface.SANS_SERIF);
                }
                else {
                    sat.setTypeface(Typeface.DEFAULT);
                }
                DAYS=DAYS|(1 << 5);
            }
        });
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 6))==0){
                    sun.setTypeface(Typeface.SANS_SERIF);
                }
                else {
                    sun.setTypeface(Typeface.DEFAULT);
                }
                DAYS=DAYS|(1 << 6);
            }
        });
    }

    void subInit(int now){
        ssub1 = (TextView) fragmentView.findViewById(R.id.add_ssub1);
        ssub2 = (TextView) fragmentView.findViewById(R.id.add_ssub2);
        ssub3 = (TextView) fragmentView.findViewById(R.id.add_ssub3);
        ssub4 = (TextView) fragmentView.findViewById(R.id.add_ssub4);
        ssub5 = (TextView) fragmentView.findViewById(R.id.add_ssub5);
        ssub6 = (TextView) fragmentView.findViewById(R.id.add_ssub6);
        ssub7 = (TextView) fragmentView.findViewById(R.id.add_ssub7);
        ssub8 = (TextView) fragmentView.findViewById(R.id.add_ssub8);

        sub_topic = (TextView)fragmentView.findViewById(R.id.add_sub_topic);

        //now꺼 데이터 불러오기 ssub1~8, sub_topic에!

        sub_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackView();
            }
        });

        linearLayout=(LinearLayout)fragmentView.findViewById(R.id.term_visible);
        linearLayout.setVisibility(View.VISIBLE);

        editText=(EditText)fragmentView.findViewById(R.id.content);
        editText.setText(ssub1.getText());

        ssub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB1;
                editText.setText(ssub1.getText());
                //월~일도 불러오기
            }
        });
        ssub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB2;
                editText.setText(ssub2.getText());
                //월~일도 불러오기
            }
        });
        ssub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB3;
                editText.setText(ssub3.getText());
                //월~일도 불러오기
            }
        });
        ssub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB4;
                editText.setText(ssub4.getText());
                //월~일도 불러오기
            }
        });
        ssub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB5;
                editText.setText(ssub5.getText());
                //월~일도 불러오기
            }
        });
        ssub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB6;
                editText.setText(ssub6.getText());
                //월~일도 불러오기
            }
        });
        ssub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB7;
                editText.setText(ssub7.getText());
                //월~일도 불러오기
            }
        });
        ssub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=SSUB8;
                editText.setText(ssub8.getText());
                //월~일도 불러오기
            }
        });

    }

    void savePrev(int insertWhere){
        if(currentMode == MAIN_MODE){
            //MAIN_MODE, insertWhere, editText.getText 입력

        }
        else if(currentMode == SUB_MODE){
            //SUB_MODE, insertWhere, editText.getText, 날짜입력처리해서 입력


        }
    }


    void changeView(int now){
        currentMode = SUB_MODE;
        insertWhere=SSUB1;

        getMandalArtView(now);
    }

    void changeBackView(){
        savePrev(insertWhere);

        insertWhere=SSUB1;
        currentMode = MAIN_MODE;

        getMandalArtView(0);
    }

    void getMandalArtView(int now){
        if(frameLayout.getChildCount() > 0) frameLayout.removeViewAt(0);
        if(currentMode == MAIN_MODE){
            frameView = layoutInflater.inflate(R.layout.add_main_table, frameLayout, false);
            frameLayout.addView(frameView);
            mainInit();
        }
        else if(currentMode == SUB_MODE){
            frameView = layoutInflater.inflate(R.layout.add_sub_table, frameLayout, false);
            frameLayout.addView(frameView);
            subInit(now);
        }
    }

    @Override
    public void onBackPressed(){
        if(currentMode == SUB_MODE){
            changeBackView();
        }
        else mainActivity.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setOnBackPressedListener(this);
    }
}
