package com.example.inmonutricion;
import com.bumptech.glide.module.ManifestParser;
import com.example.inmonutricion.persistencia.UsuarioDAO;

import android.app.DatePickerDialog;
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
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//public class HomeActivity extends AppCompatActivity {
public class AdminHomeActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener, DatePickerDialog.OnDateSetListener {
    public static final String ANONYMOUS = "anonymous";
    //GoogleApiClient mGoogleApiClient;
    String mUsername;
    TextView userEmail;
    Button btnLogout,btndate;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    //private FirebaseAuth.AuthStateListener mAuthStateListener;
    //////////to upload data
    Button loadButton;
    TextView dateTV;
    EditText eventname, eventeditor,eventhour,eventdescription;

    ///to ask for the DB
    DatabaseReference mDatabase; //reference to my main node

    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
        userEmail= findViewById(R.id.textView2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //////load variables event
        eventname= findViewById(R.id.editText4);
        eventhour= findViewById(R.id.editText3);
        eventdescription= findViewById(R.id.editText2);
        eventeditor= findViewById(R.id.editText1);
        dateTV= findViewById(R.id.editText);


        //User Message
        userEmail.setText("Welcome!!! "+firebaseUser.getEmail());
        ////////This is the logout action
        btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(AdminHomeActivity.this, LoginActivity.class);
                intToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intToMain);
            }
        });
        //////////////////event
        ////////This is the logout action
        /////
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(AdminHomeActivity.this, LoginActivity.class);
                intToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intToMain);
            }
        });

        /////////Here we open the calendar
        btndate = findViewById(R.id.button2);
        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
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
        Toast.makeText(AdminHomeActivity.this,"the date is"+ datestr,Toast.LENGTH_SHORT).show();
        //catch date
        //dateTV.setText("the date is "+datestr);

    }

    ///////the fragment method
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView dateTV = (TextView) findViewById(R.id.textView);
//        dateTV.setText(currentDateString);
    }

}
