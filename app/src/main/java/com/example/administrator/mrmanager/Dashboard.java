package com.example.administrator.mrmanager;

import android.os.Bundle;
import android.widget.LinearLayout;

public class Dashboard extends MainActivity {
    LinearLayout dynamicContent,bottonNavBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }
}
