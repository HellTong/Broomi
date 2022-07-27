package com.MKE.broomi;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;


public class MenuActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView tv_id, TextView_number, TextView_nickname;
    private Button button_Post;
    private Button button_PostList;
    private Button button_Info;
    private Button Button_ChatList_menu;
    private UserAccount userAccount = new UserAccount();
    private ImageView ImageView_profile_menu;



    //뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button_Post = findViewById(R.id.button_Post);
        button_PostList = findViewById(R.id.ImageButton_chat_menu);
        button_Info = findViewById(R.id.button_Info);
        Button_ChatList_menu = findViewById(R.id.Button_ChatList_menu);
        TextView_nickname = findViewById(R.id.TextView_nickname);
        tv_id = findViewById(R.id.tv_id);
        TextView_nickname = findViewById(R.id.TextView_nickname);
        TextView_number = findViewById(R.id.TextView_number);
        ImageView_profile_menu = findViewById(R.id.ImageView_profile_menu);

        String userID= FirebaseAuth.getInstance().getCurrentUser().getEmail();

        tv_id.setText(userID + " 님");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.getReference().child("broomi").child("UserAccount").child(myuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userAccount = snapshot.getValue(UserAccount.class);
                TextView_nickname.setText(userAccount.getUserNickname());
                TextView_number.setText(userAccount.getPhoneNumber());
                FirebaseStorage.getInstance().getReference().child(myuid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(MenuActivity.this)
                                .load(userAccount.profileImageUrl)
                                .into(ImageView_profile_menu);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        String userNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        TextView_number.setText(userNumber);


        button_Post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MenuActivity.this, PostActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);

            }
        });

        button_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, InfoActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        button_PostList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, PostListActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
        Button_ChatList_menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MenuActivity.this, ChatListActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed() {
        //기존의 뒤로가기 버튼의 기능 제거
        if (System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        // 2초 이내에 뒤로가기 버튼을 한번 더 클리식 finsih() (앱 종료)
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            finish();
        }
    }
}