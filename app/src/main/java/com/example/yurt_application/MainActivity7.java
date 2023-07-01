package com.example.yurt_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity7 extends AppCompatActivity {
    private SharedPreferences sp;
    String secilenKat, secilenOda, kullaniciSayisi, k_id;
    TextView title, text_bos;
    ListView listView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Dialog dialog;
    private odaya_ogrenci_ekle adapter;
    ImageButton add;
    private SharedPreferences.Editor editor;
    ArrayList<String> isimler, soyisimler, emailler, ppler, idler;

    @SuppressLint({"MissingInflatedId", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        listView=findViewById(R.id.listee);
        isimler = new ArrayList<>();
        soyisimler = new ArrayList<>();
        emailler = new ArrayList<>();
        ppler = new ArrayList<>();
        idler = new ArrayList<>();
        adapter = new odaya_ogrenci_ekle(isimler, soyisimler, emailler, ppler, idler, k_id, this, MainActivity7.this, kullaniciSayisi);
        title = findViewById(R.id.title);
        text_bos = findViewById(R.id.textView13);
        add = findViewById(R.id.add_button);
        sp = this.getSharedPreferences("kullanici_bilgi", MODE_PRIVATE);
        editor = sp.edit();
        secilenKat = sp.getString("secilenkat", null);
        k_id = sp.getString("kurumid", null);
        Toast.makeText(this, k_id, Toast.LENGTH_SHORT).show();
        secilenOda = sp.getString("secilenOda", null);
        kullaniciSayisi = sp.getString("kullaniciSayisi", null);
        myRef.child(k_id).child("oturumDuzeni").child(sp.getString("secilenkat", null))
                .child(sp.getString("secilenOda", null)).child("kullaniciSayisi").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                if (task.getResult().getValue() != null) {
                                    if (task.getResult().getValue().toString().equals("0")) {
                                        text_bos.setVisibility(View.VISIBLE);
                                    } else {
                                        text_bos.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        tum_kullanici();
        if (kullaniciSayisi.equals("0")) {
            text_bos.setVisibility(View.VISIBLE);
        } else {
            text_bos.setVisibility(View.INVISIBLE);
        }
        if (secilenKat != null && secilenOda != null) {
            title.setText(secilenKat.concat("   ").concat(secilenOda));
        }

        dialog = new Dialog(MainActivity7.this);
        dialog.setContentView(R.layout.list_view_tum_ogrenci);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        ListView listview1 = dialog.findViewById(R.id.listview1);
        listview1.setAdapter(adapter);
        ImageButton imageButton = dialog.findViewById(R.id.imageButton6);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_bos.setVisibility(View.INVISIBLE);
                dialog.show();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                MainActivity7.this.recreate();
            }
        });

    }

    private void tum_kullanici() {
        firebaseFirestore.collection(k_id)
                .whereEqualTo("onay", "1")
                .whereEqualTo("kat", "0")
                .whereEqualTo("oda", "0")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshotList) {
                            firebaseFirestore.collection(k_id).document(snapshot.getId().toString())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            if (documentSnapshot.exists()) {
                                                isimler.add((String) documentSnapshot.getData().get("name"));
                                                soyisimler.add((String) documentSnapshot.getData().get("surname"));
                                                emailler.add((String) documentSnapshot.getData().get("email"));
                                                ppler.add((String) documentSnapshot.getData().get("imageUrl"));
                                                idler.add((String) documentSnapshot.getData().get("id"));
                                            } else {
                                                Toast.makeText(MainActivity7.this, "not exist", Toast.LENGTH_SHORT).show();
                                            }
                                            adapter.notifyDataSetChanged();


                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity7.this, "error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity7.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}