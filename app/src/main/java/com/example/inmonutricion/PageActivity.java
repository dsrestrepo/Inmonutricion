package com.example.inmonutricion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PageActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(PageActivity.this,"Hello"+ firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();
    }
}
