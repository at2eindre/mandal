package com.example.mandalart;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeekRecyclerViewAdapter extends RecyclerView.Adapter<WeekRecyclerViewAdapter.WeekViewHolder> {
    ArrayList<String> routine;
    Activity activity;

    public class WeekViewHolder extends RecyclerView.ViewHolder{
        TextView routineTitle;
        CheckBox[] checkBoxes= new CheckBox[8];

        WeekViewHolder(View itemView){
            super(itemView);
            routineTitle=itemView.findViewById(R.id.week_todo);
            checkBoxes[1]=itemView.findViewById(R.id.week_sun);
            checkBoxes[2]=itemView.findViewById(R.id.week_mon);
            checkBoxes[3]=itemView.findViewById(R.id.week_tue);
            checkBoxes[4]=itemView.findViewById(R.id.week_wed);
            checkBoxes[5]=itemView.findViewById(R.id.week_thu);
            checkBoxes[6]=itemView.findViewById(R.id.week_fri);
            checkBoxes[7]=itemView.findViewById(R.id.week_sat);

            for(int i=1;i<=7;i++){
                int finalI = i;
                checkBoxes[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            WeekFragment.updateRoutine(position, finalI);
                            notifyItemChanged(position);
                        }
                    }
                });
            }
        }
    }

    WeekRecyclerViewAdapter(ArrayList<String> routine, Activity activity){
        this.routine=routine;
        this.activity=activity;
    }


    @NonNull
    @Override
    public WeekRecyclerViewAdapter.WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.week_todo_list_recyclerview_item, parent, false);
        WeekRecyclerViewAdapter.WeekViewHolder weekViewHolder = new WeekRecyclerViewAdapter.WeekViewHolder(view);

        return weekViewHolder;
    }

    @Override
    public void onBindViewHolder(WeekRecyclerViewAdapter.WeekViewHolder holder, int position){
        String title = routine.get(position);
        holder.routineTitle.setText(title);
        for(int i=1;i<=7;i++) {
            boolean isClick = WeekFragment.isClicked(position, i);
            holder.checkBoxes[i].setClickable(isClick);
            if(isClick){
                holder.checkBoxes[i].setChecked(WeekFragment.isChecked(position, i));
            }
            else{
                holder.checkBoxes[i].setVisibility(View.INVISIBLE);
            }

        }
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount(){
        return routine.size();
    }

}
