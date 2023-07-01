package com.example.yurt_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class resim_ekle extends AppCompatActivity {
    Button sec, yukle;
    ImageView imageView;
    TextView kurumid;
    EditText aciklama, baslik;
    FirebaseStorage storage;
    Random rand = new Random();
    // Setting the upper bound to generate the
    // random numbers in specific range
    int upperbound = 2500;
    // Generating random values from 0 - 24
    // using nextInt()
    int int_random = rand.nextInt(upperbound);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    StorageReference storageReference;
    Uri filePath;
    long mili;

    int k = 0;
    String metin;
    FirebaseFirestore firebaseFirestore;
    Map<String, Object> images = new HashMap<>();
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resim_ekle);
        sec = (Button) findViewById(R.id.choose_button);
        kurumid = (TextView) findViewById(R.id.textView8);
        baslik = (EditText) findViewById(R.id.titlee);
        yukle = (Button) findViewById(R.id.upload_button);
        aciklama = (EditText) findViewById(R.id.describ);
        imageView = (ImageView) findViewById(R.id.imageView);
        storage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = storage.getReference();

        sp = getSharedPreferences("kullanici_bilgi", MODE_PRIVATE);
        editor = sp.edit();
        metin=sp.getString("kurumid", null);
        kurumid.setText(sp.getString("kurumid", null));
        sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_image();
            }
        });

        yukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath == null) {
                    Toast.makeText(resim_ekle.this, "lütfen resim seçiniz", Toast.LENGTH_SHORT).show();

                } else if (aciklama.getText().toString().isEmpty()) {
                    aciklama.setError("lütfen açıklma ekleyiniz !");
                    aciklama.requestFocus();
                } else if (baslik.getText().toString().isEmpty()) {
                    baslik.setError("lütfen başlık ekleyiniz !");
                    baslik.requestFocus();
                } else {
                    uploadImage(filePath);
                }

            }
        });

    }

    private void select_image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            imageView.setImageURI(filePath);
        }
    }

    private void uploadImage(Uri filePath) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // Defining the child of storageReference
        final StorageReference ref = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(filePath));
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri filePath) {
                        urlModel model = new urlModel(filePath.toString());
                        Toast.makeText(resim_ekle.this, model.toString(), Toast.LENGTH_SHORT).show();
                        mili=System.currentTimeMillis();
                        myRef.child(metin).child("resimler").child(String.valueOf(mili)).child("url").setValue(model.getImageUrl());
                        myRef.child(metin).child("resimler").child(String.valueOf(mili)).child("title").setValue(baslik.getText().toString());
                        myRef.child(metin).child("resimler").child(String.valueOf(mili)).child("describtion").setValue(aciklama.getText().toString());
                       /* myRef = database.getReference("message/"+model+"/url");
                        myRef.setValue(model);*/
                        //url_firestore(model, k);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mili=System.currentTimeMillis();
                    }
                });
                Toast.makeText(resim_ekle.this, "başarılı", Toast.LENGTH_SHORT).show();
                mili=System.currentTimeMillis();
                progressDialog.cancel();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressDialog.show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                mili=System.currentTimeMillis();
                Toast.makeText(resim_ekle.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getFileExtension(Uri filePath) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(filePath));
    }

    private void url_firestore(urlModel model, int i) {


    }
}