package com.example.easyattendance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String className;
    private String subjectName;
    private int position;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private final ArrayList<StudentItem> studentItems = new ArrayList<>();
    private DbManager dbManager;
    private long cid;
    private MyCalender calendar;
    private TextView subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        calendar = new MyCalender();

        dbManager = new DbManager(this);

        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");
        position = intent.getIntExtra("position",-1);
        cid = intent.getLongExtra("cid",-1);

        setStudentToolbar();
        loadData();

        recyclerView = findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this::changeStatus);
        loadStatusData();
    }


    private void loadData() {
        Cursor cursor = dbManager.getStudentTable(cid);
        studentItems.clear();
        while (cursor.moveToNext()){
            @SuppressLint("Range") long sid = cursor.getLong(cursor.getColumnIndex(DbManager.S_ID));
            @SuppressLint("Range") int roll = cursor.getInt(cursor.getColumnIndex(DbManager.STUDENT_ROLL_KEY));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DbManager.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(sid,roll,name));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();
        if (status.equals("P"))
            status = "A";
        else
            status = "P" ;

        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    @SuppressLint("SetTextI18n")
    public void setStudentToolbar(){
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subTitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        save.setOnClickListener(v -> saveStatus());

        title.setText(className);
        subtitle.setText(subjectName+" | "+calendar.getDate());

        back.setOnClickListener(v-> onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClicked);

    }

    private void saveStatus() {
        for (StudentItem studentItem : studentItems){
            String status = studentItem.getStatus();
            if (!status.equals("P"))
                status = "A";
            long value = dbManager.addStatus(studentItem.getSid(),cid,calendar.getDate(),status);

            if (value == -1)
                dbManager.updateStatus(studentItem.getSid(),calendar.getDate(),status);

            showToastMessage("Saved",1000);

        }
    }

    public void showToastMessage(String text,int duration){
        final  Toast toast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        },duration);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadStatusData(){
        for (StudentItem studentItem : studentItems){
            String status = dbManager.getStatus(studentItem.getSid(),calendar.getDate());
            if (status != null)
                studentItem.setStatus(status);
            else
                studentItem.setStatus("");
        }
        adapter.notifyDataSetChanged();
    }

    private boolean onMenuItemClicked(MenuItem menuItem){
        if (menuItem.getItemId() == R.id.add_student){
            showAddStudentDialog();
        }
        else if (menuItem.getItemId() == R.id.show_calender){
            showCalender();
        }
        else if (menuItem.getItemId() == R.id.show_attendance_sheet) {
            openSheetList();
        }
        return true;
    }

    private void openSheetList() {
        long[] idArray = new long[studentItems.size()];
        int[] rollArray = new int[studentItems.size()];
        String[] nameArray = new String[studentItems.size()];

        for (int i=0;i<idArray.length;i++)
            idArray[i]= studentItems.get(i).getSid();
        for (int i=0;i<rollArray.length;i++)
            rollArray[i] = studentItems.get(i).getRollNo();
        for (int i=0;i<nameArray.length;i++)
            nameArray[i] = studentItems.get(i).getName();

            Intent intent = new Intent(this, SheetListActivity.class);
            intent.putExtra("cid", cid);
            intent.putExtra("idArray", idArray);
            intent.putExtra("rollArray", rollArray);
            intent.putExtra("nameArray", nameArray);
            startActivity(intent);
    }

    private void showCalender() {
        calendar = new MyCalender();
        calendar.show(getSupportFragmentManager(),"");
        calendar.setOnCalenderOkClickListener(this::onCalenderOkClicked);
    }

    @SuppressLint("SetTextI18n")
    private void onCalenderOkClicked(int year, int month, int day) {
        calendar.setDate(year,month,day);
        subtitle.setText(subjectName+" | "+calendar.getDate());
        loadStatusData();
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener(this::addStudent);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addStudent(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
        long sid = dbManager.addStudent(cid,roll,name);
        StudentItem studentItem = new StudentItem(sid,roll,name);
        studentItems.add(studentItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deletedStudent(item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog = new MyDialog(studentItems.get(position).getRollNo(),studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string,name)->UpdateStudent(position,name));
    }

    private void UpdateStudent(int position, String name) {
        dbManager.updateStudent(studentItems.get(position).getSid(),name);
        studentItems.get(position).setName(name);
        adapter.notifyItemChanged(position);
    }



    public void deletedStudent(int position){
        dbManager.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
        adapter.notifyItemRemoved(position);
    }

}