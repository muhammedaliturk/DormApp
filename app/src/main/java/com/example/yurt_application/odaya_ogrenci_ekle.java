package com.example.yurt_application;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class odaya_ogrenci_ekle extends ArrayAdapter<String> {
    private ArrayList<String> tum_isimler;
    private ArrayList<String> tum_soyisimler;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ArrayList<String> tum_emailler;
    private ArrayList<String> tum_ppler;
    private ArrayList<String> tum_idler;
    private Context tum_context;
    private String tum_kurumid, kat, oda,kullaniciSayisi;
    private TextView isimSoyisim, email, kullanici_id, ekle_button;
    private ImageView profilPicture;
    private int k;
    private Activity activity;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public odaya_ogrenci_ekle(ArrayList<String> tum_isimler, ArrayList<String> tum_soyisimler,
                              ArrayList<String> tum_emailler,
                              ArrayList<String> tum_ppler, ArrayList<String> tum_idler,
                              String tum_kurumid, Context tum_context, Activity activity,String kullaniciSayisi) {
        super(tum_context, R.layout.list_view_odaya_ekle, tum_isimler);
        this.tum_isimler = tum_isimler;
        this.tum_soyisimler = tum_soyisimler;
        this.tum_emailler = tum_emailler;
        this.tum_ppler = tum_ppler;
        this.tum_idler = tum_idler;
        this.tum_context = tum_context;
        this.tum_kurumid = tum_kurumid;
        this.activity = activity;
        this.kullaniciSayisi=kullaniciSayisi;

    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(tum_context).inflate(R.layout.list_view_odaya_ekle, null);
        if (view != null) {

            sp = activity.getSharedPreferences("kullanici_bilgi", MODE_PRIVATE);
            editor = sp.edit();
            k=Integer.parseInt(sp.getString("kullaniciSayisi", null));
            //kat=sp.getString("secilenkat",null);
            oda = sp.getString("secilenOda", null);
            tum_kurumid = sp.getString("kurumid", null);
            if (sp.getString("secilenkat", null) != null) {
                if (sp.getString("secilenkat", null).length() > 5) {
                    kat = "";
                    for (int i = 4; i <= sp.getString("secilenkat", null).length(); i++) {
                        kat = kat.concat(String.valueOf(sp.getString("secilenkat", null).charAt(i)));
                    }
                } else {
                    kat = String.valueOf(sp.getString("secilenkat", null).charAt(4));
                }
            }
            if (sp.getString("secilenOda", null) != null) {
                if (sp.getString("secilenOda", null).length() > 5) {
                    oda = "";
                    for (int i = 4; i <= sp.getString("secilenOda", null).length(); i++) {
                        oda = oda.concat(String.valueOf(sp.getString("secilenOda", null).charAt(i)));
                    }
                } else {
                    oda = String.valueOf(sp.getString("secilenOda", null).charAt(4));
                }
            }

            ekle_button = view.findViewById(R.id.textView39);
            kullanici_id = view.findViewById(R.id.textView45);
            isimSoyisim = view.findViewById(R.id.textView43);
            email = view.findViewById(R.id.textView44);
            profilPicture = view.findViewById(R.id.imageView13);
            Picasso.get().load(tum_ppler.get(position)).into(profilPicture);
            isimSoyisim.setText(tum_isimler.get(position) + " \n" + tum_soyisimler.get(position));
            kullanici_id.setText(tum_idler.get(position));
            email.setText(tum_emailler.get(position));

            ekle_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firebaseFirestore.collection(tum_kurumid).document(tum_idler.get(position).toString())
                            .update("kat",kat);
                    firebaseFirestore.collection(tum_kurumid).document(tum_idler.get(position).toString())
                            .update("oda",oda);
                    myRef.child(tum_kurumid).child("oturumDuzeni")
                            .child("kat ".concat(kat)).child("odalar")
                            .child("oda ".concat(oda))
                            .child(String.valueOf(System.currentTimeMillis())).setValue(tum_idler.get(position));
                    myRef.child(tum_kurumid).child("oturumDuzeni")
                            .child("kat ".concat(kat)).child("odalar")
                            .child("oda ".concat(oda))
                            .child("kullaniciSayisi").get()
                                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().exists()) {
                                                    if (task.getResult().getValue() != null) {
                                                        k=Integer.parseInt(task.getResult().getValue().toString());
                                                        myRef.child(tum_kurumid).child("oturumDuzeni")
                                                                .child("kat ".concat(kat)).child("odalar")
                                                                .child("oda ".concat(oda))
                                                                .child("kullaniciSayisi").setValue(Integer.toString(k+1));
                                                    }
                                                }
                                            }
                                        }
                                    });

                    activity.recreate();

                }
            });

        }
        return view;
    }
}
