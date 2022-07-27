package com.MKE.broomi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OtherUserViewActivity extends AppCompatActivity {
    ImageView ImageView_profile_other;
    TextView TextView_ID_other, TextView_phone_other, TextView_nickname_other;
    private Uri imageuri;
    private UserAccount userAccount = new UserAccount();
    private DatabaseReference mDatabaseRef;
    private RecyclerView reviewRecyclerView;
    public Review review = new Review();
    private String destuid;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_view);

        ImageView_profile_other = findViewById(R.id.ImageView_profile_other);
        TextView_ID_other = findViewById(R.id.TextView_ID_other);
        TextView_phone_other = findViewById(R.id.TextView_phone_other);
        TextView_nickname_other = findViewById(R.id.TextView_nickname_other);

        destuid = getIntent().getStringExtra("destuid");
        reviewRecyclerView = findViewById(R.id.Reviewrecyclerview);




        FirebaseDatabase.getInstance().getReference().child("broomi").child("UserAccount").child(destuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAccount = snapshot.getValue(UserAccount.class);
                TextView_ID_other.setText(userAccount.getEmailId());
                TextView_phone_other.setText(userAccount.getPhoneNumber());
                TextView_nickname_other.setText(userAccount.getUserNickname());
                FirebaseStorage.getInstance().getReference().child(destuid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(OtherUserViewActivity.this)
                                .load(userAccount.profileImageUrl)
                                .into(ImageView_profile_other);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(OtherUserViewActivity.this));
        reviewRecyclerView.setAdapter(new ReviewRecyclerViewAdapter());



    }

    class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder>
    {
        List<Review> arrayList;

        public ReviewRecyclerViewAdapter() {
            arrayList = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("Review").child(destuid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        review = snapshot1.getValue(Review.class);
                        arrayList.add(review);

                    }
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        @NonNull
        @Override
        public ReviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewRecyclerViewAdapter.ViewHolder holder, int position) {
            ViewHolder viewHolder = ((ViewHolder)holder);

            viewHolder.TextView_Nickname_listreview.setText(arrayList.get(position).getUsernick());
            viewHolder.TextView_review_listreview.setText(arrayList.get(position).getReview());
            viewHolder.RatingBar_list.setRating(arrayList.get(position).getRating1());

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            public TextView TextView_Nickname_listreview;
            public TextView TextView_review_listreview;
            public RatingBar RatingBar_list;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                TextView_Nickname_listreview = itemView.findViewById(R.id.TextView_Nickname_listreview);
                TextView_review_listreview = itemView.findViewById(R.id.TextView_review_listreview);
                RatingBar_list = itemView.findViewById(R.id.RatingBar_list);
            }
        }
    }




}