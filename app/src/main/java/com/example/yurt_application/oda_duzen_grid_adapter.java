package com.example.yurt_application;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class oda_duzen_grid_adapter extends ArrayAdapter<String> {

    private ArrayList<String> odalar;
    private ArrayList<String> kullanici_sayisi;
    private Context tum_context;
    private TextView odaNo;
    Dialog dialog;
    private TextView kullaniciSayisi;
    private LinearLayout lineer;
    private String k_id;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Activity activity;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public oda_duzen_grid_adapter(ArrayList<String> odalar, ArrayList<String> kullanici_sayisi, Context tum_context,
                                  LinearLayout lineer, String k_id, Activity activity) {
        super(tum_context, R.layout.kat_duzen_gosterim_model, odalar);
        this.odalar = odalar;
        this.kullanici_sayisi = kullanici_sayisi;
        this.lineer = lineer;
        this.k_id = k_id;
        this.tum_context = tum_context;
        this.activity = activity;


    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(tum_context).inflate(R.layout.kat_duzen_gosterim_model, null);
        if (view != null) {
            sp = activity.getSharedPreferences("kullanici_bilgi", MODE_PRIVATE);
            editor = sp.edit();
            odaNo = view.findViewById(R.id.textView37);
            lineer = view.findViewById(R.id.lineer);
            kullaniciSayisi = view.findViewById(R.id.textView36);
            odaNo.setText(odalar.get(position));
            System.out.println(kullanici_sayisi);
            myRef.child(k_id).child("oturumDuzeni").child(sp.getString("secilenkat", null))
                    .child(odalar.get(position)).child("kullaniciSayisi").get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    if (task.getResult().getValue() != null) {
                                        kullanici_sayisi.add(task.getResult().getValue().toString());
                                        System.out.println(kullanici_sayisi);
                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
          //  kullaniciSayisi.setText(kullanici_sayisi.get(position).concat(" kullanıcı var"));
            lineer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putString("kullaniciSayisi", kullanici_sayisi.get(position).toString());
                    editor.putString("secilenOda",odalar.get(position).toString());
                    editor.apply();
                    Intent myIntent = new Intent(tum_context, MainActivity7.class);
                    tum_context.startActivity(myIntent);
                }
            });
        }
        return view;
    }
}
