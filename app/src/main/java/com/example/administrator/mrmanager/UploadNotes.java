package com.example.administrator.mrmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class UploadNotes extends MainActivity implements View.OnClickListener {
    LinearLayout dynamicContent,bottonNavBar;
    Button btn_Upload,btn_Reset,btn_Select;
    TextView text_fileName;
    String selected_class;
    int FILE_SELECT_CODE=1;
    Spinner spinner_class,spinner_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);


        // Change the corresponding icon and text color on nav button click.

        btn_Select = (Button) findViewById(R.id.Button_Select);
        btn_Select.setOnClickListener(this);
        btn_Upload = (Button) findViewById(R.id.Button_Upload);
        btn_Upload.setOnClickListener(this);
        btn_Reset = (Button) findViewById(R.id.Button_Reset);
        btn_Reset.setOnClickListener(this);

        text_fileName = (TextView) findViewById(R.id.Text_FileName);

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

    @Override
    public void onClick(View view) {
        if(view == btn_Select)
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),FILE_SELECT_CODE);
        }
        else if(view == btn_Upload)
        {

        }
        else if(view == btn_Reset)
        {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILE_SELECT_CODE)
        {
            Uri selectedFileUri=data.getData();
            text_fileName.setText(selectedFileUri.toString().trim());

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
