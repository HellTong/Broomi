package com.MKE.broomi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ChatListFragment extends RecyclerView.Adapter<ChatListFragment.ChatViewHolder> {

    private final ArrayList<ChatModel> arrayList;
    private final Context context;
    private OnitemClickListener mlistener = null;
    private UserAccount destUser = new UserAccount();
    private String destUid = null;
    public ChatListFragment(ArrayList<ChatModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);
        ChatViewHolder holder = new ChatViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListFragment.ChatViewHolder holder, int position) {
              final ChatViewHolder chatViewHolder = (ChatViewHolder) holder;

              String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

              for(String user : arrayList.get(position).users.keySet()){
                  if(!user.equals(uid)){
                      destUid = user;
                  }
              }
              System.out.println("DESTUID : " + destUid);

              Map<String,ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
              commentMap.putAll(arrayList.get(position).comments);
              String lastMessageKey = (String) commentMap.keySet().toArray()[0];
              FirebaseDatabase.getInstance().getReference().child("broomi").child("UserAccount").child(destUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        destUser = snapshot.getValue(UserAccount.class);
                        Glide.with(holder.itemView.getContext())
                            .load(destUser.profileImageUrl)
                            .into(chatViewHolder.item_chat_imageView);
                        holder.item_chat_tv_title.setText(destUser.userNickname);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {



            }
        });


              holder.item_chat_tv_comment.setText(arrayList.get(position).comments.get(lastMessageKey).message);

              holder.item_chat_tv_title2.setText(destUid);
              holder.itemView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        String destUID = holder.item_chat_tv_title2.getText().toString();
                        Intent intent = new Intent(context, MessageActivity.class);
                        intent.putExtra("destUid",destUID);
                        context.startActivity(intent);
                  }
              });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
}

    public void setOnItemClicklistener(OnitemClickListener listener) {

        this.mlistener = listener;
    }


    public interface OnitemClickListener {
        void onitemClick(View v, int position);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView item_chat_tv_title;
        TextView item_chat_tv_comment;
        ImageView item_chat_imageView;
        TextView item_chat_tv_title2;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.item_chat_imageView = itemView.findViewById(R.id.item_chat_imageView);
            this.item_chat_tv_title = itemView.findViewById(R.id.item_chat_tv_title);
            this.item_chat_tv_comment = itemView.findViewById(R.id.item_chat_tv_comment);
            this.item_chat_tv_title2 = itemView.findViewById(R.id.item_chat_tv_title2);


        }
    }
}
