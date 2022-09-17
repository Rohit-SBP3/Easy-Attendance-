package com.example.easyattendance;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;
    ArrayList<ClassItem> classItems = new ArrayList<>();
    Toolbar toolbar;
    DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DbManager(this);

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> showDialog());

        loadData();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        classAdapter = new ClassAdapter(this,classItems);
        recyclerView.setAdapter(classAdapter);
        recyclerView.setLayoutManager(layoutManager);
        classAdapter.setOnItemClickListener(this::gotoItemActivity);


        setToolbar();

    }

    private void loadData() {
        Cursor cursor = dbManager.getClassTable();

        classItems.clear();
        while (cursor.moveToNext()){
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DbManager.C_ID));
            @SuppressLint("Range") String department = cursor.getString(cursor.getColumnIndex(DbManager.CLASS_NAME_KEY));
            @SuppressLint("Range") String subject = cursor.getString(cursor.getColumnIndex(DbManager.SUBJECT_NAME_KEY));

            classItems.add(new ClassItem(id,department,subject));
        }

    }


    @SuppressLint("SetTextI18n")
    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subTitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Easy Attendance");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this,StudentActivity.class);
        intent.putExtra("className",classItems.get(position).getClassName());
        intent.putExtra("subjectName",classItems.get(position).getSubjectName());
        intent.putExtra("cid",classItems.get(position).getCid());
        intent.putExtra("position",position);
        startActivity(intent);
    }

    private void showDialog(){
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_DIALOG);
        dialog.setListener(this::addClass);
    }

    @SuppressLint("NotifyDataSetChanged")
    private  void addClass(String className, String subjectName){
        long cid = dbManager.addClass(className,subjectName);
        ClassItem classItem = new ClassItem(cid,className,subjectName);
        classItems.add(classItem);
        classAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog updateDialog = new MyDialog();
        updateDialog.show(getSupportFragmentManager(),MyDialog.CLASS_UPDATE_DIALOG);
        updateDialog.setListener((className,subjectName)->updateClass(position,className,subjectName));
    }

    private void updateClass (int position,String className,String subjectName){
        dbManager.updateClass(classItems.get(position).getCid(),className,subjectName);
        classItems.get(position).setClassName(className);
        classItems.get(position).setSubjectName(subjectName);
        classAdapter.notifyItemChanged(position);
    }

    private void deleteClass(int position) {
        dbManager.deleteClass(classItems.get(position).getCid());
        classItems.remove(position);
        classAdapter.notifyItemRemoved(position);
    }

}