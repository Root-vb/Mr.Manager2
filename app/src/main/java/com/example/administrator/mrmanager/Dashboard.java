package com.example.administrator.mrmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class Dashboard extends MainActivity {
    LinearLayout dynamicContent,bottonNavBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dashboard);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_dashboard, null);
        dynamicContent.addView(wizard);


        //get the reference of RadioGroup.
        RadioButton rb=(RadioButton)findViewById(R.id.dashboard);

        // Change the corresponding icon and text color on nav button click.
        rb.setTextColor(Color.parseColor("#3F51B5"));
    }
}
