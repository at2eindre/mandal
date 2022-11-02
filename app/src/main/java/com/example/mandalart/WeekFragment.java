package com.example.mandalart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeekFragment extends Fragment {
    public static final String LOG = "WeekFragmentLog";
    static DBHelper dbHelper;
    static SQLiteDatabase sqLiteDatabase;
    static RecyclerView recyclerView;
    SimpleDateFormat format;
    Calendar calendar;
    CalendarView calendarView;
    static View fragmentView;
    int dayOfWeek;
    static final int WEEK = 7;
    static final long DAY_SECOND = 60 * 60 * 24 * 1000;
    static String weekDay[] = new String[8];
    static ArrayList<String>[] todoList = new ArrayList[2]; // 0 : id, 1 : name
    static WeekRecyclerViewAdapter weekRecyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_week, container, false);
        init();
        return fragmentView;

    }

    ///구현하시오^__^///


    public void init() {
        for(int i = 0; i < 2; i++){
            todoList[i] = new ArrayList<String>();
        }

        dbHelper = new DBHelper((MainActivity)getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();

        calendar = Calendar.getInstance();
        calendarView = (CalendarView) fragmentView.findViewById(R.id.week_calendar);
        format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Date date = new Date(year - 1900, month, dayOfMonth);

                dayOfWeek = getCurrentWeek(date);
                for(int i = 0; i < 2; i++){
                    todoList[i].clear();
                }
                for(int i = 1; i<= WEEK ;i++){
                    Date tmpDate = new Date(date.getTime() + (i - dayOfWeek) * DAY_SECOND);
                    int tmpDayOfWeek = getCurrentWeek(tmpDate);
                    weekDay[i] = format.format(tmpDate);
                    getTodoList(tmpDayOfWeek, weekDay[i]);
                }
                getRoutine();
            }
        });

        getThisWeek();

    }

    public void getThisWeek(){
        Date date = new Date();

        dayOfWeek = getCurrentWeek(date);
        for(int i = 0; i < 2; i++){
            todoList[i].clear();
        }
        for(int i = 1; i<= WEEK ;i++){
            Date tmpDate = new Date(date.getTime() + (i - dayOfWeek) * DAY_SECOND);
            int tmpDayOfWeek = getCurrentWeek(tmpDate);
            weekDay[i] = format.format(tmpDate);
            getTodoList(tmpDayOfWeek, weekDay[i]);
        }

        getRoutine();
    }

    public void getRoutine(){
        recyclerView = fragmentView.findViewById(R.id.week_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager((MainActivity)getActivity()));

        weekRecyclerViewAdapter = new WeekRecyclerViewAdapter(todoList[1], (MainActivity)getActivity());
        recyclerView.setAdapter(weekRecyclerViewAdapter);

    }

    public int getCurrentWeek(Date date) {
        calendar.setTime(date);
        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeekNumber;
    }

    public void getTodoList(int dayOfWeek, String day){
        String todoListSelect = "SELECT " + DBHelper.PLAN_ID + ", " + DBHelper.PLAN_NAME +
                " FROM " + DBHelper.TABLE_PLANS + ", " + DBHelper.TABLE_TOPICS + "," + DBHelper.TABLE_MAIN +
                " WHERE ( SELECT " + DBHelper.PLAN_TERM + " & (1 << "+ Integer.toString(dayOfWeek - 1) + ")) >= 1" +
                " AND " + DBHelper.TERM_START + "<='" + day +"' AND "  + DBHelper.TERM_END + ">= '" + day + "'" +
                " AND " + DBHelper.TABLE_MAIN + "." + DBHelper.ID + "=" + DBHelper.TABLE_TOPICS + "." + DBHelper.ID +
                " AND " + DBHelper.TABLE_PLANS + "." + DBHelper.TOPIC_ID + "=" + DBHelper.TABLE_TOPICS + "." + DBHelper.TOPIC_ID;
        Cursor todoListCursor = sqLiteDatabase.rawQuery(todoListSelect, null);
        while(todoListCursor.moveToNext()){
            String planId = todoListCursor.getString(0);
            String planName = todoListCursor.getString(1);
            String daysSelect = "SELECT * FROM " + DBHelper.TABLE_DAYS + " WHERE " + DBHelper.PLAN_ID +" = '" +
                    planId  +"' AND " + DBHelper.DATE + " = '" + day +"'";
            Cursor daysCursor = sqLiteDatabase.rawQuery(daysSelect, null);
            if(!daysCursor.moveToNext()){
                insertPlanTodo(planId, planName, day);
            }
        }

        String daysSelect = "SELECT * FROM " + DBHelper.TABLE_DAYS + " WHERE " + DBHelper.DATE + " = '" + day +"'" +
                " AND " + DBHelper.PLAN_ID + " NOT LIKE 'p%'";
        Cursor daysCursor = sqLiteDatabase.rawQuery(daysSelect, null);
        while(daysCursor.moveToNext()){
            if(!todoList[0].contains(daysCursor.getString(1))) {
                todoList[0].add(daysCursor.getString(1));
                todoList[1].add(daysCursor.getString(2));
            }
        }

    }

    public void insertPlanTodo(String planId, String planName, String day){
        String insertPlans = "INSERT INTO " + DBHelper.TABLE_DAYS + "(" + DBHelper.DATE + ", " +
                DBHelper.PLAN_ID + ", " + DBHelper.PLAN_NAME + ", " + DBHelper.CHECK + ") VALUES ('" + day +
                "', '" + planId +  "', '" + planName + "', " + Integer.toString(0) +")";
        sqLiteDatabase.execSQL(insertPlans);
    }

    public static boolean isChecked(int pos, int dayOfWeek) {
        String daysSelect = "SELECT * FROM " + DBHelper.TABLE_DAYS + " WHERE " + DBHelper.DATE + " = '" + weekDay[dayOfWeek] +"' AND " +
                DBHelper.PLAN_ID + "= '" + todoList[0].get(pos) +"'";
        Cursor daysCursor = sqLiteDatabase.rawQuery(daysSelect, null);
        if(daysCursor.moveToNext()){
            int check = daysCursor.getInt(3);
            if(check == 1) return true;
            else return false;
        }
        return false;
    }

    public static void updateRoutine(int pos, int dayOfWeek) {
        String daysSelect = "SELECT * FROM " + DBHelper.TABLE_DAYS + " WHERE " + DBHelper.DATE + " = '" + weekDay[dayOfWeek] +"' AND " +
                DBHelper.PLAN_ID + "= '" + todoList[0].get(pos) +"'";
        Cursor daysCursor = sqLiteDatabase.rawQuery(daysSelect, null);
        int chk = 0;
        if(daysCursor.moveToNext()){
            chk = daysCursor.getInt(3);
        }
        if(chk == 0) chk = 1;
        else chk = 0;
        String updateDays = "UPDATE " + DBHelper.TABLE_DAYS + " SET " + DBHelper.CHECK+ " = " + chk +
                " WHERE " + DBHelper.PLAN_ID + " = '" + todoList[0].get(pos) + "' AND " + DBHelper.DATE + "='" + weekDay[dayOfWeek] +"'";
        sqLiteDatabase.execSQL(updateDays);

        char first = todoList[0].get(pos).charAt(0);
        if(first != 'p'){
            int newComplete = 0;
            String plansSelect = "SELECT * FROM " + DBHelper.TABLE_PLANS + " WHERE " + DBHelper.PLAN_ID + " = '" + todoList[0].get(pos) + "'";
            Cursor plansCursor = sqLiteDatabase.rawQuery(plansSelect, null);
            if(plansCursor.moveToNext()){
                newComplete = daysCursor.getInt(3);
            }
            if(chk == 0) newComplete--;
            else newComplete++;

            String updatePlans = "UPDATE " + DBHelper.TABLE_PLANS + " SET " + DBHelper.COMPLETE+ " = " + newComplete +
                    " WHERE " + DBHelper.PLAN_ID + " = '" + todoList[0].get(pos)+"'";
            sqLiteDatabase.execSQL(updatePlans);

        }
    }

    public static boolean isClicked(int pos, int dayOfWeek){
        String daysSelect = "SELECT * FROM " + DBHelper.TABLE_DAYS + " WHERE " + DBHelper.DATE + " = '" + weekDay[dayOfWeek] +"' AND " +
                DBHelper.PLAN_ID + "= '" + todoList[0].get(pos) +"'";
        Cursor daysCursor = sqLiteDatabase.rawQuery(daysSelect, null);
        if(daysCursor.moveToNext()) return true;
        return false;
    }
}
