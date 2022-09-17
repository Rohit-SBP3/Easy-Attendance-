package com.example.easyattendance;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.classViewHolder>{

    ArrayList<ClassItem> classItems;
    Context context;

    private ClassAdapter.onItemClickListener onItemClickListener;
    public interface onItemClickListener{
        void onClick (int position);
    }

    public void setOnItemClickListener(ClassAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ClassAdapter(Context context, ArrayList<ClassItem> classItems) {
        this.context = context;
        this.classItems = classItems;
    }

    @NonNull
    @Override
    public ClassAdapter.classViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
        return new ClassAdapter.classViewHolder(itemView , onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.classViewHolder holder, int position) {
        holder.className.setText(classItems.get(position).getClassName());
        holder.subjectName.setText(classItems.get(position).getSubjectName());
    }

    @Override
    public int getItemCount() {
        return classItems.size();
    }

    public static class classViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView className;
        TextView subjectName;

        public classViewHolder(@NonNull View itemView, ClassAdapter.onItemClickListener onItemClickListener) {
            super(itemView);
            className = itemView.findViewById(R.id.class_tv);
            subjectName = itemView.findViewById(R.id.subject_tv);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"EDIT");
            menu.add(getAdapterPosition(),1,0,"DELETE");
        }
    }
}
