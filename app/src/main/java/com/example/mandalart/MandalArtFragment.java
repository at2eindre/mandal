package com.example.mandalart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MandalArtFragment extends Fragment implements OnBackPressedListener{

    static final String LOG = "MandalArtFragmentLog";

    long backKeyPressedTime = 0;
    Toast toast;
    String[] subTopicId = new String[9];
    TextView[] sub = new TextView[9];
    TextView[] ssub = new TextView[9];
    SimpleDateFormat format;
    TextView subTopicTextView, mainTheme, mandalArtTitle, mandalArtTerm;
    ImageView tableList;
    FrameLayout frameLayout;
    LayoutInflater layoutInflater;
    View frameView, fragmentView;

    static final int RESULT_OK = -1;
    static final int MAIN_MODE = 0;
    static final int SUB_MODE = 1;
    static final int GET_TABLE_ID = 2;

    static final long DAY_SECOND = 60 * 60 * 24 * 1000;
    int currentMode = MAIN_MODE;

    boolean changeTable = false;

    MainActivity mainActivity;

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mandalart, container, false);
        fragmentView = view;
        mainActivity = (MainActivity)getActivity();
        id = mainActivity.tableId;
        layoutInflater = (LayoutInflater)((MainActivity)getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        frameLayout = view.findViewById(R.id.mandalart_table_framelayout);
        mandalArtTitle = view.findViewById(R.id.mandalart_table_title);
        mandalArtTerm = view.findViewById(R.id.mandalart_table_term);
        tableList = view.findViewById(R.id.table_list);
        tableList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, ListActivity.class);
                startActivityForResult(intent, GET_TABLE_ID);
            }
        });
        dbHelper = new DBHelper(mainActivity);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        getMandalArtView();
        try {
            long a = getTerm();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
    }

    void getMainMandalArt(String id){
        String mainSelect = "SELECT * FROM " + dbHelper.TABLE_MAIN + " WHERE " + dbHelper.ID + " = '"  + id + "';";
        Cursor mainCursor = sqLiteDatabase.rawQuery(mainSelect, null);
        String title = "", term = "", color = "", theme = "";
        if(mainCursor.moveToNext()){
            title = mainCursor.getString(1);
            term = mainCursor.getString(2);
            term += " ~ ";
            term += mainCursor.getString(3);
            theme = mainCursor.getString(5);
        }
        mandalArtTitle.setText(title);
        mandalArtTerm.setText(term);
        mainTheme.setText(theme);

        String subSelect = "SELECT * FROM " + dbHelper.TABLE_SUB + " WHERE " + dbHelper.ID + " = '"  + id + "';";
        Cursor subCursor = sqLiteDatabase.rawQuery(subSelect, null);
        if(subCursor.moveToNext()){
            for(int i = 1; i < 9; i++) {
                subTopicId[i] = subCursor.getString(i);
                String topicSelect = "SELECT * FROM " + dbHelper.TABLE_TOPICS + " WHERE " + dbHelper.TOPIC_ID + " = '" + subTopicId[i] + "';";
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
            subTopicTextView.setText(subTopicCursor.getString(1));
        }

        String topicSelect = "SELECT * FROM " + dbHelper.TABLE_SSUB + " WHERE " + dbHelper.TOPIC_ID + " = '"  + topicId + "';";
        Cursor topicCursor = sqLiteDatabase.rawQuery(topicSelect, null);
        if(topicCursor.moveToNext()){
            for(int i = 1; i < 9; i++) {
                String planId = topicCursor.getString(i);
                String planSelect = "SELECT * FROM " + dbHelper.TABLE_PLANS + " WHERE " + dbHelper.PLAN_ID + " = '" + planId + "';";
                Cursor planCursor = sqLiteDatabase.rawQuery(planSelect, null);
                if (planCursor.moveToNext()) {
                    ssub[i].setText(planCursor.getString(1));
                }
            }
        }
    }

    void mainInit(){
        format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        sub[1] = (TextView) fragmentView.findViewById(R.id.sub1);
        sub[2] = (TextView) fragmentView.findViewById(R.id.sub2);
        sub[3] = (TextView) fragmentView.findViewById(R.id.sub3);
        sub[4] = (TextView) fragmentView.findViewById(R.id.sub4);
        sub[5] = (TextView) fragmentView.findViewById(R.id.sub5);
        sub[6] = (TextView) fragmentView.findViewById(R.id.sub6);
        sub[7] = (TextView) fragmentView.findViewById(R.id.sub7);
        sub[8] = (TextView) fragmentView.findViewById(R.id.sub8);

        mainTheme = (TextView)fragmentView.findViewById(R.id.main_theme);

        for(int i = 1; i < 9; i++){
            int finalI = i;
            sub[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainToSub();
                    getSubMandalArt(subTopicId[finalI]);
                    mainActivity.topicId = subTopicId[finalI];
                }
            });
        }
        getMainMandalArt(id);
    }

    void subInit(){
        ssub[1] = (TextView) fragmentView.findViewById(R.id.ssub1);
        ssub[2] = (TextView) fragmentView.findViewById(R.id.ssub2);
        ssub[3] = (TextView) fragmentView.findViewById(R.id.ssub3);
        ssub[4] = (TextView) fragmentView.findViewById(R.id.ssub4);
        ssub[5] = (TextView) fragmentView.findViewById(R.id.ssub5);
        ssub[6] = (TextView) fragmentView.findViewById(R.id.ssub6);
        ssub[7] = (TextView) fragmentView.findViewById(R.id.ssub7);
        ssub[8] = (TextView) fragmentView.findViewById(R.id.ssub8);

        subTopicTextView = (TextView) fragmentView.findViewById(R.id.sub_topic);
        subTopicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    void mainToSub(){
        currentMode = SUB_MODE;
        getMandalArtView();
    }

    void getMandalArtView(){
        if(frameLayout.getChildCount() > 0) frameLayout.removeViewAt(0);
        if(currentMode == MAIN_MODE){
            frameView = layoutInflater.inflate(R.layout.main_table, frameLayout, false);
            frameLayout.addView(frameView);
            mainInit();
        }
        else if(currentMode == SUB_MODE){
            frameView = layoutInflater.inflate(R.layout.sub_table, frameLayout, false);
            frameLayout.addView(frameView);
            subInit();
            getSubMandalArt(mainActivity.topicId);
        }
    }

    public long getTerm() throws ParseException {
        String mainSelect = "SELECT * FROM " + dbHelper.TABLE_MAIN + " WHERE " + dbHelper.ID + " = '"  + id + "';";
        Cursor mainCursor = sqLiteDatabase.rawQuery(mainSelect, null);
        String termStart = "", termEnd = "";
        if(mainCursor.moveToNext()){
            termStart = mainCursor.getString(2);
            termEnd = mainCursor.getString(3);
        }
        Date termStartDate = format.parse(termStart);
        Date termEndDate = format.parse(termEnd);
        long day = (termEndDate.getTime() - termStartDate.getTime()) / DAY_SECOND;
        return day + 1;
    }

    @Override
    public void onBackPressed(){
        switch (currentMode){
            case SUB_MODE:
                currentMode = MAIN_MODE;
                getMandalArtView();
                break;
            case MAIN_MODE:
                if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                    backKeyPressedTime = System.currentTimeMillis();
                    toast = Toast.makeText(mainActivity, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                    mainActivity.finish();
                    toast.cancel();
                }
                break;
            default:
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(changeTable){
            changeTable = false;
        }
        else mainActivity.setOnBackPressedListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(requestCode == GET_TABLE_ID){
            if(resultCode == RESULT_OK){
                id = intent.getStringExtra("tableId");
                mainActivity.tableId = id;
                changeTable = true;
                currentMode = MAIN_MODE;
                getMandalArtView();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("table", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString("tableId", id);
                editor.commit();
            }
        }

    }
}
