package com.example.mandalart;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DayTodoListRecyclerViewAdapter extends RecyclerView.Adapter<DayTodoListRecyclerViewAdapter.DayTodoListViewHolder> {
    ArrayList<String> todoList;
    Activity activity;
    public class DayTodoListViewHolder extends RecyclerView.ViewHolder{
        TextView listTodoTitle;
        CheckBox checkBox;
        ImageView delete;

        DayTodoListViewHolder(View itemView){
            super(itemView);

            listTodoTitle = itemView.findViewById(R.id.list_todo_title);
            checkBox = itemView.findViewById(R.id.day_todo_checkbox);
            delete = itemView.findViewById(R.id.delete_todo);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        DayFragment.updateTodo(position);
                        notifyItemChanged(position);
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                       DayFragment.showDialog(position, activity);
                    }
                }
            });
        }
    }

    DayTodoListRecyclerViewAdapter(ArrayList<String> list, Activity activity){
        todoList = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DayTodoListRecyclerViewAdapter.DayTodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.day_todo_list_recyclerview_item, parent, false) ;
        DayTodoListRecyclerViewAdapter.DayTodoListViewHolder dayTodoListViewHolder = new DayTodoListRecyclerViewAdapter.DayTodoListViewHolder(view) ;

        return dayTodoListViewHolder ;
    }

    @Override
    public void onBindViewHolder(DayTodoListRecyclerViewAdapter.DayTodoListViewHolder holder, int position) {
        String todoListTitle = todoList.get(position);
        holder.listTodoTitle.setText(todoListTitle);
        holder.checkBox.setChecked(DayFragment.isChecked(position));
        if(DayFragment.planOrNot(position)){
            holder.delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size() ;
    }
}
