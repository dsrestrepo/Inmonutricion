package com.example.inmonutricion;
import com.bumptech.glide.module.ManifestParser;
import com.example.inmonutricion.persistencia.UsuarioDAO;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Binder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

//public class HomeActivity extends AppCompatActivity {
public class HomeActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {
    public static final String ANONYMOUS = "anonymous";
    GoogleApiClient mGoogleApiClient;
    String mUsername;
    TextView userEmail;
    Button btnLogout,btngoto;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    //private FirebaseAuth.AuthStateListener mAuthStateListener;

    ///to ask for the DB
    DatabaseReference mDatabase; //reference to my main node


    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
        userEmail= findViewById(R.id.textView2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //User Message
        userEmail.setText("Welcome!!! "+firebaseUser.getEmail());
        ////////This is the logout action
        btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();*/
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(HomeActivity.this, LoginActivity.class);
                intToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intToMain);
            }
        });
        /////////Go to other activity
        btngoto = findViewById(R.id.button2);
        btngoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMain = new Intent(HomeActivity.this, PageActivity.class);
                startActivity(intToMain);
            }
        });
    }

    //////////////////////here we have the method to upload data
    private void uploadEventOnDB( int date, int time, String eventname, String description) {
        String dat = String.valueOf(date);
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        map.put("time", time);
        map.put("eventname", eventname);
        map.put("description", description);

        mDatabase.child("event").child(eventname).setValue(map);
        mDatabase.child("eventdate").child(dat).child(eventname).setValue(map);
    }

    /////////Here the calendar method
    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

        String yearstr = String.valueOf(year);
        String monthstr = String.valueOf(month+1);
        String daystr = String.valueOf(dayOfMonth);
        final String datestr = daystr+"-"+monthstr+"-"+yearstr;
        Toast.makeText(HomeActivity.this,"the date is"+ datestr,Toast.LENGTH_SHORT).show();
    }


}
