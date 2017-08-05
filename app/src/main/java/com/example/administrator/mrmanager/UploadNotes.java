
package com.example.administrator.mrmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


public class UploadNotes extends MainActivity implements View.OnClickListener {
    LinearLayout LLselect;
    Button btn_Upload,btn_Reset;
    TextView text_fileName;
    String selected_class;
    //ProgressDialog progressDialog;
    String HitUrl, Response = "false";
    int FILE_SELECT_CODE=1;
    private StorageReference mStorageRef;
    Spinner spinner_class,spinner_subject;
    Uri selectedFileUri;
    RadioGroup  RGQuestionType;
    RadioButton check_notes, check_ques, check_solved, check_unsolved;
    EditText txt_topic;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        LLselect = (LinearLayout) findViewById(R.id.LLselect);
        LLselect.setOnClickListener(this);
        btn_Upload = (Button) findViewById(R.id.Button_Upload);
        btn_Upload.setOnClickListener(this);
        btn_Reset = (Button) findViewById(R.id.Button_Reset);
        btn_Reset.setOnClickListener(this);
        txt_topic = (EditText) findViewById(R.id.txt_topic);
        text_fileName = (TextView) findViewById(R.id.Text_FileName);
        RGQuestionType = (RadioGroup) findViewById(R.id.RGQuestionType);
        check_notes = (RadioButton) findViewById(R.id.check_notes);
        check_ques = (RadioButton) findViewById(R.id.check_question);
        check_ques.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    RGQuestionType.setVisibility(View.VISIBLE);
                } else {
                    RGQuestionType.setVisibility(View.INVISIBLE);
                }
            }
        });
        check_solved = (RadioButton) findViewById(R.id.check_solved);
        check_unsolved = (RadioButton) findViewById(R.id.check_unsolved);

        spinner_class = (Spinner) findViewById(R.id.Spinner_Class);
        spinner_subject = (Spinner) findViewById(R.id.Spinner_Subject);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Class_string, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_class.setAdapter(adapter1);
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
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),FILE_SELECT_CODE);
        }
        else if(view == btn_Upload)
        {
            if (!txt_topic.getText().equals(null)) {
                if (selectedFileUri != null) {
                    createHitUrl();
                    UploadToFirebase();
                    //Reset();

                } else {
                    Toast.makeText(UploadNotes.this, "Please Select a file to Upload !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(UploadNotes.this, "Please Enter Topic !", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view == btn_Reset)
        {
            Reset();
        }
    }


    public void UploadToFirebase() {
        final ProgressDialog progressDialog = new ProgressDialog(UploadNotes.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Uploading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StorageReference pdfRef = mStorageRef.child(selectedFileUri.getLastPathSegment());

        pdfRef.putFile(selectedFileUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int currentprogress = (int) progress;
                progressDialog.setProgress(currentprogress);

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get a URL to the uploaded content
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                HitUrl += "&link=" + downloadUrl.toString();
                progressDialog.dismiss();
                new HitApi().execute();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadNotes.this, "Some Error Occured", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void Reset() {
        startActivity(new Intent(this, UploadNotes.class));
        finish();

    }

    public void createHitUrl() {
        HitUrl = "https://datahub.000webhostapp.com/add.php?";
        HitUrl += "&class=" + spinner_class.getSelectedItem().toString().trim();
        HitUrl += "&subject=" + spinner_subject.getSelectedItem().toString().trim();
        HitUrl += "&topic=" + txt_topic.getText().toString().trim();
        HitUrl = HitUrl.replaceAll(" ", "%20");
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("", "Problem building the URL ", e);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
            output.toString();
        }
        return output.toString();
    }

    class HitApi extends AsyncTask<Void, Void, Void> {

        final ProgressDialog progressDialog = new ProgressDialog(UploadNotes.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            progressDialog.setMessage("Completing..");
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url = createUrl(HitUrl);
            Log.e("E", HitUrl);

            // If the URL is null, then return early.

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    Response = readFromStream(inputStream);
                    Log.e("E", "response code: " + Response);
                } else {
                    Log.e("E", "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e("E", "Problem retrieving the earthquake JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // Closing the input stream could throw an IOException, which is why
                    // the makeHttpRequest(URL url) method signature specifies than an IOException
                    // could be thrown.
                    Log.e("E", "inputstreamNull");
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (Response.equals("true")) {
                Toast.makeText(UploadNotes.this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Reset();
            } else {
                Toast.makeText(UploadNotes.this, "Data Upload failed", Toast.LENGTH_SHORT).show();
                Reset();
            }

            // iv_Banner.startAutoScroll();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("request code",requestCode+"");
        if(requestCode==FILE_SELECT_CODE)
        {
            try {
                selectedFileUri = data.getData();
                text_fileName.setVisibility(View.VISIBLE);
                text_fileName.setText(selectedFileUri.getLastPathSegment().toString().trim());
            }catch (Exception e){
                selectedFileUri = null;
                Toast.makeText(UploadNotes.this, "No File Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
