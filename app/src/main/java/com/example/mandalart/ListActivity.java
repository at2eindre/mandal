package com.example.mandalart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    static final String LOG = "ListActivityLog";
    static final String NULL = "null";
    static final int COUNT = 8;
    static DBHelper dbHelper;
    static SQLiteDatabase sqLiteDatabase;
    ImageView addTable;

    static ArrayList<String> tableList = new ArrayList<>();
    static ArrayList<String> tableId = new ArrayList<>();

    static RecyclerView recyclerView;
    ListRecyclerViewAdapter listRecyclerViewAdapter;

    @Override
    protected void onResume() {
        getTableList();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        addTable = (ImageView)findViewById(R.id.add_table);

        getTableList();

        addTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddTable();
            }
        });
    }

    public void toAddTable(){
        Intent intent = new Intent(this, AddTableActivity.class);
        Log.i(LOG, "addTable");
        startActivity(intent);

    }

    public void getTableList(){
        tableId.clear();
        tableList.clear();
        String tableSelect = "SELECT * FROM " + dbHelper.TABLE_MAIN + ";";
        Cursor tableCursor = sqLiteDatabase.rawQuery(tableSelect, null);
        while(tableCursor.moveToNext()){
            tableId.add(tableCursor.getString(0));
            tableList.add(tableCursor.getString(1));
        }

        recyclerView = findViewById(R.id.list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listRecyclerViewAdapter = new ListRecyclerViewAdapter(tableList, this);
        recyclerView.setAdapter(listRecyclerViewAdapter);

        listRecyclerViewAdapter.setOnItemClickListener(new ListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent();
                intent.putExtra("tableId", tableId.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    public static void showDialog(int position, Activity activity) {
        View dialogView = activity.getLayoutInflater().inflate(R.layout.delete_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView deleteTextView = dialogView.findViewById(R.id.delete_textView);
        deleteTextView.setText(tableList.get(position) + "을/를\n 삭제하시겠습니까?");

        Button deleteOk = dialogView.findViewById(R.id.delete_dialog_ok);
        deleteOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG, "ok");
                deleteTable(tableId.get(position));
                tableId.remove(position);
                tableList.remove(position);
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

    static void deleteTable(String tableId){
        ArrayList<String> topicId = new ArrayList<>();
        ArrayList<String>[] planId = new ArrayList[9];

        for(int i = 1; i<=COUNT; i++){
            planId[i] = new ArrayList<String>();
        }

        String mainSelect = "SELECT * FROM " + dbHelper.TABLE_SUB + " WHERE " + dbHelper.ID + " = '"  + tableId + "';";
        Cursor mainCursor = sqLiteDatabase.rawQuery(mainSelect, null);
        topicId.add(NULL);
        if(mainCursor.moveToNext()){
            for(int i = 1; i<=COUNT ; i++){
                topicId.add(mainCursor.getString(i));
            }
        }
        for(int i = 1 ; i<= COUNT; i++){
            planId[i].add(NULL);
            String subSelect = "SELECT * FROM " + dbHelper.TABLE_SSUB + " WHERE " + dbHelper.TOPIC_ID + " = '"  + topicId.get(i) + "';";
            Log.i(LOG, subSelect);
            Cursor subCursor = sqLiteDatabase.rawQuery(subSelect, null);
            if(subCursor.moveToNext()){
                for(int j = 1; j<= COUNT; j++){
                   planId[i].add(subCursor.getString(j));
                }
            }
        }

        deleteMain(tableId);

        for(int i = 1; i <= COUNT; i++){
            for(int j = 1; j <= COUNT; j++) {
                String deletePlans = "DELETE FROM " + dbHelper.TABLE_PLANS + " WHERE " + dbHelper.PLAN_ID +
                        " = '" + planId[i].get(j) + "';";
                sqLiteDatabase.execSQL(deletePlans);

                String deleteDays = "DELETE FROM " + dbHelper.TABLE_DAYS + " WHERE " + dbHelper.PLAN_ID +" = '" + planId[i].get(j) + "';";
                sqLiteDatabase.execSQL(deleteDays);
            }
        }

        deleteTopics(tableId, topicId);
    }

    public static void deleteMain(String tableId){
        String deleteMain = "DELETE FROM " + dbHelper.TABLE_MAIN + " WHERE " + dbHelper.ID +" = '" + tableId + "';";
        sqLiteDatabase.execSQL(deleteMain);


    }

    public static void deleteTopics(String tableId, ArrayList topicId) {
        String deleteSub = "DELETE FROM " + dbHelper.TABLE_SUB + " WHERE " + dbHelper.ID +" = '" + tableId + "';";
        sqLiteDatabase.execSQL(deleteSub);

        for(int i = 1; i <= COUNT; i++){
            String deleteTopics = "DELETE FROM " + dbHelper.TABLE_TOPICS + " WHERE " + dbHelper.TOPIC_ID +
                    " = '" + topicId.get(i) + "';";
            sqLiteDatabase.execSQL(deleteTopics);
        }

        for(int i = 1; i<= COUNT ; i++) {
            String deleteSsub = "DELETE FROM " + dbHelper.TABLE_SSUB + " WHERE " + dbHelper.TOPIC_ID +
                    " = '" + topicId.get(i) + "';";
            sqLiteDatabase.execSQL(deleteSsub);
        }
    }

}
