package com.example.mandalart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    FragmentManager fragmentManager;
    MandalArtFragment mandalArtFragment;
    DayFragment dayFragment;
    WeekFragment weekFragment;
    FragmentTransaction fragmentTransaction;
    OnBackPressedListener onBackPressedListener;

    public String tableId = null;
    public String topicId = null;

    int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqLiteDatabase);
        fragmentManager = getSupportFragmentManager();

        getTableId();

        mandalArtFragment = new MandalArtFragment();
        dayFragment = new DayFragment();
        weekFragment = new WeekFragment();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, mandalArtFragment).commitAllowingStateLoss();

    }

    public void clickHandler(View view){
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.btn_fragment_mandalart:
                if(flag==2){
                    ((Button)findViewById(R.id.btn_fragment_day)).setTextColor(getResources().getColor(R.color.textColor));
                }
                else if(flag==3){
                    ((Button)findViewById(R.id.btn_fragment_week)).setTextColor(getResources().getColor(R.color.textColor));
                }
                ((Button)findViewById(R.id.btn_fragment_mandalart)).setTextColor(getResources().getColor(R.color.yellow));
                flag=1;
                fragmentTransaction.replace(R.id.main_frame, mandalArtFragment).commitAllowingStateLoss();
                break;
            case R.id.btn_fragment_day:
                if(flag==1){
                    ((Button)findViewById(R.id.btn_fragment_mandalart)).setTextColor(getResources().getColor(R.color.textColor));
                }
                else if(flag==3){
                    ((Button)findViewById(R.id.btn_fragment_week)).setTextColor(getResources().getColor(R.color.textColor));
                }
                ((Button)findViewById(R.id.btn_fragment_day)).setTextColor(getResources().getColor(R.color.yellow));
                flag=2;
                fragmentTransaction.replace(R.id.main_frame, dayFragment).commitAllowingStateLoss();
                break;
            case R.id.btn_fragment_week:
                if(flag==1){
                    ((Button)findViewById(R.id.btn_fragment_mandalart)).setTextColor(getResources().getColor(R.color.textColor));
                }
                else if(flag==2){
                    ((Button)findViewById(R.id.btn_fragment_day)).setTextColor(getResources().getColor(R.color.textColor));
                }
                ((Button)findViewById(R.id.btn_fragment_week)).setTextColor(getResources().getColor(R.color.yellow));
                flag=3;
                fragmentTransaction.replace(R.id.main_frame, weekFragment).commitAllowingStateLoss();
                break;
            default:
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener){
        this.onBackPressedListener = onBackPressedListener;
    }
    @Override
    public void onBackPressed(){
        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if(fragment.equals(mandalArtFragment)){
            if(onBackPressedListener != null) onBackPressedListener.onBackPressed();
            else super.onBackPressed();
        }
        else{
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_frame, mandalArtFragment).commitAllowingStateLoss();
        }
    }

    public void getTableId(){
        SharedPreferences sharedPreferences = getSharedPreferences("table", Activity.MODE_PRIVATE);
        tableId = sharedPreferences.getString("tableId", null);
        if(tableId == null) tableId = "0";
    }

}