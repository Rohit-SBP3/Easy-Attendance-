package com.example.easyattendance;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.studentViewHolder>{

    ArrayList<StudentItem> StudentItems;
    Context context;


    private StudentAdapter.onItemClickListener onItemClickListener;
    public interface onItemClickListener{
        void onClick (int position);
    }

    public void setOnItemClickListener(StudentAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentAdapter(Context context, ArrayList<StudentItem> StudentItems) {
        this.context = context;
        this.StudentItems = StudentItems;
    }

    @NonNull
    @Override
    public studentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return new studentViewHolder(itemView , onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull studentViewHolder holder, int position) {
        holder.rollNo.setText(StudentItems.get(position).getRollNo()+"");
        holder.name.setText(StudentItems.get(position).getName());
        holder.status.setText(StudentItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }

    private int getColor(int position) {
        String status = StudentItems.get(position).getStatus();
        if (status.equals("P"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.present)));
        else if (status.equals("A"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));
        return
                Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.white)));
    }

    @Override
    public int getItemCount() {
        return StudentItems.size();
    }

    public static class studentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView rollNo;
        TextView name;
        TextView status;
        CardView cardView;

        public studentViewHolder(@NonNull View itemView, StudentAdapter.onItemClickListener onItemClickListener) {
            super(itemView);
            rollNo = itemView.findViewById(R.id.rollNo);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.card_student);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"Edit");
            menu.add(getAdapterPosition(),1,0,"Delete");

        }
    }
}
