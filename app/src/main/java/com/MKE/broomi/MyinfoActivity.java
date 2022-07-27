package com.MKE.broomi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;


public class MyinfoActivity extends AppCompatActivity {

    private TextView tv_id;
    private EditText EditText_info_nickname, EditText_info_number;
    private ImageView ImageView_profile_myinfo;
    private Button button_nickname_set, button_number_set,Button_profileCH_myinfo, button_nickname_check;
    private Uri imageUri;
    private UserAccount userAccount = new UserAccount();
    private DatabaseReference mDatabaseRef;
    private Review review;
    private RecyclerView RecyclerView_myinfo;
    String destuid = FirebaseAuth.getInstance().getCurrentUser().getUid();;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("broomi");
        EditText_info_nickname = findViewById(R.id.EditText_info_nickname);
        EditText_info_number = findViewById(R.id.EditText_info_number);
        ImageView_profile_myinfo = findViewById(R.id.ImageView_profile_myinfo);
        button_nickname_set = findViewById(R.id.button_nickname_set);
        button_number_set = findViewById(R.id.button_number_set);
        button_nickname_check =findViewById(R.id.button_nickname_check);
        Button_profileCH_myinfo = findViewById(R.id.Button_profileCH_myinfo);
        RecyclerView_myinfo = findViewById(R.id.RecyclerView_myinfo);
        tv_id = findViewById(R.id.tv_id);
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        RecyclerView_myinfo.setLayoutManager(new LinearLayoutManager(MyinfoActivity.this));
        RecyclerView_myinfo.setAdapter(new MyReviewRecyclerViewAdapter());

        tv_id.setText(userID + " 님");
        ImageView_profile_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        database.getReference().child("broomi").child("UserAccount").child(myuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userAccount = snapshot.getValue(UserAccount.class);
                EditText_info_number.setText(userAccount.getPhoneNumber());
                EditText_info_nickname.setText(userAccount.getUserNickname());
                FirebaseStorage.getInstance().getReference().child(myuid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(MyinfoActivity.this)
                             .load(userAccount.profileImageUrl)
                             .into(ImageView_profile_myinfo);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button_nickname_check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                mDatabaseRef.child("UserAccount").orderByChild("userNickname").equalTo(EditText_info_nickname.getText().toString()).
                        addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Toast.makeText(getApplicationContext(),"이미 존재하는 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                                    Log.i("여기","ㅇㅇㅇ");
                                    button_nickname_set.setVisibility(View.GONE);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"사용 가능한 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                                    button_nickname_set.setVisibility(View.VISIBLE);
                                    Log.i("여기","ㅇㅇㅇ2");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.i("nickname", "닉네임 중복체크 에러발생");
                            }
                        });
            }
        });

        EditText_info_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button_nickname_set.setVisibility(View.GONE);
            }
        });

       button_nickname_set.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                String updateNickname = EditText_info_nickname.getText().toString();
                database.getReference().child("broomi").child("UserAccount").child(myuid).child("userNickname").setValue(updateNickname);
                Intent intent = new Intent(MyinfoActivity.this, MenuActivity.class);
                Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });

        button_number_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateNumber = EditText_info_number.getText().toString();
                database.getReference().child("broomi").child("UserAccount").child(myuid).child("phoneNumber").setValue(updateNumber);
                Intent intent = new Intent(MyinfoActivity.this, MenuActivity.class);
                Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });
        Button_profileCH_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorage.getInstance().getReference().child(myuid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        FirebaseStorage.getInstance().getReference().child(myuid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                FirebaseDatabase.getInstance().getReference().child("broomi").child("UserAccount").child(myuid).child("profileImageUrl").setValue(imageUrl);
                                Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MyinfoActivity.this, MenuActivity.class);
                                startActivity(intent);

                            }
                        });
                    }
                });
            }
        });


    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        imageUri = intent.getData();
                        System.out.println("IMAGEURI= " + imageUri);
                        ImageView_profile_myinfo.setImageURI(imageUri);
                        Glide.with(MyinfoActivity.this)
                                .load(imageUri)
                                .into(ImageView_profile_myinfo);
                    }

                }
            });


    class MyReviewRecyclerViewAdapter extends RecyclerView.Adapter<MyReviewRecyclerViewAdapter.ViewHolder>
    {
        List<Review> arrayList;

        public MyReviewRecyclerViewAdapter() {
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
        public MyReviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyReviewRecyclerViewAdapter.ViewHolder holder, int position) {
            ViewHolder viewHolder = ((ViewHolder)holder);

            viewHolder.TextView_Nickname_listreview.setText(arrayList.get(position).getUsernick());
            viewHolder.TextView_review_listreview   .setText(arrayList.get(position).getReview());
            viewHolder.RatingBar_list.setRating(arrayList.get(position).getRating1());

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
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
