package com.example.administrator.mrmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout LLUploadFiles,LLdashboard,LLstudent,LLattendance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LLUploadFiles = (LinearLayout) findViewById(R.id.LLuploadFiles);
        LLUploadFiles.setOnClickListener(this);

        LLdashboard = (LinearLayout) findViewById(R.id.LLdashboard);
        LLdashboard.setOnClickListener(this);

        LLstudent = (LinearLayout) findViewById(R.id.LLstudent);
        LLstudent.setOnClickListener(this);

        LLattendance = (LinearLayout) findViewById(R.id.LLattendance);
        LLattendance.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == LLUploadFiles)
        {
            startActivity(new Intent(this,UploadNotes.class));
        }
        else if(view == LLdashboard)
        {
            startActivity(new Intent(this,Dashboard.class));
        }
        else if(view == LLstudent)
        {
            startActivity(new Intent(this,Student.class));
        }
        else if(view == LLattendance)
        {
            startActivity(new Intent(this,Attendance.class));
        }
    }
}

