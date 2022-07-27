package com.MKE.broomi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InfoActivity extends AppCompatActivity {

    private TextView tv_id;
    private Button button_My_info;
    private Button button_My_list;
    private Button button_Report;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("broomi");

        tv_id = findViewById(R.id.tv_id);
        Intent intent = getIntent();
        String userID= intent.getStringExtra("userID");

        tv_id.setText(userID + " 님");

        button_My_info=findViewById(R.id.button_My_info);
        button_My_list=findViewById(R.id.button_My_list);
        button_Report=findViewById(R.id.button_Report);

        button_My_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(InfoActivity.this, MyinfoActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);

            }
        });

        button_My_list.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(InfoActivity.this, MylistActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);

            }
        });

        button_Report.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(InfoActivity.this, ReportActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);

            }
        });
    }
}