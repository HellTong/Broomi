package com.MKE.broomi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {


    private final ArrayList<PostList> arrayList;
    private final Context context;
    private OnitemClickListener mlistener = null;


    public CustomAdapter(ArrayList<PostList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;


    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_postlist, parent, false);
        return new CustomViewHolder(view);
        //CustomViewHolder holder = new CustomViewHolder(view);
        //return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
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

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView ImageVIew_profile_postlist;
        TextView textView_list_userID;
        TextView textView_list_pay;
        TextView textView_list_title;
        TextView textView_list_write;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView_list_userID = itemView.findViewById(R.id.textView_list_userID);
            this.textView_list_pay = itemView.findViewById(R.id.textView_list_pay);
            this.textView_list_title = itemView.findViewById(R.id.textView_list_title);
            this.textView_list_write = itemView.findViewById(R.id.textView_list_write);
            this.ImageVIew_profile_postlist = itemView.findViewById(R.id.ImageVIew_profile_postlist);
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
        }

    }
}
