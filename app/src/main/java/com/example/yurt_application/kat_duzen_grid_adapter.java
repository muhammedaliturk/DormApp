package com.example.yurt_application;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
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

public class kat_duzen_grid_adapter extends ArrayAdapter<String> {
    private ArrayList<String> katlar;

    private ArrayList<String> oda_sayisi;
    private Context tum_context;
    private TextView katNo;
    private TextView odaSayisi;
    private LinearLayout lineer;
    private String k_id;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Activity kurum_ayar;
    private String secilenKat_activity;
    private GridView gridView;
    private kat_duzen_grid_adapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public kat_duzen_grid_adapter(ArrayList<String> katlar, ArrayList<String> oda_sayisi, LinearLayout lineer,
                                  String k_id, GridView gridView, kat_duzen_grid_adapter adapter, Activity kurum_ayar,
                                  String secilenKat_activity, Context tum_context) {
        super(tum_context, R.layout.kat_duzen_gosterim_model, katlar);
        this.katlar = katlar;
        this.lineer = lineer;
        this.k_id = k_id;
        this.secilenKat_activity = secilenKat_activity;
        this.kurum_ayar = kurum_ayar;
        this.adapter = adapter;
        this.gridView = gridView;
        this.oda_sayisi = oda_sayisi;
        this.tum_context = tum_context;

    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(tum_context).inflate(R.layout.kat_duzen_gosterim_model, null);
        if (view != null) {
            sp = kurum_ayar.getSharedPreferences("kullanici_bilgi", MODE_PRIVATE);
            editor = sp.edit();

            katNo = view.findViewById(R.id.textView37);
            lineer = view.findViewById(R.id.lineer);
            odaSayisi = view.findViewById(R.id.textView36);
            katNo.setText(katlar.get(position));
            odaSayisi.setText(oda_sayisi.get(position).concat(" oda var"));

            lineer.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {
                    editor.putString("secilenkat", katlar.get(position).toString());
                    editor.putString("odasayisi", oda_sayisi.get(position).toString());
                    for (int i=1;i<=Integer.parseInt(oda_sayisi.get(position).toString());i++){
                        String oda="oda ".concat(Integer.toString(i));
                        myRef.child(k_id).child("oturumDuzeni").child(katlar.get(position).toString()).child("odalar").
                                child("oda ".concat(Integer.toString(i))).child("kullaniciSayisi").get()
                                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().exists()) {
                                                if (task.getResult().getValue() != null) {
                                                editor.putString(oda,task.getResult().getValue().toString());

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

                    editor.apply();
                    Intent myIntent = new Intent(tum_context, MainActivity6.class);
                    tum_context.startActivity(myIntent);
                }
            });
        }
        return view;
    }
}
