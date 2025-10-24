package com.bear.DD.Protein;

import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bear.DD.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    int selectposition=-1;
    Button delete;
    List<String> data;

    public void addItem(String item,int position){
        data.add(position,item);
        notifyItemInserted(position);
    }
    public void addItem(String item){
        data.add(item);
        notifyItemInserted(data.size()-1);
    }
    public void removeItem(int position){
        data.remove(position);
        notifyItemRemoved(position);
        delete.setEnabled(false);
        selectposition=-1;
    }
    public MyAdapter(List<String> data){
        this.data=data;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.proteinlist_textview);
            itemView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(v -> {
                int previous=selectposition;
                selectposition=getAdapterPosition();
                delete.setEnabled(true);
                notifyItemChanged(previous);
                notifyItemChanged(selectposition);
            });
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_proteinlist_textview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        holder.itemView.setBackgroundColor(position==selectposition? Color.BLUE:Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
