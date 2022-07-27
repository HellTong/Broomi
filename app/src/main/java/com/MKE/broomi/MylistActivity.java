package com.MKE.broomi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MylistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PostList> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button Button_delete_mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);

        recyclerView = findViewById(R.id.myrecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        Button_delete_mylist = findViewById(R.id.Button_delete_mylist);
        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("Posts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    PostList postList = snapshot1.getValue(PostList.class);

                    if (postList.getUserID().equals(userID))
                        arrayList.add(postList);
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PostListActivity", String.valueOf(error.toException()));
            }
        });

        adapter = new MyAdapter(arrayList, this);
        adapter.setOnItemClicklistener(new MyAdapter.OnitemClickListener() {
            @Override
            public void onitemClick(View v, int position) {
                Intent intent = new Intent(MylistActivity.this, ModifyPost.class);
                String postPay = arrayList.get(position).getPay();
                String postTitle = arrayList.get(position).getTitle();
                String postWrite = arrayList.get(position).getWrite();
                intent.putExtra("postCost", postPay);
                intent.putExtra("postTitle", postTitle);
                intent.putExtra("postWrite", postWrite);
                intent.putExtra("userID", userID);
                startActivity(intent);

            }
        });

        recyclerView.setAdapter(adapter);

    }

    }

