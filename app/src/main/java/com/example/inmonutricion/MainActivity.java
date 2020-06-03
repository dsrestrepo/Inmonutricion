package com.example.inmonutricion;
import android.app.ProgressDialog;
import android.content.Intent;
import com.example.inmonutricion.utility.Constantes;
//import android.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inmonutricion.persistencia.UsuarioDAO;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.database.core.Context;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kbeanie.multipicker.api.Picker.PICK_IMAGE_DEVICE;

public class MainActivity extends AppCompatActivity {
    //Here We define the variables
    EditText emailId, password, nameId, countryId, confpassword;
    Button btnSignUp,picbutton;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    DatabaseReference mDatabase;
    ProgressDialog progresDialog;

    //CircleImageView profilepic;

    StorageReference mStorageRef;
    ImagePicker imagePicker;
    Uri profilepicuri;
    //private final int PICK_PHOTO = 1;
    //Then we start the on create of the app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //We associate the variables in java with the xml
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        countryId = findViewById(R.id.editText3);
        nameId = findViewById(R.id.editText4);
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        confpassword = findViewById(R.id.editText5);
        btnSignUp = findViewById(R.id.button2);
        tvSignIn = findViewById(R.id.textView);
        mStorageRef=FirebaseStorage.getInstance().getReference();

        progresDialog = new ProgressDialog(this);
        ///////////////////profile pic
/*
        profilepic =findViewById(R.id.profpic);
        imagePicker= new ImagePicker(this);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {

            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if (!list.isEmpty()){
                    String path= list.get(0).getOriginalPath();
                    profilepicuri=Uri.parse(path);
                    profilepic.setImageURI(profilepicuri);
                }

            }
            @Override
            public void onError(String s) {
                Toast.makeText(MainActivity.this, "Error!"+s, Toast.LENGTH_SHORT).show();
            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.pickImage();
            }
        });
*/


        ////////////////////////////////////////Here we define the SignUp with a regular User
        //we create the signUp button action
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            //when we click singIn we capture the password and email
            @Override
            public void onClick(View v) {
                //and convert to MAYUS
                //name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                //country.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                //Glide.with(this).load(Constants.)
                final String email = emailId.getText().toString().trim();
                final String pwd = password.getText().toString().trim();
                final String confpwd = confpassword.getText().toString().trim();
                final String nam = nameId.getText().toString().trim();
                final String coun = countryId.getText().toString().trim();
                //We define the exceptions email or password or both empty :/
                if (email.isEmpty()) {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields Are Empty!", Toast.LENGTH_SHORT).show();
                } else if (confpwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Confirm your password", Toast.LENGTH_SHORT).show();
                } else if ( !(pwd.equals(confpwd))) {
                    Toast.makeText(MainActivity.this, "Passwords must match", Toast.LENGTH_SHORT).show();
                } else if ( pwd.length()<6) {
                    Toast.makeText(MainActivity.this, "Passwords must have at least 6 characters", Toast.LENGTH_SHORT).show();
                }
                //they are not empty :) so we can search if the emil is valid
                else if (!(email.isEmpty() && pwd.isEmpty())) {
                    progresDialog.setMessage("registering user");
                    progresDialog.show();
                    //we use the firebase authentication method to verify
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {
                            //if the task is not correct, could ba a connection problem or short password
                            if (!task.isSuccessful()) {
                                mFirebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task3) {
                                        boolean confirm = !task3.getResult().getSignInMethods().isEmpty();
                                        if (confirm) {
                                            Toast.makeText(MainActivity.this, "This Email already exists", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            }
                            //if the task is correct we can go to new page and safe the data
                            //TASK SUCCESSFUL!!!!!!!!!
                            else {
                                ///profile pic
                                /*UsuarioDAO.getInstancia().subirFotoUri(profilepicuri, new UsuarioDAO.IDevolverUrlFoto() {
                                    @Override
                                    public void devolerUrlString(String url) {
                                        if (profilepicuri != null) {
                                            String pic =url;
                                            final String name= nam.toUpperCase();
                                            final String country= coun.toUpperCase();
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("email", email);
                                            map.put("password", pwd);
                                            map.put("name", name);
                                            map.put("country", country);
                                            map.put("profilepic" , pic);

                                            String id = mFirebaseAuth.getUid();
                                            mDatabase.child("users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task2) {
                                                    if (task2.isSuccessful()) {
                                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                        }
                                        else{
                                            String pic = Constantes.Url_Default_profilo_pic;
                                            final String name= nam.toUpperCase();
                                            final String country= coun.toUpperCase();
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("email", email);
                                            map.put("password", pwd);
                                            map.put("name", name);
                                            map.put("country", country);
                                            map.put("profilepic" , pic);

                                            String id = mFirebaseAuth.getUid();
                                            mDatabase.child("users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task2) {
                                                    if (task2.isSuccessful()) {
                                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                        }
                                    }
                                });*/
   /////////////////////////////////////////////////////without profile pic
                                final String name= nam.toUpperCase();
                                final String country= coun.toUpperCase();
                                Map<String, Object> map = new HashMap<>();
                                map.put("email", email);
                                map.put("password", pwd);
                                map.put("name", name);
                                map.put("country", country);
                                //map.put("profilepic" , pic);

                                String id = mFirebaseAuth.getUid();
                                mDatabase.child("users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task2) {
                                        if (task2.isSuccessful()) {
                                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                        } else {
                                            Toast.makeText(MainActivity.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
/////////////////////////////////////
                            }
                        }

                    });
                progresDialog.dismiss();
                }
                //some other error I don't know what
                else {
                    Toast.makeText(MainActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


    }
    ///si aldun dia me da por subir la foto de perfil
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_DEVICE && resultCode == RESULT_OK && data != null && data.getData() !=null){
            imagePicker.submit(data);
        }

    }

}

