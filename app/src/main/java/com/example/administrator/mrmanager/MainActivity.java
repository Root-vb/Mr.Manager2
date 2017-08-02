package com.example.administrator.mrmanager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout LLUploadFiles, LLdashboard, LLstudent, LLattendance, LLSignOut;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isStoragePermissionGranted();
        mAuth = FirebaseAuth.getInstance();
        LLUploadFiles = (LinearLayout) findViewById(R.id.LLuploadFiles);
        LLUploadFiles.setOnClickListener(this);

        LLdashboard = (LinearLayout) findViewById(R.id.LLdashboard);
        LLdashboard.setOnClickListener(this);

        LLstudent = (LinearLayout) findViewById(R.id.LLstudent);
        LLstudent.setOnClickListener(this);

        LLattendance = (LinearLayout) findViewById(R.id.LLattendance);
        LLattendance.setOnClickListener(this);

        LLSignOut = (LinearLayout) findViewById(R.id.LLsignOut);
        LLSignOut.setOnClickListener(this);
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("","Permission is granted");
                return true;
            } else {

                Log.v("","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("","Permission is granted");
            return true;
        }
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
        } else if (view == LLSignOut) {
            mAuth.signOut();
            startActivity(new Intent(this, Login.class));
            finish();

        }
    }
}

