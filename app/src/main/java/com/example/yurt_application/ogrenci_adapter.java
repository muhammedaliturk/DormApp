package com.example.yurt_application;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ogrenci_adapter extends ArrayAdapter<String> {
    private ArrayList<String> tum_isimler;
    private ArrayList<String> tum_soyisimler;
    private ArrayList<String> tum_emailler;
    private ArrayList<String> tum_ppler;
    private ArrayList<String> tum_idler;
    private Context tum_context;
    private String tum_kurumid;
    private TextView isimSoyisim, email, kullanici_id;
    private ImageView profilPicture;


    public ogrenci_adapter(ArrayList<String> tum_isimler, ArrayList<String> tum_soyisimler,
                           ArrayList<String> tum_emailler,
                           ArrayList<String> tum_ppler, ArrayList<String> tum_idler,
                           String tum_kurumid,Context tum_context) {
        super(tum_context, R.layout.ogrenci_liste, tum_isimler);
        this.tum_isimler = tum_isimler;
        this.tum_soyisimler = tum_soyisimler;
        this.tum_emailler = tum_emailler;
        this.tum_ppler = tum_ppler;
        this.tum_idler = tum_idler;
        this.tum_context = tum_context;
        this.tum_kurumid = tum_kurumid;

    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(tum_context).inflate(R.layout.ogrenci_liste, null);
        if (view != null) {

            kullanici_id = view.findViewById(R.id.textView310);
            isimSoyisim = view.findViewById(R.id.textView270);
            email = view.findViewById(R.id.textView280);
            profilPicture = view.findViewById(R.id.imageView40);
            Picasso.get().load(tum_ppler.get(position)).into(profilPicture);
            isimSoyisim.setText(tum_isimler.get(position) + " " + tum_soyisimler.get(position));
            kullanici_id.setText(tum_idler.get(position));
            email.setText(tum_emailler.get(position));


        }
        return view;
    }
}
