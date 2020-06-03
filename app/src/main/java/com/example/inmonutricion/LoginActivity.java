package com.example.inmonutricion;
import android.content.Intent;
import androidx.annotation.NonNull;
//import androidx.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

//public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
public class LoginActivity extends AppCompatActivity {
    //Defining the variable
    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    SignInButton mSignInButton;
    DatabaseReference mDatabase;

    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 2338;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //define the variables
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnSignIn = findViewById(R.id.button2);
        tvSignUp = findViewById(R.id.textView);

        ////////////////////with google
/*        //with google
        // Assign fields
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);

        // Set click listeners
        mSignInButton.setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this *//* FragmentActivity *//*, this *//* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();*/


        ///////////////Login as a User connected before
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    ////as a admin
                    if(mFirebaseUser.getEmail().equals("admin@admin.com")) {
                        Toast.makeText(LoginActivity.this, "You are logged in as admin", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        startActivity(i);
                    }else{
                        ////as a regular user
                            Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };

        /////////////login
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                ///login as admin:
                if (email.equals("admin@admin.com")){
                    ////////login as a regular user
                    if (pwd.isEmpty()) {
                        password.setError("Please enter your password");
                        password.requestFocus();
                    } else if (!(email.isEmpty() && pwd.isEmpty())) {
                        mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intToHome = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                    startActivity(intToHome);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    ////////login as a regular user
                    if (email.isEmpty()) {
                        emailId.setError("Please enter email id");
                        emailId.requestFocus();
                    } else if (pwd.isEmpty()) {
                        password.setError("Please enter your password");
                        password.requestFocus();
                    } else if (email.isEmpty() && pwd.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Fields Are Empty!", Toast.LENGTH_SHORT).show();
                    } else if (!(email.isEmpty() && pwd.isEmpty())) {
                        mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intToHome);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intSignUp);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }


/////////////////////google signIn methods
/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign-In failed
                Log.e(TAG, "Google Sign-In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "WAT");
                        } else {
                            Log.d(TAG, "i am working");
                        }
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
*//*
                            mFirebaseAuth = FirebaseAuth.getInstance();

                            String email = acct.getDisplayName();

                            Map<String, Object> map = new HashMap<>();
                            map.put("email", email);

                            String id = mFirebaseAuth.getUid();
                            mDatabase.child("users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if (task2.isSuccessful()) {
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
*//*
                            Log.d(TAG, "i am working");
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }
                    }
                });
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }*/
}
