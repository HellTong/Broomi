package com.MKE.broomi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ReviewActivity extends Activity {
    private DatabaseReference mDatabaseRef;
    private Button Button_review_complete;
    private RatingBar Rating_review;
    private EditText EditText_review;
    private String usernick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();
        String myuid = intent.getStringExtra("myuid");
        String destuid = intent.getStringExtra("destuid");

        Button_review_complete = findViewById(R.id.Button_review_complete);
        Rating_review = findViewById(R.id.Rating_review);
        EditText_review = findViewById(R.id.EditText_review);
        Rating_review.setOnRatingBarChangeListener(new Listener());

        FirebaseDatabase.getInstance().getReference().child("broomi").child("UserAccount").child(myuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = new UserAccount();
                userAccount = snapshot.getValue(UserAccount.class);
                usernick = userAccount.userNickname;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Review").child(destuid);
        Button_review_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("OnClick");
                float Rating = Rating_review.getRating();
                String review = EditText_review.getText().toString();
                writereview(myuid,destuid,Rating,review,usernick);
                Toast.makeText(getApplicationContext(), "후기가 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();


            }
        });




    }

    class Listener implements RatingBar.OnRatingBarChangeListener
    {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            Rating_review.setRating(rating);
        }
    }


    public void writereview (String myuid, String destuid, float rating, String review, String usernick){
        Review review1 = new Review(myuid, destuid, rating, review, usernick);
        mDatabaseRef.child(review).setValue(review1);
    }
}