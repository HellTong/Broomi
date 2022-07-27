package com.MKE.broomi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<Chat> arrayList;
    private Context context;

    public ChatAdapter(ArrayList<Chat> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat,parent,false);
        ChatViewHolder holder = new ChatViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Glide.with(holder.itemView).load(arrayList.get(position).getUserID());
        holder.TextView_userID_chatlist.setText(arrayList.get(position).getUserID());
        holder.TextView_chatwrite_chatlist.setText(arrayList.get(position).getChatWrite());
        holder.TextView_oppoID_chatlist.setText(arrayList.get(position).getOppoID());
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView TextView_userID_chatlist;
        TextView TextView_chatwrite_chatlist;
        TextView TextView_oppoID_chatlist;

        public ChatViewHolder(@NonNull View itemView) {

            super(itemView);
            this.TextView_userID_chatlist = itemView.findViewById(R.id.TextView_userID_chatlist);
            this.TextView_chatwrite_chatlist = itemView.findViewById(R.id.TextView_chatwrite_chatlist);
            this.TextView_oppoID_chatlist = itemView.findViewById(R.id.TextView_oppoID_chatlist);



        }
    }
}
