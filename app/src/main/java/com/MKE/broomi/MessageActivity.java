package com.MKE.broomi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;

//import org.chromium.base.Callback;
//import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class MessageActivity extends AppCompatActivity{
    TextView ChatPostID;
    ImageButton Button_reports;
    private String chatRoomUid;
    private String myuid;
    private String destUid;

    private RecyclerView recyclerView;
    private Button button , Button_comple_message,Button_destInfo_message;
    private EditText editText;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;

    public UserAccount destUser;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy.MM.dd HH:mm");

//    @Override
//    public void onBackPressed() {

//    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        destUser = new UserAccount();

        Button_comple_message = findViewById(R.id.Button_comple_message);
        Button_reports = findViewById(R.id.Button_reports);
        Button_destInfo_message = findViewById(R.id.Button_destInfo_message);
        Intent intent = getIntent();
        String postid = intent.getStringExtra("postID");
        String userID= intent.getStringExtra("userID");
        String postID= intent.getStringExtra("postID");

        Button_reports.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MessageActivity.this, ReportActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("postID", postID);
                startActivity(intent);
            }
        });

        Button_comple_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageActivity.this, ReviewActivity.class);
                intent.putExtra("myuid",myuid);
                intent.putExtra("destuid",destUid);
                startActivity(intent);

            }
        });
        Button_destInfo_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageActivity.this, OtherUserViewActivity.class);
                intent.putExtra("destuid",destUid);
                startActivity(intent);

            }
        });

        ChatPostID = findViewById(R.id.ChatPostID);

        ChatPostID.setText(postid);

        init();
        sendMsg();
        System.out.println("MYUID=" + myuid);
        System.out.println("DESTUID= " + destUid);

    }


    private void init(){
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        destUid = getIntent().getStringExtra("destUid");

        recyclerView = findViewById(R.id.message_recyclerview);
        button = findViewById(R.id.message_btn);
        editText =  findViewById(R.id.message_editText);

        firebaseDatabase = FirebaseDatabase.getInstance();

        if(editText.getText().toString() == null) button.setEnabled(false);
        else button.setEnabled(true);

        checkChatRoom();
    }
    private void sendMsg()
    {
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(myuid,true);
                chatModel.users.put(destUid,true);
                //채팅방 key 생성
                if(chatRoomUid == null){
                    Toast.makeText(MessageActivity.this , "채팅방 생성",Toast.LENGTH_SHORT).show();
                    button.setEnabled(false);
                    firebaseDatabase.getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("됐나여");
                            checkChatRoom();

                        }
                    });
                }else{
                    sendMsgToDataBase();
                }
            }
        });
    }

    private void sendMsgToDataBase(){
        if(!editText.getText().toString().equals("")){
            ChatModel.Comment comment = new ChatModel.Comment();
            comment.uid = myuid;
            comment.message = editText.getText().toString();
            comment.timetamp = ServerValue.TIMESTAMP;
            firebaseDatabase.getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editText.setText("");
                }
            });
        }
    }

    private void checkChatRoom(){
        firebaseDatabase.getReference().child("chatrooms").orderByChild("users/" + myuid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    if(chatModel.users.containsKey(destUid)){
                        chatRoomUid = dataSnapshot.getKey();
                        button.setEnabled(true);

                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                        sendMsgToDataBase();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
    {
        List<ChatModel.Comment> comments;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("broomi").child("UserAccount").child(destUid).addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    destUser = snapshot.getValue(UserAccount.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void getMessageList(){
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    comments.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        comments.add(dataSnapshot.getValue(ChatModel.Comment.class));

                    }
                    notifyDataSetChanged();

                    recyclerView.scrollToPosition(comments.size()-1);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messagebox,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position){
            ViewHolder viewHolder = ((ViewHolder)holder);




            if(comments.get(position).uid.equals(myuid)){
                viewHolder.textViewMsg.setText(comments.get(position).message);
                viewHolder.textViewMsg.setBackgroundResource(R.drawable.rightbubble);
                viewHolder.linearLayoutDest.setVisibility(View.INVISIBLE);
                viewHolder.linearLayoutRoot.setGravity(Gravity.RIGHT);
                viewHolder.linearLayoutTime.setGravity(Gravity.RIGHT);
            }else{
                Glide.with(holder.itemView.getContext())
                        .load(destUser.profileImageUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(viewHolder.imageViewProfile);
                viewHolder.textViewName.setText(destUser.userNickname);
                viewHolder.linearLayoutDest.setVisibility(View.VISIBLE);
                viewHolder.textViewMsg.setBackgroundResource(R.drawable.leftbubble);
                viewHolder.textViewMsg.setText(comments.get(position).message);
                viewHolder.linearLayoutRoot.setGravity(Gravity.LEFT);
                viewHolder.linearLayoutTime.setGravity(Gravity.LEFT);
            }
            viewHolder.textViewTimeStamp.setText(getDateTime(position));

        }

        public String getDateTime(int position){
            long unixTime = (long) comments.get(position).timetamp;
            Date date = new Date(unixTime);
            String time = simpleDateFormat.format(date);
            return time;
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder{

            public TextView textViewMsg;
            public TextView textViewName;
            public TextView textViewTimeStamp;
            public ImageView imageViewProfile;
            public LinearLayout linearLayoutDest;
            public LinearLayout linearLayoutRoot;
            public LinearLayout linearLayoutTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewMsg = (TextView)itemView.findViewById(R.id.item_messagebox_textview_msg);
                textViewName = (TextView)itemView.findViewById(R.id.item_messagebox_TextView_name);
                textViewTimeStamp = (TextView)itemView.findViewById(R.id.item_messagebox_textview_timestamp);
                imageViewProfile = (ImageView)itemView.findViewById(R.id.item_messagebox_ImageView_profile);
                linearLayoutDest = (LinearLayout)itemView.findViewById(R.id.item_messagebox_LinearLayout);
                linearLayoutRoot = (LinearLayout)itemView.findViewById(R.id.item_messagebox_root);
                linearLayoutTime = (LinearLayout)itemView.findViewById(R.id.item_messagebox_layout_timestamp);
            }




        }
    }
}