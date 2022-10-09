package com.example.mandalart;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class AddTableActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    String tableId;

    static final String LOG = "AddTableActivityLog";
    TextView[] sub = new TextView[9];
    TextView[] ssub = new TextView[9];
    Button[] button_sub = new Button[9];

    TextView mon, tue, wed, thu, fri, sat, sun;
    TextView sub_topic, main_theme;
    FrameLayout frameLayout;
    LayoutInflater layoutInflater;
    View frameView;
    LinearLayout linearLayout;
    EditText editText;
    int DAYS=0;

    ArrayList<String> topicId = new ArrayList<>();

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
    static final int COUNT = 8;
    static final String NULL = "null";

    int currentMode = MAIN_MODE;
    int insertWhere = SSUB1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_table);

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        tableId = setTableId();
        insertTableId(tableId);

        topicId.add(NULL);
        for(int i = 1; i <= COUNT; i++){
            String tmpTopicId = "";
            while(true) {
                tmpTopicId = setTopicId();
                if(!(topicId.contains(tmpTopicId))) break;
            }
            topicId.add(tmpTopicId);
        }
        insertTopicId(topicId);

        layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        frameLayout = findViewById(R.id.insert_mandalart_table_framelayout);
        getMandalArtView(0);
    }

    void getMainMandalArt(String id){
        String mainSelect = "SELECT * FROM " + dbHelper.TABLE_MAIN + " WHERE " + dbHelper.ID + " = '"  + id + "';";
        Cursor mainCursor = sqLiteDatabase.rawQuery(mainSelect, null);
        String title = "", term = "", color = "", theme = "";
        if(mainCursor.moveToNext()){
            title = mainCursor.getString(1);
            term = mainCursor.getString(2);
            color = mainCursor.getString(3);
            theme = mainCursor.getString(4);
        }
//        mandalArtTitle.setText(title);
//        mandalArtTerm.setText(term);
        main_theme.setText(theme);

        String subSelect = "SELECT * FROM " + dbHelper.TABLE_SUB + " WHERE " + dbHelper.ID + " = '"  + id + "';";
        Log.i(LOG, subSelect);
        Cursor subCursor = sqLiteDatabase.rawQuery(subSelect, null);
        if(subCursor.moveToNext()){
            for(int i = 1; i <= COUNT; i++) {
                String topicSelect = "SELECT * FROM " + dbHelper.TABLE_TOPICS + " WHERE " + dbHelper.TOPIC_ID + " = '" + topicId.get(i) + "';";
                Cursor topicCursor = sqLiteDatabase.rawQuery(topicSelect, null);
                if (topicCursor.moveToNext()) {
                    sub[i].setText(topicCursor.getString(1));
                }
            }
        }
    }

    void getSubMandalArt(String topicId){
        String subTopicSelect = "SELECT * FROM " + dbHelper.TABLE_TOPICS + " WHERE " + dbHelper.TOPIC_ID + " = '" + topicId + "';";
        Cursor subTopicCursor = sqLiteDatabase.rawQuery(subTopicSelect, null);
        if (subTopicCursor.moveToNext()) {
            sub_topic.setText(subTopicCursor.getString(1));
        }

        String topicSelect = "SELECT * FROM " + dbHelper.TABLE_SSUB + " WHERE " + dbHelper.TOPIC_ID + " = '"  + topicId + "';";
        Cursor topicCursor = sqLiteDatabase.rawQuery(topicSelect, null);
        if(topicCursor.moveToNext()){
            for(int i = 1; i <= COUNT; i++) {
                String planId = topicCursor.getString(i);
                String planSelect = "SELECT * FROM " + dbHelper.TABLE_PLANS + " WHERE " + dbHelper.PLAN_ID + " = '" + planId + "';";
                Cursor planCursor = sqLiteDatabase.rawQuery(planSelect, null);
                if (planCursor.moveToNext()) {
                    ssub[i].setText(planCursor.getString(1));
                }
            }
        }
    }

    public void insertTableId(String id){
        String insertTableId = "INSERT INTO " + DBHelper.TABLE_MAIN + "(" + DBHelper.ID + ", " +
                DBHelper.TITLE + ", " + DBHelper.TERM + ", " + DBHelper.COLOR + ", " +
                DBHelper.THEME + ") VALUES (" + id + ", " + NULL + ", " + NULL +", " + NULL +", " + NULL +")";
        sqLiteDatabase.execSQL(insertTableId);
    }

    public void insertTopicId(ArrayList arrayList){
        //sub table 생성
        for(int i = 1; i<= COUNT; i++) {
            String insertSub = "INSERT INTO " + DBHelper.TABLE_SUB + "(" + DBHelper.ID + ", " +
                    DBHelper.TOPIC_ID_1 + ", " + DBHelper.TOPIC_ID_2 + ", " + DBHelper.TOPIC_ID_3 + ", " +
                    DBHelper.TOPIC_ID_4 + ", " + DBHelper.TOPIC_ID_5 + ", " + DBHelper.TOPIC_ID_6 + ", " +
                    DBHelper.TOPIC_ID_7 + ", " + DBHelper.TOPIC_ID_8 + ") VALUES (" + tableId + ", " +
                    topicId.get(SSUB1) + ", " + topicId.get(SSUB2) + ", " + topicId.get(SSUB3) + ", " +
                    topicId.get(SSUB4) + ", " + topicId.get(SSUB5) + ", " + topicId.get(SSUB6) + ", " +
                    topicId.get(SSUB7) + ", " + topicId.get(SSUB8) + ")";
            sqLiteDatabase.execSQL(insertSub);
        }
    }

    void mainInit(){
        sub[1] = (TextView)findViewById(R.id.add_sub1);
        sub[2] = (TextView)findViewById(R.id.add_sub2);
        sub[3] = (TextView)findViewById(R.id.add_sub3);
        sub[4] = (TextView)findViewById(R.id.add_sub4);
        sub[5] = (TextView)findViewById(R.id.add_sub5);
        sub[6] = (TextView)findViewById(R.id.add_sub6);
        sub[7] = (TextView)findViewById(R.id.add_sub7);
        sub[8] = (TextView)findViewById(R.id.add_sub8);
        button_sub[1] = (Button)findViewById(R.id.button_add_sub1);
        button_sub[2] = (Button)findViewById(R.id.button_add_sub2);
        button_sub[3] = (Button)findViewById(R.id.button_add_sub3);
        button_sub[4] = (Button)findViewById(R.id.button_add_sub4);
        button_sub[5] = (Button)findViewById(R.id.button_add_sub5);
        button_sub[6] = (Button)findViewById(R.id.button_add_sub6);
        button_sub[7] = (Button)findViewById(R.id.button_add_sub7);
        button_sub[8] = (Button)findViewById(R.id.button_add_sub8);

        main_theme = (TextView)findViewById(R.id.add_main_theme);


        //여기서 sub1~8이랑 main_theme 불러오기 필요
        getMainMandalArt(tableId);

        linearLayout=(LinearLayout)findViewById(R.id.term_visible);
        linearLayout.setVisibility(View.INVISIBLE);

        editText=(EditText)findViewById(R.id.content);
        editText.setText(sub[1].getText());

        main_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                insertWhere=0;
                editText.setText(main_theme.getText());
            }
        });
        for(int i = 1; i <= COUNT; i++) {
            int finalI = i;
            sub[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    savePrev(insertWhere);

                    insertWhere=finalI;
                    editText.setText(sub[finalI].getText());
                }
            });
        }
        for(int i = 1; i <= COUNT; i++){
            int finalI = i;
            button_sub[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    savePrev(insertWhere);
                    resetDay(1);

                    changeView(finalI);
                }
            });
        }

        mon = (TextView)findViewById(R.id.mon);
        tue = (TextView)findViewById(R.id.tue);
        wed = (TextView)findViewById(R.id.wed);
        thu = (TextView)findViewById(R.id.thu);
        fri = (TextView)findViewById(R.id.fri);
        sat = (TextView)findViewById(R.id.sat);
        sun = (TextView)findViewById(R.id.sun);

        resetDay(1);

        //색 클릭 처리 해줘야함
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if ((DAYS & (1 << 0)) == 0) {
                    mon.setPaintFlags(mon.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    mon.setPaintFlags(mon.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
                }
                DAYS = DAYS ^ (1 << 0);
            }
        });
        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 1))==0){
                    tue.setPaintFlags(tue.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
                }
                else {
                    tue.setPaintFlags(tue.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
                }
                DAYS=DAYS^(1 << 1);

            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 2))==0){
                    wed.setPaintFlags(wed.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
                }
                else {
                    wed.setPaintFlags(wed.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
                }
                DAYS=DAYS^(1 << 2);

            }
        });
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 3))==0){
                    thu.setPaintFlags(thu.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
                }
                else {
                    thu.setPaintFlags(thu.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
                }
                DAYS=DAYS^(1 << 3);

            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 4))==0){
                    fri.setPaintFlags(fri.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
                }
                else {
                    fri.setPaintFlags(fri.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
                }
                DAYS=DAYS^(1 << 4);

            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 5))==0){
                    sat.setPaintFlags(sat.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
                }
                else {
                    sat.setPaintFlags(sat.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
                }
                DAYS=DAYS^(1 << 5);

            }
        });
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //색깔 확인
                if((DAYS & (1 << 6))==0){
                    sun.setPaintFlags(sun.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
                }
                else {
                    sun.setPaintFlags(sun.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
                }
                DAYS=DAYS^(1 << 6);

            }
        });
    }

    void subInit(int now){
        ssub[1] = (TextView)findViewById(R.id.add_ssub1);
        ssub[2] = (TextView)findViewById(R.id.add_ssub2);
        ssub[3] = (TextView)findViewById(R.id.add_ssub3);
        ssub[4] = (TextView)findViewById(R.id.add_ssub4);
        ssub[5] = (TextView)findViewById(R.id.add_ssub5);
        ssub[6] = (TextView)findViewById(R.id.add_ssub6);
        ssub[7] = (TextView)findViewById(R.id.add_ssub7);
        ssub[8] = (TextView)findViewById(R.id.add_ssub8);

        sub_topic = (TextView)findViewById(R.id.add_sub_topic);
        //now꺼 데이터 불러오기 ssub1~8, sub_topic에!
        getSubMandalArt(topicId.get(now));

        sub_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrev(insertWhere);

                changeBackView();
            }
        });

        linearLayout=(LinearLayout)findViewById(R.id.term_visible);
        linearLayout.setVisibility(View.VISIBLE);

        editText=(EditText)findViewById(R.id.content);
        editText.setText(ssub[1].getText());

        for(int i = 1; i <= COUNT; i++){
            int finalI = i;
            ssub[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    savePrev(insertWhere);
                    resetDay(finalI);

                    insertWhere = finalI;
                    editText.setText(ssub[finalI].getText());
                }
            });
        }

    }

    void resetDay(int i){
        //i의 계획에 저장된 day 끌어오기
        //지금은 그냥 리셋
        mon.setPaintFlags(mon.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
        tue.setPaintFlags(tue.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
        wed.setPaintFlags(wed.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
        thu.setPaintFlags(thu.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
        fri.setPaintFlags(fri.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
        sat.setPaintFlags(sat.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
        sun.setPaintFlags(sun.getPaintFlags()&~Paint.UNDERLINE_TEXT_FLAG);
    }

    void savePrev(int insertWhere){
        if(currentMode == MAIN_MODE){
            //MAIN_MODE, insertWhere, editText.getText 입력
            //insertWhere=0이면 MAIN_THEME에 저장하면 된다!

            if(insertWhere>0)sub[insertWhere].setText(editText.getText());
            else main_theme.setText(editText.getText());
        }
        else if(currentMode == SUB_MODE){
            //SUB_MODE, insertWhere, editText.getText, 날짜입력처리해서 입력

            ssub[insertWhere].setText(editText.getText());
        }
    }

    void changeView(int now){
        currentMode = SUB_MODE;
        insertWhere = SSUB1;

        getMandalArtView(now);
    }

    void changeBackView(){
        currentMode = MAIN_MODE;
        insertWhere = SSUB1;

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

    public void finishAddTable(){
        Intent intent = new Intent();
        intent.putExtra("tableId", tableId);
        setResult(RESULT_OK, intent);
        finish();
    }

    public String setTableId(){
        String id = "";
        while(true){
            id = makeId();
            if(chkDupTableId(id)) break;
        }
        return id;
    }

    public String setTopicId(){
        String id = "";
        while(true){
            id = makeId();
            if(chkDupTopicId(id)) break;
        }
        return id;
    }

    public String makeId(){
        int intTableId = (int) (Math.random() * 100000000);
        String StrTableId = Integer.toString(intTableId);
        return StrTableId;
    }

    public boolean chkDupTableId(String id){
        String mainSelect = "SELECT * FROM " + dbHelper.TABLE_MAIN + " WHERE " + dbHelper.ID + " = '"  + id + "';";
        Cursor mainCursor = sqLiteDatabase.rawQuery(mainSelect, null);
        if(mainCursor.moveToNext()) return false;
        return true;
    }

    public boolean chkDupTopicId(String id){
        String topicSelect = "SELECT * FROM " + dbHelper.TABLE_TOPICS + " WHERE " + dbHelper.TOPIC_ID + " = '"  + id + "';";
        Cursor topicCursor = sqLiteDatabase.rawQuery(topicSelect, null);
        if(topicCursor.moveToNext()) return false;
        return true;
    }

}
