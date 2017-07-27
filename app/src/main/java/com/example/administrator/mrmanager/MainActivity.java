package com.example.administrator.mrmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.LLuploadFiles);
        linearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == linearLayout)
        {
            startActivity(new Intent(this,UploadNotes.class));
        }
    }
}

