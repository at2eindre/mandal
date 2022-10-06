package com.example.mandalart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ListViewHolder> {
    ArrayList<String> titleList;
    OnItemClickListener onItemClickListener = null;
    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView listTableTitle;
        ListViewHolder(View itemView){
            super(itemView);

            listTableTitle = itemView.findViewById(R.id.list_table_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(onItemClickListener != null){
                            onItemClickListener.onItemClick(view, position);
                        }
                    }
                }
            });
        }
    }

    ListRecyclerViewAdapter(ArrayList<String> list){
        titleList = list;
    }

    @NonNull
    @Override
    public ListRecyclerViewAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.list_recyclerview_item, parent, false) ;
        ListRecyclerViewAdapter.ListViewHolder listViewHolder = new ListRecyclerViewAdapter.ListViewHolder(view) ;

        return listViewHolder ;
    }

    @Override
    public void onBindViewHolder(ListRecyclerViewAdapter.ListViewHolder holder, int position) {
        String title = titleList.get(position) ;
        holder.listTableTitle.setText(title) ;
    }

    @Override
    public int getItemCount() {
        return titleList.size() ;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
