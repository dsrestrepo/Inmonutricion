package com.example.inmonutricion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivityGoogle extends AppCompatActivity implements CalendarView.OnDateChangeListener{
    public static final String ANONYMOUS = "anonymous";
    GoogleApiClient mGoogleApiClient;
    String mUsername;
    TextView userEmail;
    Button btnLogout,btngoto;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    //private FirebaseAuth.AuthStateListener mAuthStateListener;

    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_google);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
        userEmail= findViewById(R.id.textView2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        //User Message
        userEmail.setText("Welcome!!! "+firebaseUser.getEmail());
        ////////This is the logout action
        btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(HomeActivityGoogle.this, LoginActivity.class));
                finish();
/*                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                Intent intToMain = new Intent(HomeActivityGoogle.this, LoginActivity.class);
                intToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intToMain);*/
            }
        });
        /////////Go to other activity
        btngoto = findViewById(R.id.button2);
        btngoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMain = new Intent(HomeActivityGoogle.this, PageActivity.class);
                startActivity(intToMain);
            }
        });
    }

    /////////Here the calendar method
    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
/*        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final CharSequence [] items = new CharSequence[3];
        items[1]="There is an Event!!!!";
        items[2]="Add Event";
        items[3]="Cancel";*/

        String yearstr = String.valueOf(year);
        String monthstr = String.valueOf(month+1);
        String daystr = String.valueOf(dayOfMonth);
        final String datestr = daystr+"-"+monthstr+"-"+yearstr;
        //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        //String date = df.format(datestr);
        Toast.makeText(HomeActivityGoogle.this,"the date is"+ datestr,Toast.LENGTH_SHORT).show();
/*
        builder.setTitle("See events: ").setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which ==0){
                    Toast.makeText(HomeActivity.this,"the date is"+ datestr,Toast.LENGTH_SHORT).show();
                }else if(which==1){
                    Toast.makeText(HomeActivity.this,"the date is"+ datestr,Toast.LENGTH_SHORT).show();
                }else{
                    return;
                }
            }


        });
        AlertDialog dialog = builder.create();
        dialog.show();*/
    }

}