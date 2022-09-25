package com.example.mandalart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    FragmentManager fragmentManager;
    MandalArtFragment mandalArtFragment;
    DayFragment dayFragment;
    WeekFragment weekFragment;
    SettingFragment settingFragment;
    FragmentTransaction fragmentTransaction;
    OnBackPressedListener onBackPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqLiteDatabase);
        fragmentManager = getSupportFragmentManager();
        mandalArtFragment = new MandalArtFragment();
        dayFragment = new DayFragment();
        weekFragment = new WeekFragment();
        settingFragment = new SettingFragment();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, mandalArtFragment).commitAllowingStateLoss();

    }

    public void clickHandler(View view){
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.btn_fragment_mandalart:
                fragmentTransaction.replace(R.id.main_frame, mandalArtFragment).commitAllowingStateLoss();
                break;
            case R.id.btn_fragment_day:
                fragmentTransaction.replace(R.id.main_frame, dayFragment).commitAllowingStateLoss();
                break;
            case R.id.btn_fragment_week:
                fragmentTransaction.replace(R.id.main_frame, weekFragment).commitAllowingStateLoss();
                break;
            case R.id.btn_fragment_setting:
                fragmentTransaction.replace(R.id.main_frame, settingFragment).commitAllowingStateLoss();
                break;
            default:
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener){
        this.onBackPressedListener = onBackPressedListener;
    }
    @Override
    public void onBackPressed(){
        if(onBackPressedListener != null) onBackPressedListener.onBackPressed();
        else super.onBackPressed();
    }

}