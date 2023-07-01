package com.example.yurt_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Objects;

public class kurum_ayar extends AppCompatActivity {
    ImageView ogrenci_liste, makinalar, oturum, katOda;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Dialog dialog, oturum_duzen, kat_oda_düzen_gosterim;
    String k_id, secilenKat;
    ArrayList<String> kat_sayisi_adapter;
    ImageButton menu_button;
    FirebaseFirestore firebaseFirestore;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    ArrayAdapter<String> adapter_oturum;
    private ogrenci_adapter adapter;
    private kat_duzen_grid_adapter kat_duzen_adapter;
    ArrayList<String> isimler, soyisimler, emailler, ppler, idler, katlar, odalar, oda_sayisii;

    @SuppressLint({"MissingInflatedId", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurum_ayar);
        kat_sayisi_adapter = new ArrayList<>();
        isimler = new ArrayList<>();
        soyisimler = new ArrayList<>();
        emailler = new ArrayList<>();
        ppler = new ArrayList<>();
        idler = new ArrayList<>();
        adapter = new ogrenci_adapter(isimler, soyisimler, emailler, ppler, idler, k_id, this);
        katlar = new ArrayList<>();
        odalar = new ArrayList<>();
        oda_sayisii = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ogrenci_liste = findViewById(R.id.imageView5);
        oturum = findViewById(R.id.imageView7);
        katOda = findViewById(R.id.imageView8);
        menu_button = (ImageButton) findViewById(R.id.menu_button);
        makinalar = (ImageView) findViewById(R.id.imageView6);
        sp = this.getSharedPreferences("kullanici_bilgi", MODE_PRIVATE);
        editor = sp.edit();
        k_id = sp.getString("kurumid", null);
        tum_kullanici();
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        dialog = new Dialog(kurum_ayar.this);
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
        //TextView title_pop = dialog.findViewById(R.id.title_popup);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        oturum_duzen = new Dialog(kurum_ayar.this);
        oturum_duzen.setContentView(R.layout.oturum_duzeni);
        oturum_duzen.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        oturum_duzen.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        oturum_duzen.setCancelable(true);
        oturum_duzen.setCanceledOnTouchOutside(true);
        oturum_duzen.getWindow().setGravity(Gravity.BOTTOM);
        oturum_duzen.getWindow().getAttributes().windowAnimations = R.style.animation;
        EditText kat_sayisi = oturum_duzen.findViewById(R.id.editTextNumber);
        EditText oda_sayisi = oturum_duzen.findViewById(R.id.editTextNumber2);
        ImageButton imageButton_oturum_duzen = oturum_duzen.findViewById(R.id.imageButton10);
        Button button_kayit = oturum_duzen.findViewById(R.id.button6);
        Spinner spinner = oturum_duzen.findViewById(R.id.spinner2);
        adapter_oturum = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kat_sayisi_adapter);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        kat_oda_düzen_gosterim = new Dialog(kurum_ayar.this);
        kat_oda_düzen_gosterim.setContentView(R.layout.kat_duzen_gosterim);
        kat_oda_düzen_gosterim.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        kat_oda_düzen_gosterim.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        kat_oda_düzen_gosterim.setCancelable(true);
        kat_oda_düzen_gosterim.setCanceledOnTouchOutside(true);
        kat_oda_düzen_gosterim.getWindow().setGravity(Gravity.BOTTOM);
        kat_oda_düzen_gosterim.getWindow().getAttributes().windowAnimations = R.style.animation;
        GridView gridView = kat_oda_düzen_gosterim.findViewById(R.id.gridview);
        LinearLayout lineer = kat_oda_düzen_gosterim.findViewById(R.id.lineer);
        ImageButton downbutton = kat_oda_düzen_gosterim.findViewById(R.id.imageButton16);
        ImageButton backbutton = kat_oda_düzen_gosterim.findViewById(R.id.imageButton60);
        kat_duzen_adapter = new kat_duzen_grid_adapter(katlar, oda_sayisii, lineer, k_id, gridView, kat_duzen_adapter,kurum_ayar.this, MainActivity6.ACTIVITY_SERVICE,this);
        gridView.setAdapter(kat_duzen_adapter);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        myRef.child(k_id).child("oturumDuzeni").child("katsayisi").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        if (task.getResult().getValue() != null) {
                            kat_sayisi.setText(task.getResult().getValue().toString());
                            kat_sayisi.requestFocus();
                            for (int i = 1; i <= Integer.parseInt(task.getResult().getValue().toString()); i++) {
                                katlar.add("kat ".concat(Integer.toString(i)));
                                myRef.child(k_id).child("oturumDuzeni").child("kat ".concat(Integer.toString(i))).child("odasayisi").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().exists()) {
                                                if (task.getResult().getValue() != null) {
                                                    oda_sayisii.add(task.getResult().getValue().toString());
                                                }

                                            }

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
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
        if (!sp.getString("secilenkat",null).equals("0")) {
            Toast.makeText(this, sp.getString("secilenkat",null), Toast.LENGTH_SHORT).show();
        }

       /* firebaseFirestore.collection(k_id).document("oturumDuzeni")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                if (!documentSnapshot.getData().isEmpty()) {
                                    kat_sayisi.setText(documentSnapshot.getData().get("katsayisi").toString());
                                    kat_sayisi.requestFocus();

                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });*/
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kurum_ayar.this.finish();
            }
        });
        katOda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kat_oda_düzen_gosterim.show();
            }
        });

        ogrenci_liste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        downbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kat_oda_düzen_gosterim.dismiss();

            }
        });
        oturum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oturum_duzen.show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (kat_sayisi.getText().toString().isEmpty()) {
                    kat_sayisi.requestFocus();
                    kat_sayisi.setError("boş olamaz");
                } else {
                    secilenKat = adapterView.getItemAtPosition(i).toString();
                    myRef.child(k_id).child("oturumDuzeni").child(secilenKat).child("odasayisi").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    if (task.getResult().getValue() != null) {
                                        oda_sayisi.setText(task.getResult().getValue().toString());
                                    } else oda_sayisi.setText("0");
                                } else oda_sayisi.setText("0");

                            } else oda_sayisi.setText("0");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    /*firebaseFirestore.collection(k_id).document("oturumDuzeni")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (!documentSnapshot.getData().isEmpty()) {
                                            if (documentSnapshot.getData().get(secilenKat) != null) {
                                                oda_sayisi.setText(documentSnapshot.getData().get(secilenKat).toString());

                                            }

                                        }

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });*/

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        oda_sayisi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (kat_sayisi.getText().toString().isEmpty()) {
                        kat_sayisi.requestFocus();
                        kat_sayisi.setError("boş olamaz");
                    }
                    //kat_sayisi_adapter.add("1.kat");
                    spinner.setAdapter(adapter_oturum);
                }
            }
        });
        kat_sayisi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (kat_sayisi.getText().toString().isEmpty()) {
                        kat_sayisi.setError("Boş Olamaz !");
                        kat_sayisi.requestFocus();
                    } else {
                        Toast.makeText(kurum_ayar.this, kat_sayisi.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (Integer.parseInt(kat_sayisi.getText().toString()) != 1) {
                            for (int i = 1; i <= Integer.parseInt(kat_sayisi.getText().toString()); i++) {
                                String s = "kat ".concat(Integer.toString(i));
                                kat_sayisi_adapter.add(s);
                            }
                        }

                        adapter_oturum.notifyDataSetChanged();
                    }
                } else Toast.makeText(kurum_ayar.this, "21", Toast.LENGTH_SHORT).show();
            }
        });
        button_kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oda_sayisi.getText().toString().isEmpty()) {
                    oda_sayisi.setError("Boş Olamaz !");
                    oda_sayisi.requestFocus();
                } else {

                    //myRef.child(k_id).child("oturumDuzeni").getRef();
                    myRef.child(k_id).child("oturumDuzeni").child("katsayisi").setValue(kat_sayisi.getText().toString());
                    myRef.child(k_id).child("oturumDuzeni").child(secilenKat.toString()).child("odasayisi").setValue(oda_sayisi.getText().toString());
                    for (int i = 1; i <= Integer.parseInt(oda_sayisi.getText().toString()); i++) {
                        myRef.child(k_id).child("oturumDuzeni").child(secilenKat.toString()).child("odalar").child("oda ".concat(Integer.toString(i))).child("kullanicilar").setValue(null);
                        myRef.child(k_id).child("oturumDuzeni").child(secilenKat.toString()).child("odalar").child("oda ".concat(Integer.toString(i))).child("kullaniciSayisi").setValue("0");
                    }
                    firebaseFirestore.collection(k_id).document("oturumDuzeni")
                            .update("katsayisi", kat_sayisi.getText().toString());
                    firebaseFirestore.collection(k_id).document("oturumDuzeni")
                            .update(secilenKat.toString(), oda_sayisi.getText().toString());
                }
            }
        });

        imageButton_oturum_duzen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oturum_duzen.cancel();
            }
        });
    }

    private void tum_kullanici() {
        firebaseFirestore.collection(k_id)
                .whereEqualTo("onay", "1")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshotList) {
                            System.out.println(3);
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
                                                Toast.makeText(kurum_ayar.this, "not exist", Toast.LENGTH_SHORT).show();
                                            }
                                            adapter.notifyDataSetChanged();


                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(kurum_ayar.this, "error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(kurum_ayar.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}