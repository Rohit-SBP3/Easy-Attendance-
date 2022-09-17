package com.example.easyattendance;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {

    public static final String CLASS_ADD_DIALOG = "addClass";
    public static final String STUDENT_ADD_DIALOG = "addStudent";
    public static final String CLASS_UPDATE_DIALOG = "updateClass";
    public static final String STUDENT_UPDATE_DIALOG = "updateStudent";

    private onClickListener listener;

    private int rollNo;
    private String name;

    public MyDialog(int rollNo, String name) {
        this.rollNo = rollNo;
        this.name = name;
    }

    public MyDialog() {

    }

    public interface onClickListener{
        void onClick(String text1,String text2);
    }

    public void setListener(onClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if(getTag().equals(CLASS_ADD_DIALOG))dialog = getAddClassDialog();
        if(getTag().equals(STUDENT_ADD_DIALOG))dialog = getAddStudentDialog();
        if(getTag().equals(CLASS_UPDATE_DIALOG))dialog = getUpdateClassDialog();
        if(getTag().equals(STUDENT_UPDATE_DIALOG))dialog = getUpdateStudentDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;

    }

    private Dialog getUpdateClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("UPDATE CLASS");

        EditText class_edit = view.findViewById(R.id.edt01);
        EditText subject_edit = view.findViewById(R.id.edt02);

        class_edit.setHint("Class name");
        subject_edit.setHint("Subject name");
        Button cancel =  view.findViewById(R.id.cancel_button);
        Button add = view.findViewById(R.id.add_button);
        add.setText("Update");


        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String className = class_edit.getText().toString();
            String subName = subject_edit.getText().toString();
            listener.onClick(className,subName);
            dismiss();
        });
        return builder.create();
    }


    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("UPDATE STUDENT");

        EditText roll_edit = view.findViewById(R.id.edt01);
        EditText name_edit = view.findViewById(R.id.edt02);

        roll_edit.setHint("Roll");
        name_edit.setHint("Name");
        Button cancel =  view.findViewById(R.id.cancel_button);
        Button add = view.findViewById(R.id.add_button);
        add.setText("Update");
        roll_edit.setText(rollNo+"");
        name_edit.setText(name);
        roll_edit.setEnabled(false);/////////////////

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String roll = roll_edit.getText().toString();
            String name = name_edit.getText().toString();
            listener.onClick(roll,name);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("ADD NEW STUDENT");

        EditText roll_edit = view.findViewById(R.id.edt01);
        EditText name_edit = view.findViewById(R.id.edt02);

        roll_edit.setHint("Roll");
        name_edit.setHint("Name");
        Button cancel =  view.findViewById(R.id.cancel_button);
        Button add = view.findViewById(R.id.add_button);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String roll = roll_edit.getText().toString();
            String name = name_edit.getText().toString();
            roll_edit.setText(String.valueOf(Integer.parseInt(roll)+1));
            name_edit.setText(" ");
            listener.onClick(roll,name);
        });
        return builder.create();
    }


    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("ADD NEW CLASS");

        EditText class_edit = view.findViewById(R.id.edt01);
        EditText subject_edit = view.findViewById(R.id.edt02);

        class_edit.setHint("Class name");
        subject_edit.setHint("Subject name");
        Button cancel =  view.findViewById(R.id.cancel_button);
        Button add = view.findViewById(R.id.add_button);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String className = class_edit.getText().toString();
            String subName = subject_edit.getText().toString();
            listener.onClick(className,subName);
            dismiss();
        });
        return builder.create();
    }

}
