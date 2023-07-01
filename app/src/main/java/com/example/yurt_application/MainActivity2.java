package com.example.yurt_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    Button uyeOl, giris;
    private UserModel mUser;
    EditText Email, Password, Isim, Soyisim, Kurumid;
    ProgressDialog progressDialog;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    int i = 0;
    TextView textView;
    FileOutputStream outputStream1, outputStream2, outputStream3, outputStream4, outputStream5, outputStream6;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayList<String> institution_id = new ArrayList<String>();
    FirebaseAuth mAuth;
    int kurum_id_onay = 0;
    FirebaseUser muser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        uyeOl = (Button) findViewById(R.id.button);
        giris = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView2);
        Isim = (EditText) findViewById(R.id.isim);
        Soyisim = (EditText) findViewById(R.id.soyisim);
        Kurumid = (EditText) findViewById(R.id.KURUMID);
        Email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        Password = (EditText) findViewById(R.id.editTextNumberPassword);
        String name = Isim.getText().toString();
        String surname = Soyisim.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String kurumid = Kurumid.getText().toString();
        sp = this.getSharedPreferences("kullanici_bilgi", MODE_PRIVATE);


        progressDialog = new ProgressDialog(this);
        try {
            outputStream1 = openFileOutput("email.txt", Context.MODE_APPEND);//MODE_PRIVATE ilk önce
            outputStream2 = openFileOutput("password.txt", Context.MODE_APPEND);//MODE_PRIVATE ilk önce
            outputStream3 = openFileOutput("uid.txt", Context.MODE_APPEND);
            outputStream4 = openFileOutput("kurumid.txt", Context.MODE_APPEND);
            outputStream5 = openFileOutput("isim.txt", Context.MODE_APPEND);
            outputStream6 = openFileOutput("soyisim.txt", Context.MODE_APPEND);
        } catch (Exception e) {
            System.out.println(e);
        }

        uyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
                // Toast.makeText(MainActivity2.this, muser.getUid().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, MainActivity.class));
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////fonksiyonlar/////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void createUser() {
        progressDialog.show();

        String name = Isim.getText().toString();
        String surname = Soyisim.getText().toString();
        String kurumid = Kurumid.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            progressDialog.cancel();
            Email.setError("email cannot be empty");
            Email.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            progressDialog.cancel();
            Password.setError("password cannot be empty");
            Password.requestFocus();
        } else if (TextUtils.isEmpty(name)) {
            progressDialog.cancel();
            Isim.setError("name cannot be empty");
            Isim.requestFocus();
        } else if (TextUtils.isEmpty(surname)) {
            progressDialog.cancel();
            Soyisim.setError("surname cannot be empty");
            Soyisim.requestFocus();
        } else if (TextUtils.isEmpty(kurumid)) {
            progressDialog.cancel();
            Kurumid.setError("kurumid cannot be empty");
            Kurumid.requestFocus();
        } else {
            firebaseFirestore.collection("kurumlar")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {
                                if (snapshot.getId().toString().equals(Kurumid.getText().toString())) {
                                    i = 1;
                                }

                            }
                            if (i == 0) {
                                Kurumid.setError("kurum id yanlış");
                                Kurumid.requestFocus();
                                progressDialog.cancel();
                            } else if (i == 1) {
                                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            muser = mAuth.getCurrentUser();
                                            if (muser != null) {
                                                mUser = new UserModel(name, surname, email, password, muser.getUid(), kurumid, "0","https://firebasestorage.googleapis.com/v0/b/yurtapplication.appspot.com/o/ntitled-1.png?alt=media&token=de72a804-9bac-4b56-b4eb-68453905867c","0","0");
                                                textView.setText(muser.getUid());
                                                firebaseFirestore.collection(kurumid).document(muser.getUid())
                                                        .set(mUser)
                                                        .addOnCompleteListener(MainActivity2.this, new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    editor = sp.edit();
                                                                    editor.putString("email", email);
                                                                    editor.putString("password", password);
                                                                    editor.putString("name", name);
                                                                    editor.putString("surname", surname);
                                                                    editor.putString("uid", muser.getUid().toString());
                                                                    editor.putString("kurumid", kurumid);
                                                                    editor.apply();
                                                                    Soyisimyazma();
                                                                    emailYazma();
                                                                    uidyazma();
                                                                    isimyazma();
                                                                    kurumidYazma();
                                                                    Toast.makeText(MainActivity2.this,
                                                                            "registired is succesfully",
                                                                            Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(MainActivity2.this,
                                                                            "registired is unsuccesfully",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                            progressDialog.cancel();
                                            // passwordYazma();
                                        } else {
                                            progressDialog.cancel();
                                            Toast.makeText(MainActivity2.this,
                                                    "registiration Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(MainActivity2.this,
                                    "registiration Error" , Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }

    private void emailYazma() {
        try {

            outputStream1.write(Email.getText().toString().getBytes());


            outputStream1.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void passwordYazma() {
        try {
            outputStream2.write(Password.getText().toString().getBytes());
            outputStream2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uidyazma() {
        try {
            outputStream3.write(textView.getText().toString().getBytes());
            outputStream3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void kurumidYazma() {
        try {
            outputStream4.write(Kurumid.getText().toString().getBytes());
            outputStream4.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void isimyazma() {
        try {
            outputStream5.write(Isim.getText().toString().getBytes());
            outputStream5.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Soyisimyazma() {
        try {
            outputStream6.write(Soyisim.getText().toString().getBytes());
            outputStream6.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}