package com.example.yurt_application;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity5 extends AppCompatActivity {
    ImageButton menu_button;
    FirebaseFirestore firebaseFirestore;
    TextView text3, text4;
    FirebaseAuth mAuth;

    ListView listview;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    FirebaseUser muser;
    String metin, k_id;
    int j = 10;
    private onaysiz_adapter adapter;
    String[] user;
    ArrayList<String> isimler, soyisimler, emailler, ppler, idler;
    TextView button;
    int i = 0;
    int k = 0;


    FileInputStream inputStream1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        text3 = (TextView) findViewById(R.id.textView6);
        text4 = (TextView) findViewById(R.id.textView7);
        listview = (ListView) findViewById(R.id.liste);
        menu_button = (ImageButton) findViewById(R.id.menu_button);
        try {
            inputStream1 = openFileInput("kurumid.txt");
        } catch (Exception e) {
            System.out.println(e);
        }

        sp = this.getSharedPreferences("kullanici_bilgi", MODE_PRIVATE);
        editor = sp.edit();
        k_id = sp.getString("kurumid", null);
        if (TextUtils.isEmpty(k_id)) {
            kurum_id_okuma();
        } else {
            text3.setText(sp.getString("kurumid", ""));
            Toast.makeText(this, k_id, Toast.LENGTH_SHORT).show();
        }


        isimler = new ArrayList<>();
        soyisimler = new ArrayList<>();
        emailler = new ArrayList<>();
        ppler = new ArrayList<>();
        idler = new ArrayList<>();
        adapter = new onaysiz_adapter(isimler, soyisimler, emailler, ppler, idler, button, k_id, this,MainActivity5.this);
        listview.setAdapter(adapter);
        user = new String[j];
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        onaylanmamis_kullanici();
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity5.this.finish();
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////fonksiyon////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void kurum_id_okuma() {
        int a = 65;
        text3.setText(Character.toString((char) a));

        int x;
        String okunanKurumid = "";
        try {
            // inputStream=openFileInput("dosyam1.txt"); //tıklandığında bidaha görünsün istersek buraya alınmalıdır.
            while ((x = inputStream1.read()) != -1) {
                okunanKurumid += Character.toString((char) x);
            }
            inputStream1.close();

        } catch (Exception e) {

        }

        text3.setText(okunanKurumid);
    }

    private void onaylanmamis_kullanici() {
        firebaseFirestore.collection(text3.getText().toString())
                .whereEqualTo("onay", "0")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        System.out.println(4);
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshotList) {
                            System.out.println(3);
                            firebaseFirestore.collection(text3.getText().toString()).document(snapshot.getId().toString())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            //user[i] = documentSnapshot.getString("email");
                                            //i += 1;
                                            // unapproved_user.add("1");
                                            if (documentSnapshot.exists()) {
                                                isimler.add((String) documentSnapshot.getData().get("name"));
                                                soyisimler.add((String) documentSnapshot.getData().get("surname"));
                                                emailler.add((String) documentSnapshot.getData().get("email"));
                                                ppler.add((String) documentSnapshot.getData().get("imageUrl"));
                                                idler.add((String) documentSnapshot.getData().get("id"));
                                            } else {
                                                Toast.makeText(MainActivity5.this, "not exist", Toast.LENGTH_SHORT).show();
                                            }
                                            adapter.notifyDataSetChanged();


                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity5.this, "error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity5.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}