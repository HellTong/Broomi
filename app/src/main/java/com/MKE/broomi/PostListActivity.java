package com.MKE.broomi;



import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class PostListActivity extends AppCompatActivity {

    DatabaseReference ref;
    RecyclerView recyclerView;
    SearchView searchView;


    private CustomAdapter adapter;
    ArrayList<PostList> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        //성철존
        ref = FirebaseDatabase.getInstance().getReference().child("Posts");
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);

        searchView = findViewById(R.id.searchView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        String userid = getIntent().getStringExtra("userID");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    PostList postList = snapshot1.getValue(PostList.class);

                    arrayList.add(postList);

                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PostListActivity",String.valueOf(error.toException()));
            }
        });

        adapter = new CustomAdapter(arrayList,this);
        adapter.setOnItemClicklistener(new CustomAdapter.OnitemClickListener() {
            @Override
            public void onitemClick(View v, int position) {
                Intent intent = new Intent(PostListActivity.this, PostInfoActivity.class);
                String postId = arrayList.get(position).getUserID();
                String postPay = arrayList.get(position).getPay();
                String postTitle = arrayList.get(position).getTitle();
                String postWrite = arrayList.get(position).getWrite();
                String destUid = arrayList.get(position).getDestUid();
                intent.putExtra("userID",userid);
                intent.putExtra("postID",postId);
                intent.putExtra("postCost",postPay);
                intent.putExtra("postTitle",postTitle);
                intent.putExtra("postWrite",postWrite);
                intent.putExtra("destUid",destUid);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        if (ref != null)
        {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        arrayList = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            arrayList.add(ds.getValue(PostList.class));
                        }

                        CustomAdapter customadapter = new CustomAdapter(arrayList, null);
                        customadapter.setOnItemClicklistener(new CustomAdapter.OnitemClickListener() {
                            @Override
                            public void onitemClick(View v, int position) {
                                Intent intent = new Intent(PostListActivity.this, PostInfoActivity.class);
                                String postId = arrayList.get(position).getUserID();
                                String postPay = arrayList.get(position).getPay();
                                String postTitle = arrayList.get(position).getTitle();
                                String postWrite = arrayList.get(position).getWrite();
                                String destUid = arrayList.get(position).getDestUid();
                                intent.putExtra("userID",userid);
                                intent.putExtra("postID",postId);
                                intent.putExtra("postCost",postPay);
                                intent.putExtra("postTitle",postTitle);
                                intent.putExtra("postWrite",postWrite);
                                intent.putExtra("destUid",destUid);
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(customadapter);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(PostListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(searchView != null)
        {
            //검색, 변경 이벤트 관리
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                //쿼리 텍스트를 변경할 때 호출
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                //쿼리를 제출할 때 호출
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }
    //이성철 검색기능
    private void search(String str)
    {
        ArrayList<PostList> myList = new ArrayList<>();
        for(PostList object : arrayList)
        {
            if(object.getTitle().toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        CustomAdapter customadapter = new CustomAdapter(myList, null);
        customadapter.setOnItemClicklistener(new CustomAdapter.OnitemClickListener() {
            @Override
            public void onitemClick(View v, int position) {
                Intent intent = new Intent(PostListActivity.this, PostInfoActivity.class);
                String postId = arrayList.get(position).getUserID();
                String postPay = arrayList.get(position).getPay();
                String postTitle = arrayList.get(position).getTitle();
                String postWrite = arrayList.get(position).getWrite();
                String destUid = arrayList.get(position).getDestUid();
                //intent.putExtra("userID",userid);
                intent.putExtra("postID",postId);
                intent.putExtra("postCost",postPay);
                intent.putExtra("postTitle",postTitle);
                intent.putExtra("postWrite",postWrite);
                intent.putExtra("destUid",destUid);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(customadapter);
    }
}
