package com.example.inmonutricion.persistencia;
import android.net.Uri;

import androidx.annotation.NonNull;

import  com.example.inmonutricion.utility.Constantes;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UsuarioDAO {
    public interface IDevolverUrlFoto{
        public void devolerUrlString(String url);
    }

    private static UsuarioDAO usuarioDAO;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference referenceUsuarios;
    private StorageReference referenceFotoDePerfil;

    public static UsuarioDAO getInstancia(){
        if(usuarioDAO==null) usuarioDAO = new UsuarioDAO();
        return usuarioDAO;
    }

    private UsuarioDAO(){
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        referenceUsuarios = database.getReference(Constantes.NODO_USUARIOS);
        referenceFotoDePerfil = storage.getReference("Fotos/FotoPerfil/"+getKeyUsuario());
    }

    public String getKeyUsuario(){
        return FirebaseAuth.getInstance().getUid();
    }

    public long fechaDeCreacionLong(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();
    }

    public long fechaDeUltimaVezQueSeLogeoLong(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();
    }
    public void subirFotoUri(Uri uri, final IDevolverUrlFoto iDevolverUrlFoto){
        String nombreFoto = "";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("SSS.ss-mm-hh-dd-MM-yyyy", Locale.getDefault());
        nombreFoto = simpleDateFormat.format(date);
        final StorageReference fotoReferencia = referenceFotoDePerfil.child(nombreFoto);
        fotoReferencia.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return fotoReferencia.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri uri = task.getResult();
                    iDevolverUrlFoto.devolerUrlString(uri.toString());
                }
            }
        });
    }

}
