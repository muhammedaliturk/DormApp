package com.example.yurt_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yurt_application.databinding.ActivityMain6Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity6 extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ActivityMain6Binding binding;
    String secilenKat, k_id, odaSayisi;
    TextView kat;
    ArrayList<String> odalar, kullanici_sayisi;
    GridView gridView;
    ImageButton imageButton;
    private oda_duzen_grid_adapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain6Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        odalar = new ArrayList<>();
        kullanici_sayisi = new ArrayList<>();
        LinearLayout lineer = findViewById(R.id.lineer);
        sp = this.getSharedPreferences("kullanici_bilgi", MODE_PRIVATE);
        editor = sp.edit();
        secilenKat = sp.getString("secilenkat", null);
        odaSayisi = sp.getString("odasayisi", null);
        k_id = sp.getString("kurumid", null);
        binding.title.setText(secilenKat);
        Toast.makeText(this, k_id, Toast.LENGTH_SHORT).show();
        adapter = new oda_duzen_grid_adapter(odalar, kullanici_sayisi, this, lineer, k_id, MainActivity6.this);
        binding.gridview.setAdapter(adapter);
        for (int i = 1; i <= Integer.parseInt(odaSayisi); i++) {
            odalar.add("oda ".concat(Integer.toString(i)));
            //kullanici_sayisi.add(sp.getString("oda ".concat(Integer.toString(i)),null));
            myRef.child(k_id).child("oturumDuzeni").child(secilenKat).child("odalar")
                    .child("oda ".concat(Integer.toString(i)))
                    .child("kullaniciSayisi").get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    if (task.getResult().getValue() != null) {
                                       kullanici_sayisi.add(task.getResult().getValue().toString());
                                       // adapter.notifyDataSetChanged();
                                        System.out.println(kullanici_sayisi);
                                        //Toast.makeText(MainActivity6.this, " as" + task.getResult().getValue().toString(), Toast.LENGTH_SHORT).show();

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

        binding.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kullanici_sayisi.clear();
                odalar.clear();
                MainActivity6.this.finish();
            }
        });


    }
}