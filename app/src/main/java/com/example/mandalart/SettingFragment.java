package com.example.mandalart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



/*
추가 창에서 추가 누르면 디비에 빈 테이블이 들어간다(새 table id만 정해서!)
일단 첫 창에서 빈 테이블을 출력(왜냐면 들어왔따 나갔다 하면서 입력하려면 부분부분 저장한걸 땡겨와야함!)
 */


public class SettingFragment extends Fragment {

    Activity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        Button button = v.findViewById(R.id.btn_fragment_setting);
        activity = (MainActivity)getActivity();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        return v;

    }
    public void showDialog() {
        View dialogView = activity.getLayoutInflater().inflate(R.layout.add_calendar_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
