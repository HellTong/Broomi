package com.MKE.broomi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ReportActivity extends AppCompatActivity {

    private TextView tv_id, TextView6;
    private DatabaseReference mDatabaseRef;
    private EditText EditText_Title_report,EditText_write_report;
    private Button Button_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        TextView6 = findViewById(R.id.TextView6);


        tv_id = findViewById(R.id.tv_id);

        Intent intent = getIntent();
        String userID= intent.getStringExtra("userID");
        String postID = intent.getStringExtra("postID");


        tv_id.setText(userID);
        TextView6.setText(postID);


        Button Button = (Button) findViewById(R.id.Button_report);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Report");

        EditText_Title_report = findViewById(R.id.EditText_Title_report);
        EditText_write_report = findViewById(R.id.EditText_write_report);
        Button_report = findViewById(R.id.Button_report);

        Button_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTitle = EditText_Title_report.getText().toString();
                String strWrite = EditText_write_report.getText().toString();
                String strUID = tv_id.getText().toString();
                String strPID = TextView6.getText().toString();
                String userID= intent.getStringExtra("userID");

                writeReport(strTitle, strUID,strPID ,strWrite);


                Toast.makeText(getApplicationContext(), "신고/건의가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

                startActivity(intent);

            }
        });
    }

    public void writeReport(String strTitle, String Title, String UID, String Write) {
        Report report = new Report(Title, UID, Write);
        mDatabaseRef.child(Title).setValue(report);
    }
}