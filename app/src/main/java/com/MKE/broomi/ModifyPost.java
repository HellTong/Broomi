package com.MKE.broomi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ModifyPost extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private EditText EditText_title_modify_post,EditText_write_modify_post,EditText_modify_pay;
    private Button Button_accept_modify_post;
    private TextView textView_destUid_modify;
    UserAccount profile = new UserAccount();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_post);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Posts");
        Intent intent = getIntent();

        EditText_title_modify_post = findViewById(R.id.EditText_title_modify_post);
        EditText_write_modify_post = findViewById(R.id.EditText_write_modify_post);
        EditText_modify_pay = findViewById(R.id.EditText_modify_pay);

        Button_accept_modify_post = findViewById(R.id.Button_accept_modify_post);
        textView_destUid_modify = findViewById(R.id.textView_destUid_modify);
        String destuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        textView_destUid_modify.setText(destuid);
        FirebaseDatabase.getInstance().getReference().child("broomi").child("UserAccount").child(destuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profile = snapshot.getValue(UserAccount.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        String strTitle = intent.getStringExtra("postTitle");
        String strCost = intent.getStringExtra("postCost");
        String strWrite = intent.getStringExtra("postWrite");

        EditText_title_modify_post.setText(strTitle);
        EditText_write_modify_post.setText(strWrite);
        EditText_modify_pay.setText(strCost);

        Button_accept_modify_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTitle = EditText_title_modify_post.getText().toString();
                String strWrite = EditText_write_modify_post.getText().toString();
                String Pay = EditText_modify_pay.getText().toString();
                String userID= intent.getStringExtra("userID");

                String profileURL = profile.getProfileImageUrl();
                writePost(strTitle,strWrite,userID,Pay,destuid,profileURL);
                

                Toast.makeText(getApplicationContext(), "게시물이 수정되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ModifyPost.this, MenuActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });



    }


    public void writePost(String Title, String Write, String userID,String Pay,String destuid, String profile) {
        Post post = new Post(Title,Write,userID,Pay,destuid,profile);

        mDatabaseRef.child(Title).setValue(post);
    }
}