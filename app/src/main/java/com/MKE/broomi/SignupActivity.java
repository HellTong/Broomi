package com.MKE.broomi;

import static android.graphics.Color.*;

import android.accounts.Account;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private Uri imageUri;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText EditText_ID_signup, EditText_PW_signup, EditText_Nickname_signup, EditText_Number_signup;
    private Button Button_signup_signup, Button_signup_nickname_check;
    private ImageView profile;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("broomi");

        EditText_ID_signup = findViewById(R.id.editText_Email_signup);
        EditText_PW_signup = findViewById(R.id.EditText_PW_signup);
        EditText_Nickname_signup = findViewById(R.id.EditText_Nickname_signup);
        EditText_Number_signup = findViewById(R.id.EditText_Number_signup);
        Button_signup_signup = findViewById(R.id.Button_signup_signup);
        Button_signup_nickname_check = findViewById(R.id.Button_signup_nickname_check);
        profile = findViewById(R.id.ImageView_profile_signup);
        Button_signup_signup.setVisibility(View.GONE);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(intent);

            }
        });

        Button_signup_nickname_check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                mDatabaseRef.child("UserAccount").orderByChild("userNickname").equalTo(EditText_Nickname_signup.getText().toString()).
                        addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Toast.makeText(getApplicationContext(),"이미 존재하는 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                                    Log.i("여기","ㅇㅇㅇ");
                                    Button_signup_signup.setVisibility(View.GONE);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"사용 가능한 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                                    Button_signup_signup.setVisibility(View.VISIBLE);
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

        EditText_Nickname_signup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Button_signup_signup.setVisibility(View.GONE);
            }
        });

        Button_signup_signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String strID = EditText_ID_signup.getText().toString();
                String strPW = EditText_PW_signup.getText().toString();

                mFirebaseAuth.createUserWithEmailAndPassword(strID, strPW).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = task.getResult().getUser().getUid();
                            if (imageUri == null) {
                                imageUri = Uri.parse("android.resource://" + getPackageName()+ "/" + R.drawable.broomi);
                            }
                            FirebaseStorage.getInstance().getReference().child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        FirebaseStorage.getInstance().getReference().child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                @SuppressWarnings("VisibleForTests")
                                                String imageUrl = uri.toString();
                                                String strNickname = EditText_Nickname_signup.getText().toString();
                                                String strPhoneNumber = EditText_Number_signup.getText().toString();
                                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                                UserAccount account = new UserAccount();
                                                account.setIdToken(firebaseUser.getUid());
                                                account.setEmailId(firebaseUser.getEmail());
                                                account.setUserNickname(strNickname);
                                                account.setPhoneNumber(strPhoneNumber);
                                                account.setProfileImageUrl(imageUrl);
                                                account.setPassword(strPW);

                                                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                                Toast.makeText(SignupActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }

                                                           });}
                    });
                        }

                        else{
                        Toast.makeText(SignupActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }}
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
                        profile.setImageURI(imageUri);
                        Glide.with(SignupActivity.this)
                                .load(imageUri)
                                .into(profile);
                    }

                }
            });
}
