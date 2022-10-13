package com.example.mandalart;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    static final String LOG = "DBHelperLog";

    static final String DATABASE_NAME = "mandalart.db";
    public static final String TABLE_MAIN = "main";
    public static final String TABLE_SUB = "sub";
    public static final String TABLE_SSUB = "ssub";
    public static final String TABLE_TOPICS = "topics";
    public static final String TABLE_PLANS = "plans";
    public static final String TABLE_DAYS = "days";


    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String TERM = "term";
    public static final String COLOR = "color";
    public static final String THEME = "theme";

    public static final String TOPIC_ID_1 = "topicId1";
    public static final String TOPIC_ID_2 = "topicId2";
    public static final String TOPIC_ID_3 = "topicId3";
    public static final String TOPIC_ID_4 = "topicId4";
    public static final String TOPIC_ID_5 = "topicId5";
    public static final String TOPIC_ID_6 = "topicId6";
    public static final String TOPIC_ID_7 = "topicId7";
    public static final String TOPIC_ID_8 = "topicId8";

    public static final String TOPIC_ID = "topicId";
    public static final String TOPIC = "topic";

    public static final String PLAN_ID_1 = "planId1";
    public static final String PLAN_ID_2 = "planId2";
    public static final String PLAN_ID_3 = "planId3";
    public static final String PLAN_ID_4 = "planId4";
    public static final String PLAN_ID_5 = "planId5";
    public static final String PLAN_ID_6 = "planId6";
    public static final String PLAN_ID_7 = "planId7";
    public static final String PLAN_ID_8 = "planId8";

    public static final String PLAN_ID = "planId";
    public static final String PLAN_NAME = "planName";
    public static final String PLAN_TERM = "planTerm";
    public static final String COMPLETE = "complete";

    public static final String DATE = "planDate";
    public static final String CHECK = "planCheck";

    static final String MAIN_CREATE = "CREATE TABLE if not exists " +
            TABLE_MAIN + "(" + ID + " TEXT, " + TITLE + " TEXT, " +
            TERM + " TEXT, " + COLOR + " TEXT, " + THEME + " TEXT);";

    static final String SUB_CREATE = "CREATE TABLE if not exists " +
            TABLE_SUB + "(" + ID + " TEXT, " + TOPIC_ID_1 + " TEXT, " +
            TOPIC_ID_2 + " TEXT, " + TOPIC_ID_3+ " TEXT, " + TOPIC_ID_4 + " TEXT, " +
            TOPIC_ID_5 + " TEXT, " + TOPIC_ID_6 + " TEXT, " + TOPIC_ID_7 + " TEXT, " +
            TOPIC_ID_8 + " TEXT);";

    static final String TOPICS_CREATE = "CREATE TABLE if not exists " +
            TABLE_TOPICS + "(" + TOPIC_ID + " TEXT, " + TOPIC + " TEXT);";

    static final String SSUB_CRATE = "CREATE TABLE if not exists " +
            TABLE_SSUB + "(" + TOPIC_ID + " TEXT, " +
            PLAN_ID_1 + " TEXT, " + PLAN_ID_2 + " TEXT, " + PLAN_ID_3 + " TEXT, " +
            PLAN_ID_4 + " TEXT, " + PLAN_ID_5 + " TEXT, " + PLAN_ID_6 + " TEXT, " +
            PLAN_ID_7 + " TEXT, " + PLAN_ID_8 + " TEXT);";

    static final String PLANS_CREATE = "CREATE TABLE if not exists " +
            TABLE_PLANS +"(" + PLAN_ID + " TEXT, " + PLAN_NAME + " TEXT, " +
            PLAN_TERM + " INTEGER, " + COMPLETE + " INTEGER);";

    static final String DAYS_CREATE = "CREATE TABLE if not exists " +
            TABLE_DAYS +"(" + DATE + " TEXT, " + PLAN_ID + " TEXT, " + PLAN_NAME + " TEXT, " +
            CHECK + " INTEGER);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MAIN_CREATE);
        sqLiteDatabase.execSQL(SUB_CREATE);
        sqLiteDatabase.execSQL(TOPICS_CREATE);
        sqLiteDatabase.execSQL(SSUB_CRATE);
        sqLiteDatabase.execSQL(PLANS_CREATE);
        sqLiteDatabase.execSQL(DAYS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
