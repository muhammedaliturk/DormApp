package com.example.yurt_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ActionTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.interfaces.TouchListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.grpc.okhttp.internal.Util;

public class MainActivity3 extends AppCompatActivity {
    TextView title, isim_text;
    Button add_button;
    String user_id;
    LinearLayout popuplayout;
    ImageButton menuButton, notificationButton;
    String value, onay_durum;
    String isim, kurumid;
    private SharedPreferences sp, sp_image;
    private SharedPreferences.Editor editor, editor_image;
    Dialog dialog, menuDialog, isim_Dialog, onaysiz_kullanici_dialog;
    FileInputStream inputStream1;
    ImageView profil_resim;
    CardView cardView;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser muser;
    Uri filePath;

    @SuppressLint({"MissingInflatedId", "SetTextI18n", "UseCompatLoadingForDrawables", "RtlHardcoded"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        firebaseFirestore = FirebaseFirestore.getInstance();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////pop-up-tanımlama///////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        dialog = new Dialog(MainActivity3.this);
        dialog.setContentView(R.layout.mainpopup);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        ImageView imageVieW = dialog.findViewById(R.id.imageView2);
        ImageButton imageButton = dialog.findViewById(R.id.imageButton3);
        TextView title_pop = dialog.findViewById(R.id.title_popup);
        popuplayout = (LinearLayout) findViewById(R.id.ll);
        /////////////////////////////////////////////////////////////////////////////////////////////
        menuDialog = new Dialog(MainActivity3.this);
        menuDialog.setContentView(R.layout.menulayout);
        menuDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        menuDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        menuDialog.setCancelable(true);
        menuDialog.setCanceledOnTouchOutside(true);
        menuDialog.getWindow().setGravity(Gravity.LEFT);
        menuDialog.getWindow().getAttributes().windowAnimations = R.style.menu_animation;
        TextView isim_tv = menuDialog.findViewById(R.id.textView12);
        TextView kurum_ayarlar = menuDialog.findViewById(R.id.textView30);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        onaysiz_kullanici_dialog = new Dialog(MainActivity3.this);
        onaysiz_kullanici_dialog.setContentView(R.layout.onaysiz_kullanici_layout);
        onaysiz_kullanici_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        onaysiz_kullanici_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        onaysiz_kullanici_dialog.setCancelable(false);
        onaysiz_kullanici_dialog.setCanceledOnTouchOutside(false);
        onaysiz_kullanici_dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        isim_Dialog = new Dialog(MainActivity3.this);
        isim_Dialog.setContentView(R.layout.isim_pupup);
        isim_Dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        isim_Dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        isim_Dialog.setCancelable(true);
        isim_Dialog.setCanceledOnTouchOutside(true);
        isim_Dialog.getWindow().setGravity(Gravity.BOTTOM);
        isim_Dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        TextView kullanici_isim = isim_Dialog.findViewById(R.id.textView18);
        TextView kullanici_soy_isim = isim_Dialog.findViewById(R.id.textView19);
        TextView kullanici_email = isim_Dialog.findViewById(R.id.textView20);
        TextView kullanici_kurum = isim_Dialog.findViewById(R.id.textView21);
        ImageButton imageButton1 = isim_Dialog.findViewById(R.id.imageButton4);
        TextView kayit_button = isim_Dialog.findViewById(R.id.textView25);
        ImageButton imageButton2 = isim_Dialog.findViewById(R.id.imageButton5);
        TextView resim_ekle_button = isim_Dialog.findViewById(R.id.textView26);
        profil_resim = isim_Dialog.findViewById(R.id.imageView3);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////ana-ekran-nesneler-tanımlama////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        menuButton = (ImageButton) findViewById(R.id.menu_button);
        notificationButton = (ImageButton) findViewById(R.id.notification_button);
        isim_text = (TextView) findViewById(androidx.core.R.id.title);
        title = (TextView) findViewById(R.id.textView3);
        add_button = (Button) findViewById(R.id.button4);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swp);//screen refresh
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////firebase-tanımlama-tanımlama////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////resim-slider-tanımlama////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        cardView = (CardView) findViewById(R.id.cv);
        ImageSlider imageSlider = findViewById(R.id.slider);
        ArrayList<SlideModel> sliderModels = new ArrayList<>();
        ArrayList<SlideModel> sliderModels_popup = new ArrayList<>();
        sliderModels.add(new SlideModel(R.drawable.logo, ScaleTypes.FIT));
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        sp = getSharedPreferences("kullanici_bilgi", MODE_PRIVATE);
        editor = sp.edit();
        sp_image = getSharedPreferences("image", MODE_PRIVATE);
        editor_image = sp_image.edit();
        try {
            inputStream1 = openFileInput("isim.txt");
        } catch (Exception e) {
            System.out.println(e);
        }
        //isim_okuma();
        onay_durum = sp.getString("onay", null);
        if (onay_durum.equals("0")) {
            onaysiz_kullanici_dialog.show();
        } else if (onay_durum.equals("1")) {
            onaysiz_kullanici_dialog.dismiss();
        }
        kurumid = sp.getString("kurumid", null);
        isim = sp.getString("name", null);
        user_id = sp.getString("userid", null);
        editor.putString("secilenkat", "0");
        editor.commit();
        if (TextUtils.isEmpty(isim)) {
            // Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            isim_okuma();
        } else {
            isim_text.setText("Merhaba " + isim);
            // Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
        }
        value = getIntent().getStringExtra("key");
        if (value != null) {

            if (value.equals("1")) {

                title.setText("admin");
                add_button.setVisibility(View.VISIBLE);
                kurum_ayarlar.setVisibility(View.VISIBLE);

            } else {
                title.setVisibility(View.INVISIBLE);
                title.setText("kullanıcı");
                notificationButton.setVisibility(View.INVISIBLE);
                kurum_ayarlar.setVisibility(View.INVISIBLE);
            }
        }
        kurum_ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                startActivity(new Intent(MainActivity3.this, kurum_ayar.class));
            }
        });
        firebaseFirestore.collection(kurumid).document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                if (Objects.requireNonNull(documentSnapshot.getData()).get("onay").toString().equals("0")) {
                                    onaysiz_kullanici_dialog.show();
                                }
                                Picasso.get().load(documentSnapshot.getData().get("imageUrl").toString()).into(profil_resim);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        firebaseFirestore.collection(kurumid).document("kurumisim")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                kullanici_kurum.setText(document.getData().get("isim").toString());
                            }


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        isim_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isim_Dialog.show();
                menuDialog.dismiss();
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButton2.setVisibility(View.INVISIBLE);
                kayit_button.setVisibility(View.VISIBLE);
                resim_ekle_button.setVisibility(View.VISIBLE);

            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isim_Dialog.dismiss();
                imageButton2.setVisibility(View.VISIBLE);
                kayit_button.setVisibility(View.INVISIBLE);
                resim_ekle_button.setVisibility(View.INVISIBLE);
            }
        });
        resim_ekle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_image();
            }
        });
        kayit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(filePath);
            }
        });
        isim_Dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                imageButton2.setVisibility(View.VISIBLE);
                kayit_button.setVisibility(View.INVISIBLE);
                resim_ekle_button.setVisibility(View.INVISIBLE);

            }
        });
        kullanici_isim.setText(sp.getString("name", null));
        kullanici_soy_isim.setText(sp.getString("surname", null));
        kullanici_email.setText(sp.getString("email", null));
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.show();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////ekran yenileme fonksiyonu///////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<SlideModel> sliderModels = new ArrayList<>();
                        ArrayList<SlideModel> sliderModels_popup = new ArrayList<>();
                        swipeRefreshLayout.setRefreshing(false);
                        sliderModels.add(new SlideModel(R.drawable.logo, ScaleTypes.FIT));
                        FirebaseDatabase.getInstance().getReference().child(kurumid.toString()).child("resimler")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot data : snapshot.getChildren()) {
                                            Picasso.get().load(Objects.requireNonNull(data.child("url").getValue()).toString()).into(imageVieW);
                                            sliderModels_popup.add(new SlideModel(Objects.requireNonNull(data.child("url").getValue()).toString()
                                                    , Objects.requireNonNull(data.child("describtion").getValue()).toString()
                                                    , ScaleTypes.FIT));
                                            sliderModels.add(new SlideModel(Objects.requireNonNull(data.child("url").getValue()).toString()
                                                    , Objects.requireNonNull(data.child("title").getValue()).toString()
                                                    , ScaleTypes.FIT));
                                            imageSlider.setImageList(sliderModels, ScaleTypes.FIT);
                                            imageSlider.setItemClickListener(new ItemClickListener() {
                                                @Override
                                                public void onItemSelected(int i) {
                                                    //Toast.makeText(MainActivity3.this, sliderModels.get(i).getImageUrl().toString(), Toast.LENGTH_SHORT).show();
                                                    if (sliderModels.get(i).getImageUrl() != null) {
                                                        Picasso.get().load(sliderModels.get(i).getImageUrl().toString()).into(imageVieW);
                                                        title_pop.setText(sliderModels.get(i).getTitle().toString() + "\n\n\n " + sliderModels_popup.get(i - 1).getTitle().toString());
                                                        dialog.setCanceledOnTouchOutside(true);
                                                        dialog.show();

                                                    }

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                }, 2000);
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity3.this, resim_ekle.class));
                //select_image();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                imageSlider.startSliding(2000);
            }
        });


        FirebaseDatabase.getInstance().getReference().child(kurumid.toString()).child("resimler")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Picasso.get().load(Objects.requireNonNull(data.child("url").getValue()).toString()).into(imageVieW);
                            sliderModels_popup.add(new SlideModel(Objects.requireNonNull(data.child("url").getValue()).toString()
                                    , Objects.requireNonNull(data.child("describtion").getValue()).toString()
                                    , ScaleTypes.FIT));
                            sliderModels.add(new SlideModel(Objects.requireNonNull(data.child("url").getValue()).toString()
                                    , Objects.requireNonNull(data.child("title").getValue()).toString()
                                    , ScaleTypes.FIT));
                            imageSlider.setImageList(sliderModels, ScaleTypes.FIT);
                            // sliderModels.size()
                            // System.out.println(sliderModels.size());

                            imageSlider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemSelected(int i) {
                                    //Toast.makeText(MainActivity3.this, sliderModels.get(i).getImageUrl().toString(), Toast.LENGTH_SHORT).show();
                                    if (sliderModels.get(i).getImageUrl() != null) {
                                        Picasso.get().load(sliderModels.get(i).getImageUrl().toString()).into(imageVieW);
                                        title_pop.setText(sliderModels.get(i).getTitle().toString() + "\n\n\n " + sliderModels_popup.get(i - 1).getTitle().toString());
                                        dialog.setCanceledOnTouchOutside(true);
                                        imageSlider.stopSliding();
                                        dialog.show();

                                    }

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialog.dismiss();
                imageSlider.startSliding(2000);
            }
        });
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity3.this, MainActivity5.class));
            }
        });


    }


    @SuppressLint("SetTextI18n")
    private void isim_okuma() {


        int a = 65;
        isim_text.setText(Character.toString((char) a));

        int x;
        String okunanIsim = "";
        try {
            // inputStream=openFileInput("dosyam1.txt"); //tıklandığında bidaha görünsün istersek buraya alınmalıdır.
            while ((x = inputStream1.read()) != -1) {
                okunanIsim += Character.toString((char) x);
            }
            inputStream1.close();

        } catch (Exception e) {

        }

        isim_text.setText("merhaba " + okunanIsim);
        // Toast.makeText(MainActivity3.this, okunanIsim, Toast.LENGTH_SHORT).show();
    }

    private void select_image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            profil_resim.setImageURI(filePath);
        }
    }

    private void uploadImage(Uri filePath) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // Defining the child of storageReference
        final StorageReference ref = storageReference.child(user_id);
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri filePath) {
                        urlModel model = new urlModel(filePath.toString());
                        //Toast.makeText(MainActivity3.this, model.getImageUrl().toString(), Toast.LENGTH_SHORT).show();
                        firebaseFirestore.collection(kurumid).document(user_id)
                                .update("imageUrl", model.getImageUrl().toString());



                       /* myRef = database.getReference("message/"+model+"/url");
                        myRef.setValue(model);*/
                        //url_firestore(model, k);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                Toast.makeText(MainActivity3.this, "başarılı", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressDialog.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(MainActivity3.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getFileExtension(Uri filePath) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(filePath));
    }
}