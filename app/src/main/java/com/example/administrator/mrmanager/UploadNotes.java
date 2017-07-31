
package com.example.administrator.mrmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;


public class UploadNotes extends MainActivity implements View.OnClickListener {
    LinearLayout LLselect;
    Button btn_Upload,btn_Reset;
    TextView text_fileName;
    String selected_class;
    String topic;
    int FILE_SELECT_CODE=1;
    private StorageReference mStorageRef;
    Spinner spinner_class,spinner_subject;
    Uri selectedFileUri;
    Uri downloadUrl1;
    EditText txt_topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("Notes");

        // Change the corresponding icon and text color on nav button click.

        LLselect = (LinearLayout) findViewById(R.id.LLselect);
        LLselect.setOnClickListener(this);
        btn_Upload = (Button) findViewById(R.id.Button_Upload);
        btn_Upload.setOnClickListener(this);
        btn_Reset = (Button) findViewById(R.id.Button_Reset);
        btn_Reset.setOnClickListener(this);
        txt_topic = (EditText) findViewById(R.id.txt_topic);
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
            // to close the onItemSelected
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
        if(view == LLselect)
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),FILE_SELECT_CODE);
        }
        else if(view == btn_Upload)
        {
            topic = txt_topic.getText().toString().trim();
           new HitUpload().execute();
        }
        else if(view == btn_Reset)
        {

        }
    }

    class HitUpload extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog progressDialog = new ProgressDialog(UploadNotes.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            progressDialog.setMessage("Uploading..");
        }

        @Override
        protected Void doInBackground(Void... voids) {

            StorageReference pdfRef = mStorageRef.child(selectedFileUri.getLastPathSegment());

            final String path = pdfRef.getPath();

            pdfRef.putFile(selectedFileUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")  double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    int currentprogress = (int) progress;
                    progressDialog.setMessage("Uploading.. "+currentprogress);

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            downloadUrl1=downloadUrl;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(),"Some Error Occured",Toast.LENGTH_LONG).show();
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("request code",requestCode+"");
        if(requestCode==FILE_SELECT_CODE)
        {
            try {
                Uri selectedFileUri1 = data.getData();
                selectedFileUri = Uri.fromFile(new File(selectedFileUri1.toString().trim()));
                text_fileName.setText(selectedFileUri.toString().trim());
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"No File Selected",Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


    /*private void Upload(Uri SFU,String topic)
    {

        StorageReference pdfRef = mStorageRef.child("notes/"+topic);

        pdfRef.putFile(SFU)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        downloadUrl1=downloadUrl;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(),"Some Error Occured",Toast.LENGTH_LONG);
                    }
                });
    }*/