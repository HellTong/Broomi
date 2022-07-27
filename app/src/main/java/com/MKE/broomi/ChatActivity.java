package com.MKE.broomi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class ChatActivity extends AppCompatActivity {

    private ChatAdapter chatAdapter;
    private ArrayList<Chat> chatArrayList;
    private DatabaseReference mdatabaseRef;
    EditText EditText_chat_input, EditText_review;
    Button Button_chat_send, Button_review_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RecyclerView recyclerViewChat = findViewById(R.id.recyclerViewchat);
        recyclerViewChat.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewChat.setLayoutManager(layoutManager);
        chatArrayList = new ArrayList<>();
        String userid, oppoid;
        Button_chat_send = findViewById(R.id.Button_chat_send);
        EditText_chat_input = findViewById(R.id.EditText_chat_input);
        userid = getIntent().getStringExtra("userID");
        oppoid = getIntent().getStringExtra("oppoID");
        mdatabaseRef = FirebaseDatabase.getInstance().getReference("Chat");

        Button_chat_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String chatWrite = EditText_chat_input.getText().toString();

                sendChat(userid,chatWrite,oppoid);
            }
        });



        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = database.getReference("Chat");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatArrayList.clear();
                for(DataSnapshot snapshot2 : snapshot.getChildren()){
                    Chat chat = snapshot2.getValue(Chat.class);
                    chatArrayList.add(chat);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatActivity",String.valueOf(error.toException()));
            }
        });

        chatAdapter = new ChatAdapter(chatArrayList,this);
        recyclerViewChat.setAdapter(chatAdapter);

    }

    public void sendChat(String userID, String chatWrite, String oppoID){
        Chat chat = new Chat(userID,chatWrite,oppoID);

        mdatabaseRef.child(userID).setValue(chat);

    }

}
