package com.MKE.broomi;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class PostInfoActivity extends AppCompatActivity {

    TextView TextView_userid_postinfo;
    TextView TextView_postwrite_postinfo;
    TextView TextView_cost_postinfo;
    TextView TextView_Title_postinfo;
    Button Button_postinfo_chat;
    ImageButton ImageButton_report_postinfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postinfo);

        Button_postinfo_chat = findViewById(R.id.Button_postinfo_chat);
        TextView_userid_postinfo = findViewById(R.id.TextView_userid_postinfo);
        TextView_postwrite_postinfo = findViewById(R.id.TextView_postwrite_postinfo);
        TextView_cost_postinfo = findViewById(R.id.TextView_cost_postinfo);
        TextView_Title_postinfo = findViewById(R.id.TextView_Title_postinfo);
        ImageButton_report_postinfo = findViewById(R.id.ImageButton_report_postinfo);
        Intent intent = getIntent();
        String postid = intent.getStringExtra("postID");
        String postwrite = intent.getStringExtra("postWrite");
        String postcost = intent.getStringExtra("postCost");
        String posttitle = intent.getStringExtra("postTitle");
        String userid = intent.getStringExtra("userID");
        String destuid = intent.getStringExtra("destUid");




        TextView_userid_postinfo.setText(postid + "님의 게시글 입니다.");
        TextView_cost_postinfo.setText("가격: " + postcost + "원");
        TextView_postwrite_postinfo.setText("내용: " +  postwrite);
        TextView_Title_postinfo.setText("제목: " + posttitle);

        TextView_userid_postinfo = findViewById(R.id.TextView_userid_postinfo);
        String userID= intent.getStringExtra("userID");
        String postID= intent.getStringExtra("postID");

        Button_postinfo_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userid.equals(postid)==true){
                    Toast.makeText(getApplicationContext(),"회원님 본인 게시물입니다.", Toast.LENGTH_SHORT).show();
                }else{

                    Intent intent1 = new Intent(PostInfoActivity.this,MessageActivity.class);
                    intent1.putExtra("userID",userid);
                    intent1.putExtra("postID", postID);
                    intent1.putExtra("destUid",destuid);
                    startActivity(intent1);
                }

            }
        });

        ImageButton_report_postinfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PostInfoActivity.this, ReportActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("postID", postID);
                startActivity(intent);

            }
        });

    }
}


