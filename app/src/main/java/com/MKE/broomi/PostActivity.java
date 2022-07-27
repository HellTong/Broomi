package com.MKE.broomi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private EditText EditText_title_post,EditText_write_post,EditText_title_pay;
    private Button Button_accept_post;
    private TextView TextView_destUid_Post;
    UserAccount profile = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Posts");
        Intent intent = getIntent();

        EditText_title_post = findViewById(R.id.EditText_title_post);
        EditText_write_post = findViewById(R.id.EditText_write_post);
        EditText_title_pay = findViewById(R.id.EditText_title_pay);
        Button_accept_post = findViewById(R.id.Button_accept_post);
        TextView_destUid_Post = findViewById(R.id.TextView_destUid_Post);
        String destuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        TextView_destUid_Post.setText(destuid);
        FirebaseDatabase.getInstance().getReference().child("broomi").child("UserAccount").child(destuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profile = snapshot.getValue(UserAccount.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button_accept_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTitle = EditText_title_post.getText().toString();
                String strWrite = EditText_write_post.getText().toString();
                String Pay = EditText_title_pay.getText().toString();
                String userID= intent.getStringExtra("userID");
                String profileURL = profile.getProfileImageUrl();
                writePost(strTitle,strWrite,userID,Pay,destuid,profileURL);


                Toast.makeText(getApplicationContext(), "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PostActivity.this, MenuActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
   }
        });


    }



    public void writePost(String Title, String Write, String userID,String Pay, String destuid, String profile) {
        Post post = new Post(Title,Write,userID,Pay,destuid,profile);

        mDatabaseRef.child(Title).setValue(post);
    }
}


