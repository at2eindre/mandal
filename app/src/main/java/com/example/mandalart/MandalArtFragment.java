package com.example.mandalart;

import static com.example.mandalart.AddTableActivity.DAYCOUNT;
import static java.lang.Math.floor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MandalArtFragment extends Fragment implements OnBackPressedListener, ViewToImage.SaveImageCallback{

    static final String LOG = "MandalArtFragmentLog";

    long backKeyPressedTime = 0;
    Toast toast;
    String[] subTopicId = new String[9];
    TextView[] sub = new TextView[9];
    TextView[] ssub = new TextView[9];
    //기간 동안 일(1)~토(7) 개수
    int[] weekCount = new int[8];
    /*
    소주제1에 계획1이면 [1][1]에 몇개 했는지 들어가 있음!
    planComplete / weekCount -> 이렇게 해서 색칠하면 될듯!
    */
    int[][][] planComplete = new int[9][9][2];
    String[][] subPlanId = new String[9][9];
    double[] colorTopic = new double[9];
    SimpleDateFormat format;
    TextView subTopicTextView, mainTheme, mandalArtTitle, mandalArtTerm;
    ImageView tableList;
    FrameLayout frameLayout;
    LayoutInflater layoutInflater;
    View frameView, fragmentView;
    String title = "", term = "", color = "", theme = "";
    String termStart = "", termEnd = "";
    int colorR=0,colorG=0,colorB=255;

    static final int RESULT_OK = -1;
    static final int MAIN_MODE = 0;
    static final int SUB_MODE = 1;
    static final int GET_TABLE_ID = 2;
    static final int WEEK = 7;
    static final int COUNT = 8;

    static final long DAY_SECOND = 60 * 60 * 24 * 1000;
    int currentMode = MAIN_MODE;

    boolean changeTable = false;

    MainActivity mainActivity;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Button downloadButton;
    LinearLayout layout;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mandalart, container, false);
        fragmentView = view;
        mainActivity = (MainActivity)getActivity();
        id = mainActivity.tableId;
        format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        layoutInflater = (LayoutInflater)((MainActivity)getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        frameLayout = view.findViewById(R.id.mandalart_table_framelayout);
        mandalArtTitle = view.findViewById(R.id.mandalart_table_title);
        mandalArtTerm = view.findViewById(R.id.mandalart_table_term);
        tableList = view.findViewById(R.id.table_list);
        downloadButton = view.findViewById(R.id.download);
        tableList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, ListActivity.class);
                startActivityForResult(intent, GET_TABLE_ID);
            }
        });
        dbHelper = new DBHelper(mainActivity);
        sqLiteDatabase = dbHelper.getWritableDatabase();


        getMandalArtView(0);
        try {
            long a = getTerm();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setWeekCount();
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        });
        return view;
    }

    void getSubPlanId(){
       for(int i = 1;i<=COUNT;i++){
            String planSelect = "SELECT * FROM " + dbHelper.TABLE_PLANS + " WHERE " +dbHelper.TOPIC_ID +"='" + subTopicId[i]+ "';";
            Cursor planCursor = sqLiteDatabase.rawQuery(planSelect, null);
            int planIdx = 0;
            while (planCursor.moveToNext()) {
                planIdx++;
                subPlanId[i][planIdx] = planCursor.getString(0);
            }
        }
    }

    Bitmap getSubBitmap(int idx, int subWhere){
        layout = frameView.findViewById(R.id.mandalart_table_linearlayout);
        Bitmap tmp;
        if(idx == 0){
            String mainSelect = "SELECT * FROM " + dbHelper.TABLE_MAIN + " WHERE "+ dbHelper.ID+"='" + id + "';";
            Cursor mainCursor = sqLiteDatabase.rawQuery(mainSelect, null);
            if(mainCursor.moveToNext()) {
                subTopicTextView.setText(mainCursor.getString(5));
                subTopicTextView.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            String topicSelect2 = "SELECT * FROM " + dbHelper.TABLE_TOPICS + " WHERE " +dbHelper.ID +"='" + id+ "';";
            Cursor topicCursor2 = sqLiteDatabase.rawQuery(topicSelect2, null);
            Log.i(LOG, topicSelect2);
            int ii = 0;
            while (topicCursor2.moveToNext()) {
                ssub[++ii].setText(topicCursor2.getString(1));
                ssub[ii].setBackgroundColor(Color.rgb(255, 255, 255));
            }
            tmp = getBitmapFromLayout(layout);
        }
        else{
            String topicSelect = "SELECT * FROM " + dbHelper.TABLE_TOPICS + " WHERE "+ dbHelper.TOPIC_ID+"='" + subTopicId[idx] + "';";
            Cursor topicCursor = sqLiteDatabase.rawQuery(topicSelect, null);
            if(topicCursor.moveToNext()) {
                subTopicTextView.setText(topicCursor.getString(1));
                subTopicTextView.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            String planSelect = "SELECT * FROM " + dbHelper.TABLE_PLANS + " WHERE " +dbHelper.TOPIC_ID +"='" + subTopicId[idx] + "';";
            Cursor planCursor = sqLiteDatabase.rawQuery(planSelect, null);
            Log.i(LOG, planSelect);
            int i = 0;
            while (planCursor.moveToNext()) {
                ssub[++i].setText(planCursor.getString(1));
                ssub[i].setBackgroundColor(Color.rgb(255, 255, 255));
            }
            tmp = getBitmapFromLayout(layout);
        }
        String topicSelect = "SELECT * FROM " + dbHelper.TABLE_TOPICS + " WHERE "+ dbHelper.TOPIC_ID+"='" + mainActivity.topicId + "';";
        Cursor topicCursor = sqLiteDatabase.rawQuery(topicSelect, null);
        if(topicCursor.moveToNext()) {
            subTopicTextView.setText(topicCursor.getString(1));
            subTopicTextView.setBackgroundColor(Color.rgb((int)floor(255-(255-colorR) * colorTopic[subWhere]),(int)floor(255-(255-colorG) * colorTopic[subWhere]),(int)floor(255-(255-colorB) * colorTopic[subWhere])));

        }
        String planSelect = "SELECT * FROM " + dbHelper.TABLE_PLANS + " WHERE " +dbHelper.TOPIC_ID +"='" + mainActivity.topicId + "';";
        Cursor planCursor = sqLiteDatabase.rawQuery(planSelect, null);
        Log.i(LOG, planSelect);
        int i = 0;
        while (planCursor.moveToNext()) {
            ssub[++i].setText(planCursor.getString(1));
            int daydo=planComplete[subWhere][i][0];
            int dayall=planComplete[subWhere][i][1];
            if(dayall>0){
                ssub[i].setBackgroundColor(Color.rgb((int)floor(255-(255-colorR) * (double)daydo/dayall),(int)floor(255-(255-colorG) * (double)daydo/dayall),(int)floor(255-(255-colorB) * (double)daydo/dayall)));
            }
            if(dayall==-1){
                if(daydo==0){
                    ssub[i].setBackgroundColor(Color.rgb(255,255,255));
                }
                else{
                    ssub[i].setBackgroundColor(Color.rgb(colorR,colorG,colorB));
                }
            }
        }

        return tmp;
    }

    Bitmap getMainBitmap(int idx){
        layout = frameView.findViewById(R.id.main_table_linearlayout);
        Bitmap tmp;
        if(idx == 0){
            mainTheme.setBackgroundColor(Color.rgb(255, 255, 255));
            for(int i = 1;i<9;i++){
                sub[i].setBackgroundColor(Color.rgb(255, 255, 255));
            }
            tmp = getBitmapFromLayout(layout);
        }
        else{
            String topicSelect = "SELECT * FROM " + dbHelper.TABLE_TOPICS + " WHERE "+ dbHelper.TOPIC_ID+"='" + subTopicId[idx] + "';";
            Cursor topicCursor = sqLiteDatabase.rawQuery(topicSelect, null);
            if(topicCursor.moveToNext()) {
                mainTheme.setText(topicCursor.getString(1));
                mainTheme.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            String planSelect = "SELECT * FROM " + dbHelper.TABLE_PLANS + " WHERE " +dbHelper.TOPIC_ID +"='" + subTopicId[idx] + "';";
            Cursor planCursor = sqLiteDatabase.rawQuery(planSelect, null);
            Log.i(LOG, planSelect);
            int i = 0;
            while (planCursor.moveToNext()) {
                sub[++i].setText(planCursor.getString(1));
                sub[i].setBackgroundColor(Color.rgb(255, 255, 255));
            }
            tmp = getBitmapFromLayout(layout);

        }
        String mainSelect = "SELECT * FROM " + dbHelper.TABLE_MAIN + " WHERE "+ dbHelper.ID+"='" + id + "';";
        Cursor mainCursor = sqLiteDatabase.rawQuery(mainSelect, null);
        if(mainCursor.moveToNext()) {
            mainTheme.setText(mainCursor.getString(5));
            mainTheme.setBackgroundColor(Color.rgb((int)floor(255-(255-colorR) * colorTopic[0]),(int)floor(255-(255-colorG) * colorTopic[0]),(int)floor(255-(255-colorB) * colorTopic[0])));

        }
        String topicSelect2 = "SELECT * FROM " + dbHelper.TABLE_TOPICS + " WHERE " +dbHelper.ID +"='" + id+ "';";
        Cursor topicCursor2 = sqLiteDatabase.rawQuery(topicSelect2, null);
        Log.i(LOG, topicSelect2);
        int ii = 0;
        while (topicCursor2.moveToNext()) {
            sub[++ii].setText(topicCursor2.getString(1));
            sub[ii].setBackgroundColor(Color.rgb((int)floor(255-(255-colorR) * colorTopic[ii]),(int)floor(255-(255-colorG) * colorTopic[ii]),(int)floor(255-(255-colorB) * colorTopic[ii])));
        }
        return tmp;
    }

    void download(){
        Bitmap[] bitmaps = new Bitmap[9];

        if(currentMode == MAIN_MODE){
            for(int i = 0;i<4;i++){
                bitmaps[i] = getMainBitmap(i + 1);
            }
            bitmaps[4] = getMainBitmap(0);
            for(int i=5;i<9;i++){
                bitmaps[i]=getMainBitmap(i);
            }
        }
        else{
            int subWhere = 0;
            for(int i=1;i<=COUNT;i++){
                if(mainActivity.topicId.equals(subTopicId[i])) subWhere = i;
            }
            for(int i = 0;i<4;i++){
                bitmaps[i] = getSubBitmap(i + 1, subWhere);
            }
            bitmaps[4] = getSubBitmap(0, subWhere);
            for(int i=5;i<9;i++){
                bitmaps[i]=getSubBitmap(i, subWhere);
            }
        }

        ViewToImage viewToImage = new ViewToImage();
        viewToImage.setSaveImageCallback(this);
        viewToImage.saveBitMap(mainActivity, mergeMultiple(bitmaps));

    }

    private Bitmap mergeMultiple(Bitmap[] parts){
        Bitmap result = Bitmap.createBitmap(parts[0].getWidth() * 3, parts[0].getHeight() * 3, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < parts.length; i++) {
            canvas.drawBitmap(parts[i], parts[i].getWidth() * (i % 3), parts[i].getHeight() * (i / 3), paint);
        }
        return result;
    }

    private Bitmap getBitmapFromLayout(LinearLayout layout) {
        Bitmap returnedBitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = layout.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        layout.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void imageResult(int status, Uri uri) {
        switch (status) {
            case 200:
                shareImage(uri);
                break;
            case 400:
                break;

        }
    }
    private void shareImage(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(intent.createChooser(intent, "Share Image"));

    }

    void getPlanComplete(){
        for(int i = 1; i<=COUNT ;i++){
            String plansSelect = "SELECT * FROM " + dbHelper.TABLE_PLANS + " WHERE " + dbHelper.TOPIC_ID + " = '"  + subTopicId[i] + "';";
            Cursor plansCursor = sqLiteDatabase.rawQuery(plansSelect, null);
            int planIdx = 0;
            while(plansCursor.moveToNext()){
                planIdx++;
                planComplete[i][planIdx][0] = plansCursor.getInt(3);
                planComplete[i][planIdx][1] = plansCursor.getInt(2);

                int days=planComplete[i][planIdx][1];

                if(days==0)planComplete[i][planIdx][1]=-1;
                else {
                    planComplete[i][planIdx][1]=0;

                    for(int ii = 0; ii <= DAYCOUNT; ii++) {
                        if ((days & (1 << ii)) >= 1) {
                            planComplete[i][planIdx][1]+=weekCount[ii+1];
                        }
                    }
                }
            }
        }
    }

    void getMainMandalArt(String id){
        String mainSelect = "SELECT * FROM " + dbHelper.TABLE_MAIN + " WHERE " + dbHelper.ID + " = '"  + id + "';";
        Cursor mainCursor = sqLiteDatabase.rawQuery(mainSelect, null);
        if(mainCursor.moveToNext()){
            termStart = mainCursor.getString(2);
            termEnd = mainCursor.getString(3);
            title = mainCursor.getString(1);
            term = mainCursor.getString(2);
            term += " ~ ";
            term += mainCursor.getString(3);
            color = mainCursor.getString(4);
            colorR=Integer.decode("0x"+color.substring(3,5));
            colorG=Integer.decode("0x"+color.substring(5,7));
            colorB=Integer.decode("0x"+color.substring(7,9));
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
        mandalArtTitle.setText(title);
        mandalArtTerm.setText(term);
        mainTheme.setText(theme);
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
        sub[1] = (TextView) fragmentView.findViewById(R.id.sub1);
        sub[2] = (TextView) fragmentView.findViewById(R.id.sub2);
        sub[3] = (TextView) fragmentView.findViewById(R.id.sub3);
        sub[4] = (TextView) fragmentView.findViewById(R.id.sub4);
        sub[5] = (TextView) fragmentView.findViewById(R.id.sub5);
        sub[6] = (TextView) fragmentView.findViewById(R.id.sub6);
        sub[7] = (TextView) fragmentView.findViewById(R.id.sub7);
        sub[8] = (TextView) fragmentView.findViewById(R.id.sub8);

        mainTheme = (TextView)fragmentView.findViewById(R.id.main_theme);

        for(int i = 1; i <= COUNT; i++){
            int finalI = i;
            sub[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainToSub(finalI);
                    getSubMandalArt(subTopicId[finalI]);
                    mainActivity.topicId = subTopicId[finalI];
                }
            });

            colorTopic[i]=0;
            for(int ii = 1; ii <= COUNT; ii++) {
                if(planComplete[i][ii][1] > 0){
                   colorTopic[i]+=((double) planComplete[i][ii][0] / planComplete[i][ii][1]) / 8;
                }
                else {
                    if(planComplete[i][ii][0] == 1){
                        colorTopic[i] += (double) 1 / 8;
                    }
                }
            }
            sub[i].setBackgroundColor(Color.rgb((int)floor(255-(255-colorR) * colorTopic[i]),(int)floor(255-(255-colorG) * colorTopic[i]),(int)floor(255-(255-colorB) * colorTopic[i])));
        }
        colorTopic[0]=0;
        for(int i=1;i<=COUNT;i++){
            colorTopic[0]+=colorTopic[i]/8;
        }
        mainTheme.setBackgroundColor(Color.rgb((int)floor(255-(255-colorR) * colorTopic[0]),(int)floor(255-(255-colorG) * colorTopic[0]),(int)floor(255-(255-colorB) * colorTopic[0])));
        getMainMandalArt(id);
    }

    void subInit(int where){
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


        getPlanComplete();
        colorTopic[where]=0;
        for(int i = 1; i <= COUNT; i++){
            int finalI = i;
            int daydo=planComplete[where][finalI][0];
            int dayall=planComplete[where][finalI][1];
            if(dayall>0){
                ssub[finalI].setBackgroundColor(Color.rgb((int)floor(255-(255-colorR) * (double)daydo/dayall),(int)floor(255-(255-colorG) * (double)daydo/dayall),(int)floor(255-(255-colorB) * (double)daydo/dayall)));

                colorTopic[where]+=((double)daydo/dayall)/8;
            }
            if(dayall==-1){
                if(daydo==0){
                    ssub[finalI].setBackgroundColor(Color.rgb(255,255,255));
                }
                else{
                    ssub[finalI].setBackgroundColor(Color.rgb(colorR,colorG,colorB));
                    colorTopic[where]+=(double)1/8;
                }
            }

            ssub[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int daydo=planComplete[where][finalI][0];
                    int dayall=planComplete[where][finalI][1];
                    if(dayall==-1){
                        if(daydo==1){
                            planComplete[where][finalI][0]=0;
                            colorTopic[where]-=(double)1/8;
                            subTopicTextView.setBackgroundColor(Color.rgb((int)floor(255-(255-colorR) * colorTopic[where]),(int)floor(255-(255-colorG) * colorTopic[where]),(int)floor(255-(255-colorB) * colorTopic[where])));

                            String updatePlans = "UPDATE " + DBHelper.TABLE_PLANS + " SET " + DBHelper.COMPLETE+ " = " + 0 +
                                    " WHERE " + DBHelper.PLAN_ID + " = '" + subPlanId[where][finalI]+"'";
                            sqLiteDatabase.execSQL(updatePlans);

                            ssub[finalI].setBackgroundColor(Color.rgb(255,255,255));
                        }
                        else{
                            planComplete[where][finalI][0]=1;
                            colorTopic[where]+=(double)1/8;
                            subTopicTextView.setBackgroundColor(Color.rgb((int)floor(255-(255-colorR) * colorTopic[where]),(int)floor(255-(255-colorG) * colorTopic[where]),(int)floor(255-(255-colorB) * colorTopic[where])));

                            String updatePlans = "UPDATE " + DBHelper.TABLE_PLANS + " SET " + DBHelper.COMPLETE+ " = " + 1 +
                                    " WHERE " + DBHelper.PLAN_ID + " = '" + subPlanId[where][finalI]+"'";
                            sqLiteDatabase.execSQL(updatePlans);

                            ssub[finalI].setBackgroundColor(Color.rgb(colorR,colorG,colorB));
                        }
                    }

                }
            });
        }
        subTopicTextView.setBackgroundColor(Color.rgb((int)floor(255-(255-colorR) * colorTopic[where]),(int)floor(255-(255-colorG) * colorTopic[where]),(int)floor(255-(255-colorB) * colorTopic[where])));

    }

    void mainToSub(int where){
        currentMode = SUB_MODE;
        getMandalArtView(where);
    }

    void getMandalArtView(int where){
        getSubPlanId();
        String mainSelect = "SELECT * FROM " + dbHelper.TABLE_MAIN + " WHERE " + dbHelper.ID + " = '"  + id + "';";
        Cursor mainCursor = sqLiteDatabase.rawQuery(mainSelect, null);
        if(mainCursor.moveToNext()){
            color = mainCursor.getString(4);
            colorR=Integer.decode("0x"+color.substring(3,5));
            colorG=Integer.decode("0x"+color.substring(5,7));
            colorB=Integer.decode("0x"+color.substring(7,9));
        }

        String subSelect = "SELECT * FROM " + dbHelper.TABLE_SUB + " WHERE " + dbHelper.ID + " = '"  + id + "';";
        Cursor subCursor = sqLiteDatabase.rawQuery(subSelect, null);
        if(subCursor.moveToNext()){
            for(int i = 1; i < 9; i++) {
                subTopicId[i] = subCursor.getString(i);
            }
        }
        getPlanComplete();
        if(frameLayout.getChildCount() > 0) frameLayout.removeViewAt(0);
        if(currentMode == MAIN_MODE){
            frameView = layoutInflater.inflate(R.layout.main_table, frameLayout, false);
            frameLayout.addView(frameView);
            mainInit();
        }
        else if(currentMode == SUB_MODE){
            frameView = layoutInflater.inflate(R.layout.sub_table, frameLayout, false);
            frameLayout.addView(frameView);
            if(where==0){
            for(int i=1;i<=8;i++){
                if(subTopicId[i].equals(mainActivity.topicId)){
                    subInit(i);
                }
            }}

            else subInit(where);
            getSubMandalArt(mainActivity.topicId);
        }
    }

    void setWeekCount(){
        try {
            int firstDay = getFirstDayOfWeek();
            int termDay = getTerm();
            int remainder = termDay % WEEK;
            for(int i=0;i<WEEK;i++){
                int currentDay = firstDay + i;
                currentDay %=7;
                if(currentDay == 0) currentDay =7;
                if(i < remainder) weekCount[currentDay] = termDay / WEEK + 1;
                else weekCount[currentDay] = termDay / WEEK;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public int getTerm() throws ParseException {
        Date termStartDate = format.parse(termStart);
        Date termEndDate = format.parse(termEnd);
        long day = (termEndDate.getTime() - termStartDate.getTime()) / DAY_SECOND;
        return (int)day + 1;
    }

    public int getFirstDayOfWeek() throws ParseException{
        Date date = format.parse(termStart);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeekNumber;
    }

    @Override
    public void onBackPressed(){
        switch (currentMode){
            case SUB_MODE:
                currentMode = MAIN_MODE;
                getMandalArtView(0);
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
                getMandalArtView(0);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("table", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString("tableId", id);
                editor.commit();
            }
        }

    }
}
