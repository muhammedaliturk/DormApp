package com.example.yurt_application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

public class onaysiz_adapter extends ArrayAdapter<String> {
    private ArrayList<String> isimler;
    private ArrayList<String> soyisimler;
    private ArrayList<String> emailler;
    private ArrayList<String> ppler;
    private ArrayList<String> idler;
    private Context context;
    private String kurumid;
    private Activity activity;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ;

    private TextView isimSoyisim, email, kullanici_id, button;
    private ImageView profilPicture;

    public onaysiz_adapter(ArrayList<String> isimler, ArrayList<String> soyisimler, ArrayList<String> emailler, ArrayList<String> ppler, ArrayList<String> idler, TextView button, String kurumid, Context context, Activity activity) {
        super(context, R.layout.onaysiz_kullanici, isimler);
        this.isimler = isimler;
        this.soyisimler = soyisimler;
        this.emailler = emailler;
        this.ppler = ppler;
        this.activity=activity;
        this.idler = idler;
        this.context = context;
        this.button = button;
        this.kurumid = kurumid;
    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.onaysiz_kullanici, null);
        if (view != null) {
            kullanici_id = view.findViewById(R.id.textView300);
            isimSoyisim = view.findViewById(R.id.textView27);
            email = view.findViewById(R.id.textView28);
            button = view.findViewById(R.id.textView31);
            profilPicture = view.findViewById(R.id.imageView4);
            isimSoyisim.setText(isimler.get(position) + " " + soyisimler.get(position));
            kullanici_id.setText(idler.get(position));
            email.setText(emailler.get(position));
            Picasso.get().load(ppler.get(position)).into(profilPicture);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firebaseFirestore.collection(kurumid).document(idler.get(position))
                            .update("onay", "1");
                    activity.recreate();

                }
            });

        }
        return view;
    }
}
