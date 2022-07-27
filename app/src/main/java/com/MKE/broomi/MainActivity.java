package com.MKE.broomi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {

    int nCurrentPermission = 0;
    static final int PERMISSION_REQUEST = 0x0000001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 2000);

    }


    public void onRequestPerMissionResult(int requsestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requsestCode) {
            case PERMISSION_REQUEST:

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "권한 설정 완료", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "권한 설정 실패", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

}