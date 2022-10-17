package com.example.mandalart;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

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

public class DayFragment extends Fragment {
    public static final String LOG = "DayFragmentLog";
    CalendarView calendarView;
    static DBHelper dbHelper;
    static SQLiteDatabase sqLiteDatabase;
    static String day;
    int dayOfWeek;

    static ArrayList<String>[] todoList = new ArrayList[2]; // 0 : id, 1 : name

    View fragmentView;
    Calendar calendar;
    Button todoInsertButton;
    EditText todoEditText;

    static RecyclerView recyclerView;

    static DayTodoListRecyclerViewAdapter dayTodoListRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_day, container, false);
        init();
        return fragmentView;
    }

    public void init(){
        for(int i = 0; i < 2; i++){
            todoList[i] = new ArrayList<String>();
        }

        dbHelper = new DBHelper((MainActivity)getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        calendar = Calendar.getInstance();
        calendarView = (CalendarView) fragmentView.findViewById(R.id.calendar);
        todoEditText = (EditText) fragmentView.findViewById(R.id.todo_list);
        todoInsertButton = (Button) fragmentView.findViewById(R.id.todo_list_btn);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일", Locale.getDefault());
                Date date = new Date(year - 1900, month, dayOfMonth);
                day = format.format(date);
                dayOfWeek = getCurrentWeek(date);
                getTodoList(dayOfWeek);
            }
        });

        todoInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!todoEditText.getText().equals("")){
                    insertTodo();
                    getTodoList(dayOfWeek);
                }
            }
        });

        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일", Locale.getDefault());
        day = format.format(currentDate);
        getTodoList(getCurrentWeek(currentDate));
    }

    public static boolean isChecked(int pos){
        String daysSelect = "SELECT * FROM " + DBHelper.TABLE_DAYS + " WHERE " + DBHelper.DATE + " = '" + day +"' AND " +
                DBHelper.PLAN_ID + "= '" + todoList[0].get(pos) +"'";
        Cursor daysCursor = sqLiteDatabase.rawQuery(daysSelect, null);
        if(daysCursor.moveToNext()){
            int check = daysCursor.getInt(3);
            if(check == 1) return true;
            else return false;
        }
        return false;
    }

    public static boolean planOrNot(int pos){
        String planId = todoList[0].get(pos);
        char first = planId.charAt(0);
        if(first == 'p') return true;
        return false;
    }

    public static void deleteTodo(int pos){
        String daysDelete = "DELETE FROM " + DBHelper.TABLE_DAYS + " WHERE " + DBHelper.DATE + " = '" + day +"' AND " +
                DBHelper.PLAN_ID + "= '" + todoList[0].get(pos) +"'";
        sqLiteDatabase.execSQL(daysDelete);
    }

    public static void updateTodo(int pos){
        String daysSelect = "SELECT * FROM " + DBHelper.TABLE_DAYS + " WHERE " + DBHelper.DATE + " = '" + day +"' AND " +
                DBHelper.PLAN_ID + "= '" + todoList[0].get(pos) +"'";
        Cursor daysCursor = sqLiteDatabase.rawQuery(daysSelect, null);
        int chk = 0;
        if(daysCursor.moveToNext()){
            chk = daysCursor.getInt(3);
        }
        if(chk == 0) chk = 1;
        else chk = 0;
        String updatePlans = "UPDATE " + DBHelper.TABLE_DAYS + " SET " + DBHelper.CHECK+ " = " + chk +
                " WHERE " + DBHelper.PLAN_ID + " = '" + todoList[0].get(pos) + "' AND " + DBHelper.DATE + "='" + day +"'";
        sqLiteDatabase.execSQL(updatePlans);
    }

    public void getTodoList(int dayOfWeek){
        for(int i = 0; i < 2; i++){
            todoList[i].clear();
        }
        String todoListSelect = "SELECT * FROM " + DBHelper.TABLE_PLANS + " WHERE ( SELECT "
                + DBHelper.PLAN_TERM + " & (1 << "+ Integer.toString(dayOfWeek - 1) + ")) >= 1";
        Cursor todoListCursor = sqLiteDatabase.rawQuery(todoListSelect, null);
        while(todoListCursor.moveToNext()){
            String planId = todoListCursor.getString(0);
            String planName = todoListCursor.getString(1);
            String daysSelect = "SELECT * FROM " + DBHelper.TABLE_DAYS + " WHERE " + DBHelper.PLAN_ID +" = '" +
                    planId  +"' AND " + DBHelper.DATE + " = '" + day +"'";
            Cursor daysCursor = sqLiteDatabase.rawQuery(daysSelect, null);
            if(!daysCursor.moveToNext()){
                insertPlanTodo(planId, planName);
            }
        }

        String daysSelect = "SELECT * FROM " + DBHelper.TABLE_DAYS + " WHERE " + DBHelper.DATE + " = '" + day +"'";
        Cursor daysCursor = sqLiteDatabase.rawQuery(daysSelect, null);
        while(daysCursor.moveToNext()){
            todoList[0].add(daysCursor.getString(1));
            todoList[1].add(daysCursor.getString(2));
        }

        recyclerView = fragmentView.findViewById(R.id.todo_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager((MainActivity)getActivity()));

        dayTodoListRecyclerViewAdapter = new DayTodoListRecyclerViewAdapter(todoList[1], (MainActivity)getActivity());
        recyclerView.setAdapter(dayTodoListRecyclerViewAdapter);
    }

    public String makePlanId(){
        int intPlanId = (int) (Math.random() * 100000000);
        String StrPlanId = "p" + Integer.toString(intPlanId);
        return StrPlanId;
    }

    public boolean chkDupPlanId(String id){
        String planSelect = "SELECT * FROM " + DBHelper.TABLE_DAYS + " WHERE " + DBHelper.DATE + "= '" + day +"' AND " +
                DBHelper.PLAN_ID + " = '"  + id + "';";
        Cursor planCursor = sqLiteDatabase.rawQuery(planSelect, null);
        if(planCursor.moveToNext()) return false;
        return true;
    }

    public void insertTodo() {
        String planId = "";
        while(true){
            planId = makePlanId();
            if(chkDupPlanId(planId)) break;
        }

        String insertPlans = "INSERT INTO " + DBHelper.TABLE_DAYS + "(" + DBHelper.DATE + ", " +
                DBHelper.PLAN_ID + ", " + DBHelper.PLAN_NAME + ", " + DBHelper.CHECK + ") VALUES ('" + day +
                "', '" + planId +  "', '" + todoEditText.getText() + "', " + Integer.toString(0) +")";
        sqLiteDatabase.execSQL(insertPlans);

        todoEditText.setText("");
    }

    public void insertPlanTodo(String planId, String planName){
        String insertPlans = "INSERT INTO " + DBHelper.TABLE_DAYS + "(" + DBHelper.DATE + ", " +
                DBHelper.PLAN_ID + ", " + DBHelper.PLAN_NAME + ", " + DBHelper.CHECK + ") VALUES ('" + day +
                "', '" + planId +  "', '" + planName + "', " + Integer.toString(0) +")";
        sqLiteDatabase.execSQL(insertPlans);
    }

    public int getCurrentWeek(Date date) {
        calendar.setTime(date);
        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeekNumber;
    }

    public static void showDialog(int position, Activity activity) {
        View dialogView = activity.getLayoutInflater().inflate(R.layout.delete_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView deleteTextView = dialogView.findViewById(R.id.delete_textView);
        deleteTextView.setText(todoList[1].get(position) + "을/를\n 삭제하시겠습니까?");

        Button deleteOk = dialogView.findViewById(R.id.delete_dialog_ok);
        deleteOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG, "ok");
                deleteTodo(position);
                todoList[0].remove(position);
                todoList[1].remove(position);
                recyclerView.getAdapter().notifyItemRemoved(position);
                alertDialog.dismiss();
            }
        });

        Button deleteCancel = dialogView.findViewById(R.id.delete_dialog_cancel);
        deleteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


}
