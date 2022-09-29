package com.example.mandalart;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        ArrayList<String> tableList = new ArrayList<>();
        ArrayList<String> tableId = new ArrayList<>();

        String tableSelect = "SELECT * FROM " + dbHelper.TABLE_MAIN + ";";
        Cursor tableCursor = sqLiteDatabase.rawQuery(tableSelect, null);
        while(tableCursor.moveToNext()){
            tableId.add(tableCursor.getString(0));
            tableList.add(tableCursor.getString(1));
        }

        RecyclerView recyclerView = findViewById(R.id.list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ListRecyclerViewAdapter listRecyclerViewAdapter = new ListRecyclerViewAdapter(tableList);
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
}
