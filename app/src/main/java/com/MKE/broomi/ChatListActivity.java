package com.MKE.broomi;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity
{
    private ChatListFragment adapter5;
    private ArrayList<ChatModel> chatModels = new ArrayList<>();
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        TextView item_chat_tv_title;
        RecyclerView recyclerView = findViewById(R.id.fragment_chat_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        item_chat_tv_title = findViewById(R.id.item_chat_tv_title);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatModels.clear();
                for (DataSnapshot item : snapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    
                    chatModels.add(chatModel);

                }
                adapter5.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter5 = new ChatListFragment(chatModels,this);
//        adapter5.setOnItemClicklistener(new ChatListFragment.OnitemClickListener() {
//            @Override
//            public void onitemClick(View v, int position) {
//                Intent intent = new Intent(ChatListActivity.this, MessageActivity.class);
//                startActivity(intent);
//            }
//        });
        recyclerView.setAdapter(adapter5);
    }
}