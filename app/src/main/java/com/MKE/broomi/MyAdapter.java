package com.MKE.broomi;

import static org.chromium.base.ContextUtils.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    private final ArrayList<PostList> arrayList;
    private final Context context;
    private OnitemClickListener mlistener = null;


    public MyAdapter(ArrayList<PostList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mylist, parent, false);
        return new MyViewHolder(view);
        //CustomViewHolder holder = new CustomViewHolder(view);
        //return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.ImageVIew_profile_postlist);
        holder.textView_list_userID.setText(arrayList.get(position).getUserID());
        holder.textView_list_title.setText(arrayList.get(position).getTitle());
        holder.textView_list_pay.setText(arrayList.get(position).getPay());
        holder.textView_list_write.setText(arrayList.get(position).getWrite());


    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }


    public void setOnItemClicklistener(OnitemClickListener listener) {

        this.mlistener = listener;
    }


    public interface OnitemClickListener {
        void onitemClick(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ImageVIew_profile_postlist;
        TextView textView_list_userID;
        TextView textView_list_pay;
        TextView textView_list_title;
        TextView textView_list_write;
        Button Button_delete_mylist;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView_list_userID = itemView.findViewById(R.id.textView_mylist_userID);
            this.textView_list_pay = itemView.findViewById(R.id.textView_mylist_pay);
            this.textView_list_title = itemView.findViewById(R.id.textView_mylist_title);
            this.textView_list_write = itemView.findViewById(R.id.textView_mylist_write);
            this.ImageVIew_profile_postlist = itemView.findViewById(R.id.ImageVIew_profile_mylist);
            this.Button_delete_mylist = itemView.findViewById(R.id.Button_delete_mylist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        if (mlistener != null) {
                            mlistener.onitemClick(v, position);
                        }
                    }
                }
            });
            Button_delete_mylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    FirebaseDatabase.getInstance().getReference().child("Posts").child(arrayList.get(position).getTitle()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {


                        }
                    });
                }
            });

        }

    }
}
