package com.example.yurt_application;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;
import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 101010;
    Button giris, uyeOl;

    EditText Email, Password, Kurum_id;
    int i = 0;
    ImageButton parmakizi;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    ConstraintLayout constraintLayout;
    TextView email_gizli_text, password_gizli_text, fp, uid_gizli_text, text4, text5, text6, text7;
    FileOutputStream outputStream1, outputStream2, outputStream3, outputStream4, outputStream5, outputStream6, outputStream7;
    FileInputStream inputStream1, inputStream2, inputStream3, inputStream4, inputStream5, inputStream6, inputStream7;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser muser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("admin");
    String value, isim;

    private void init() {
        giris = (Button) findViewById(R.id.button);
        email_gizli_text = (TextView) findViewById(R.id.textView);
        constraintLayout = (ConstraintLayout) findViewById(R.id.login);
        Email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        Kurum_id = (EditText) findViewById(R.id.Kurumid);
        Password = (EditText) findViewById(R.id.editTextNumberPassword);
        uyeOl = (Button) findViewById(R.id.button3);
        password_gizli_text = (TextView) findViewById(R.id.textView2);
        fp = (TextView) findViewById(R.id.forgetPassword);
        parmakizi = (ImageButton) findViewById(R.id.fingerPrint);
        text4 = (TextView) findViewById(R.id.textView4);
        uid_gizli_text = (TextView) findViewById(R.id.textView3);
        text6 = (TextView) findViewById(R.id.textView6);
        text7 = (TextView) findViewById(R.id.textView7);
        text5 = (TextView) findViewById(R.id.textView5);
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();
        parmakizi.setVisibility(View.INVISIBLE);
        muser = mAuth.getCurrentUser();
        sp = this.getSharedPreferences("kullanici_bilgi", MODE_PRIVATE);
        editor = sp.edit();
        firebaseFirestore = FirebaseFirestore.getInstance();

        try {
            outputStream1 = openFileOutput("email.txt", Context.MODE_APPEND);//MODE_PRIVATE ilk önce
            inputStream1 = openFileInput("email.txt");
            outputStream2 = openFileOutput("password.txt", Context.MODE_APPEND);//MODE_PRIVATE ilk önce
            inputStream2 = openFileInput("password.txt");
            outputStream3 = openFileOutput("uid.txt", Context.MODE_APPEND);//MODE_PRIVATE ilk önce
            inputStream3 = openFileInput("uid.txt");
            outputStream4 = openFileOutput("isim.txt", Context.MODE_APPEND);//MODE_PRIVATE ilk önce
            inputStream4 = openFileInput("isim.txt");
            outputStream5 = openFileOutput("kurumid.txt", Context.MODE_APPEND);//MODE_PRIVATE ilk önce
            inputStream5 = openFileInput("kurumid.txt");
            outputStream6 = openFileOutput("soyisim.txt", Context.MODE_APPEND);//MODE_PRIVATE ilk önce
            inputStream6 = openFileInput("soyisim.txt");
        } catch (Exception e) {
            System.out.println(e);
        }
        kurum_id_okuma();
        uidokuma();
        email_okuma();
        isim_okuma();
        password_okuma();
        soyisim_okuma();
        if (!TextUtils.isEmpty(sp.getString("email", null))) {
            Email.setText(sp.getString("email", null));
            email_gizli_text.setText(sp.getString("email", null));
        } else {
            email_okuma();
        }
        if (TextUtils.isEmpty(sp.getString("password", null))) {
            password_okuma();
        } else {
            password_gizli_text.setText(sp.getString("password", null));
        }
        if (TextUtils.isEmpty(sp.getString("uid", null))) {
            uidokuma();
        } else {
            uid_gizli_text.setText(sp.getString("uid", null));
        }
        if (TextUtils.isEmpty(sp.getString("kurumid", null))) {
            kurum_id_okuma();
        } else {
            text6.setText(sp.getString("kurumid", null));
        }
        if (TextUtils.isEmpty(sp.getString("name", null))) {
            isim_okuma();
        } else {
            text5.setText(sp.getString("name", null));
        }

        if (password_gizli_text.getText().toString().equals("")) {
            parmakizi.setVisibility(View.INVISIBLE);
        } else {
            parmakizi.setVisibility(View.INVISIBLE);
        }
        if (!email_gizli_text.getText().toString().equals("")) {
            uyeOl.setVisibility(View.INVISIBLE);
            //parmakizi.setVisibility(View.VISIBLE);
        } else {
            uyeOl.setVisibility(View.VISIBLE);
            //parmakizi.setVisibility(View.INVISIBLE);
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                value = dataSnapshot.getValue(String.class);
                text4.setText(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value

            }
        });


    }

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        isim = getIntent().getStringExtra("isim");

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////parmak izi/////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                if (!email_gizli_text.getText().toString().equals("") && !password_gizli_text.getText().toString().equals("")) {
                    parmakizi.setVisibility(View.VISIBLE);
                }
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                parmakizi.setVisibility(View.INVISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
                Password.requestFocus();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Password.setText(password_gizli_text.getText().toString());

                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                signIn();
                //startActivity(new Intent(MainActivity.this, MainActivity3.class));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biyometrik Giriş")
                .setNegativeButtonText("Şifre kullan")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.

        parmakizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*email_okuma();
                password_okuma();*/
                biometricPrompt.authenticate(promptInfo);
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////parmak izi///////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordSilme();

                startActivity(new Intent(MainActivity.this, MainActivity4.class));
            }
        });


        //  signIn2();
        uyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity2.class));

            }
        });


        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
                //createUser();


            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////// fonksiyonlar//////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*private void signIn2() {

        String email = Email.getText().toString();
        String password = Password.getText().toString();
        if (TextUtils.isEmpty(email)) {
            //Email.setError("email cannot be empty");
            Email.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            // Password.setError("password cannot be empty");
            Password.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "sign-in is succesfully", Toast.LENGTH_SHORT).show();

                        if (muser != null) {
                            if (text3.getText().equals(value)) {
                                intent.putExtra("Key", "1"); //veri gönderiliyor
                                startActivity(intent);

                                startActivity(new Intent(MainActivity.this, MainActivity3.class));
                            } else {
                                intent.putExtra("Key", "0"); //veri gönderiliyor
                                startActivity(intent);
                                startActivity(new Intent(MainActivity.this, MainActivity3.class));
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "sign-in is unsuccesfully" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }*/

    private void signIn() {

        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String kurum_id = Kurum_id.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Email.setError("email boş olamaz");
            Email.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            Password.setError("password boş olamaz");
            Password.requestFocus();
        } else if (TextUtils.isEmpty(kurum_id)) {
            Kurum_id.setError("kurum id boş olamaz");
            Kurum_id.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        muser = mAuth.getCurrentUser();
                        if (muser != null) {
                            firebaseFirestore.collection("kurumlar")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                            for (DocumentSnapshot snapshot : snapshotList) {
                                                if (snapshot.getId().toString().equals(Kurum_id.getText().toString())) {
                                                    Toast.makeText(MainActivity.this, snapshot.getId().toString(), Toast.LENGTH_SHORT).show();
                                                    i = 1;
                                                }
                                            }
                                            if (i == 0) {
                                                Kurum_id.setError("kurum id yanlış");
                                                Kurum_id.requestFocus();
                                            } else if (i == 1) {
                                                firebaseFirestore.collection(Kurum_id.getText().toString()).document(muser.getUid())
                                                        .get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                emailYazma();
                                                                passwordYazma();
                                                                kurum_idyazma();
                                                                uid_gizli_text.setText(muser.getUid().toString());
                                                                uidyazma();
                                                                email_okuma();
                                                                isim_okuma();
                                                                password_okuma();
                                                                kurum_id_okuma();
                                                                uidokuma();
                                                                soyisim_okuma();
                                                                editor.putString("name", documentSnapshot.getString("name"));
                                                                editor.putString("userid", muser.getUid().toString());
                                                                //Toast.makeText(MainActivity.this, documentSnapshot.getString("name"), Toast.LENGTH_SHORT).show();
                                                                editor.putString("surname", documentSnapshot.getString("surname"));
                                                                editor.putString("email", documentSnapshot.getString("email"));
                                                                editor.putString("password", documentSnapshot.getString("password"));
                                                                editor.putString("kurumid", documentSnapshot.getString("kurumid"));
                                                                editor.putString("onay", documentSnapshot.getString("onay"));
                                                                editor.apply();

                                                                if (muser.getUid().equals(value)) {
                                                                    Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                                                                    intent.putExtra("key", "1");
                                                                    Toast.makeText(MainActivity.this, "sign-in is succesfully",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    startActivity(intent);
                                                                    MainActivity.this.finishAffinity();
                                                                    // startActivity(new Intent(MainActivity.this, MainActivity3.class));
                                                                } else {
                                                                    Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                                                                    intent.putExtra("key", "0");//veri gönderiliyor
                                                                    Toast.makeText(MainActivity.this, "sign-in is succesfully",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    startActivity(intent);
                                                                    MainActivity.this.finishAffinity();
                                                                    //startActivity(new Intent(MainActivity.this, MainActivity3.class));
                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this,
                                                    "sign-in is unsuccesfully" + task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }


                    } else {
                        Toast.makeText(MainActivity.this, "sign-in is unsuccesfully" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }

    private void createUser() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Email.setError("email cannot be empty");
            Email.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            Password.setError("password cannot be empty");
            Password.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "User Registered succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Registiration Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////dosya yazma fonksiyonları///////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void emailYazma() {
        if (email_gizli_text.getText().toString().equals("")) {
            editor.putString("email", Email.getText().toString());
            editor.apply();
            try {

                outputStream1.write(Email.getText().toString().getBytes());
                outputStream1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {

                outputStream1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void passwordYazma() {
        if (password_gizli_text.getText().toString().equals("")) {
            editor.putString("password", Password.getText().toString());
            editor.apply();
            try {
                outputStream2.write(Password.getText().toString().getBytes());
                outputStream2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {

                outputStream2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uidyazma() {
        if (uid_gizli_text.getText().toString().equals("")) {
            editor.putString("uid", muser.getUid().toString());
            editor.apply();
            try {
                outputStream3.write(muser.getUid().getBytes());
                outputStream3.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {

                outputStream3.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void kurum_idyazma() {
        if (text6.getText().toString().equals("")) {
            editor.putString("kurumid", Kurum_id.getText().toString());
            editor.apply();
            try {
                outputStream5.write(Kurum_id.getText().toString().getBytes());
                outputStream5.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {

                outputStream5.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////dosya okuma fonksiyonları/////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void kurum_id_okuma() {
        int a = 65;
        text6.setText(Character.toString((char) a));

        int x;
        String okunan_kurum_id = "";
        try {

            while ((x = inputStream5.read()) != -1) {
                okunan_kurum_id += Character.toString((char) x);
            }
            inputStream5.close();

        } catch (Exception e) {

        }

        text6.setText(okunan_kurum_id);
        Kurum_id.setText(okunan_kurum_id);
        editor.putString("kurumid", okunan_kurum_id);
        editor.apply();
    }

    private void isim_okuma() {
        int a = 65;
        text5.setText(Character.toString((char) a));

        int x;
        String okunanisim = "";
        try {
            // inputStream=openFileInput("dosyam1.txt"); //tıklandığında bidaha görünsün istersek buraya alınmalıdır.
            while ((x = inputStream4.read()) != -1) {
                okunanisim += Character.toString((char) x);
            }
            inputStream4.close();

        } catch (Exception e) {

        }

        text5.setText(okunanisim);
        editor.putString("name", okunanisim);
        editor.apply();
    }

    private void email_okuma() {


        int a = 65;
        email_gizli_text.setText(Character.toString((char) a));

        int x;
        String okunanEmail = "";
        try {
            // inputStream=openFileInput("dosyam1.txt"); //tıklandığında bidaha görünsün istersek buraya alınmalıdır.
            while ((x = inputStream1.read()) != -1) {
                okunanEmail += Character.toString((char) x);
            }
            inputStream1.close();

        } catch (Exception e) {

        }

        email_gizli_text.setText(okunanEmail);
        editor.putString("email", okunanEmail);
        editor.apply();
        if (!email_gizli_text.getText().toString().equals("")) {
            Email.setText(okunanEmail);
        }
        //Toast.makeText(MainActivity.this, okunanEmail, Toast.LENGTH_SHORT).show();
    }


    private void password_okuma() {


        int a = 65;
        password_gizli_text.setText(Character.toString((char) a));

        int x;
        String okunanPassword = "";
        try {
            // inputStream=openFileInput("dosyam1.txt"); //tıklandığında bidaha görünsün istersek buraya alınmalıdır.
            while ((x = inputStream2.read()) != -1) {
                okunanPassword += Character.toString((char) x);
            }
            inputStream2.close();

        } catch (Exception e) {

        }

        password_gizli_text.setText(okunanPassword);
        editor.putString("password", okunanPassword);
        editor.apply();
        /*if (!text2.getText().toString().equals("")){
            Password.setText(okunanPassword);
        }*/
        //Snackbar.make(constraintLayout, okunanPassword, Snackbar.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this, okunanPassword, Toast.LENGTH_SHORT).show();
    }


    private void uidokuma() {
        int a = 65;
        uid_gizli_text.setText(Character.toString((char) a));

        int x;
        String okunanuid = "";
        try {
            // inputStream=openFileInput("dosyam1.txt"); //tıklandığında bidaha görünsün istersek buraya alınmalıdır.
            while ((x = inputStream3.read()) != -1) {
                okunanuid += Character.toString((char) x);
            }
            inputStream3.close();

        } catch (Exception e) {

        }

        uid_gizli_text.setText(okunanuid);
        editor.putString("uid", okunanuid);
        editor.apply();
        //Snackbar.make(constraintLayout, okunanuid, Snackbar.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this, okunanPassword, Toast.LENGTH_SHORT).show();
    }

    private void soyisim_okuma() {
        int a = 65;
        text7.setText(Character.toString((char) a));

        int x;
        String okunan_soy_isim = "";
        try {
            // inputStream=openFileInput("dosyam1.txt"); //tıklandığında bidaha görünsün istersek buraya alınmalıdır.
            while ((x = inputStream6.read()) != -1) {
                okunan_soy_isim += Character.toString((char) x);
            }
            inputStream6.close();

        } catch (Exception e) {

        }

        text7.setText(okunan_soy_isim);
        editor.putString("surname", okunan_soy_isim);
        editor.apply();
        //Snackbar.make(constraintLayout, okunan_soy_isim, Snackbar.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this, okunanPassword, Toast.LENGTH_SHORT).show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////Dosya silme fonksiyonları//////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void emailSilme() {
        File dir = getFilesDir();
        File file = new File(dir, "email.txt");
        boolean deleted = file.delete();
    }

    private void passwordSilme() {
        File dir = getFilesDir();
        File file = new File(dir, "password.txt");
        boolean deleted = file.delete();
        editor.putString("password", null);
        editor.apply();
    }

}