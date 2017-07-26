package com.example.administrator.mrmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class UploadNotes extends MainActivity {
    LinearLayout dynamicContent,bottonNavBar;
    Button btn_Upload,btn_Reset,btn_Select;
    TextView text_fileName;
    String selected_class;
    Spinner spinner_class,spinner_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_upload_notes);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_upload_notes, null);
        dynamicContent.addView(wizard);

        //get the reference of RadioGroup.
        RadioButton rb=(RadioButton)findViewById(R.id.uploadnotes);

        // Change the corresponding icon and text color on nav button click.
        rb.setTextColor(Color.parseColor("#3F51B5"));

        spinner_class = (Spinner) findViewById(R.id.Spinner_Class);
        spinner_subject = (Spinner) findViewById(R.id.Spinner_Subject);


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Class_string, android.R.layout.simple_spinner_item);
        //ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Error_Subject_string, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_class.setAdapter(adapter1);
        //spinner_subject.setAdapter(adapter2);

        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                select();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    /*@Override*/
    public void select() {
            selected_class = ((Spinner) findViewById(R.id.Spinner_Class)).getSelectedItem().toString();
            if(selected_class.equals("11th")||selected_class.equals("12th"))
            {
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.E_and_T_Subject_string, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_subject.setAdapter(adapter1);
            }
            else if(selected_class.equals("8th")||selected_class.equals("9th"))
            {
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Common_Subject_string, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_subject.setAdapter(adapter1);
            }
            else
            {
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Tenth_Subject_string, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_subject.setAdapter(adapter1);
            }
        }
}
