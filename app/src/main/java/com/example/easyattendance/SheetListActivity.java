package com.example.easyattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {
    private ArrayList<String> listItems = new ArrayList<>();
    ListView sheetList;
    ArrayAdapter<String> adapter;
    private String className;
    private String subjectName;
    private long cid;
    Toolbar toolbar;
    TextView title;
    TextView subtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        setStudentToolbar();

        cid = getIntent().getLongExtra("cid", -1);
        loadListItems();
        sheetList = findViewById(R.id.sheet_list);
        adapter = new ArrayAdapter<>(this, R.layout.sheet_list, R.id.date_list_item, listItems);
        sheetList.setAdapter(adapter);

        sheetList.setOnItemClickListener((parent, view, position, id) -> openSheetActivity(position));

    }

    private void openSheetActivity(int position) {

            try {
                long[] idArray = getIntent().getLongArrayExtra("idArray");
                int[] rollArray = getIntent().getIntArrayExtra("rollArray");
                String[] nameArray = getIntent().getStringArrayExtra("nameArray");
                Intent intent = new Intent(this, SheetActivity.class);
                intent.putExtra("idArray", idArray);
                intent.putExtra("rollArray", rollArray);
                intent.putExtra("nameArray", nameArray);
                intent.putExtra("month", listItems.get(position));

                 startActivity(intent);
            }catch (Exception e) {
                Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
            }
    }

    private void loadListItems() {
        Cursor cursor = new DbManager(this).getDistinctMonths(cid);

        Log.i("1234567890","loadListItems: "+cursor.getCount());

        while (cursor.moveToNext()){
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DbManager.DATE_KEY));
            listItems.add(date.substring(3));
        }
    }

    public void setStudentToolbar(){

        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");

        toolbar = findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subTitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        save.setVisibility(View.INVISIBLE);

        title.setText("Attendance Record");
        subtitle.setVisibility(View.INVISIBLE);
        //subtitle.setText(className+subjectName);

        back.setOnClickListener(v-> onBackPressed());
    }


}