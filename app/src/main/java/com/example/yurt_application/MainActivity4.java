package com.example.yurt_application;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity4 extends AppCompatActivity {
Button button;
EditText editText;
    FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mAuth= FirebaseAuth.getInstance();
        button=(Button) findViewById(R.id.sıfreUnuttum);
        editText=(EditText) findViewById(R.id.editTextTextEmailAddress4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= editText.getText().toString();
                if(TextUtils.isEmpty(email)){
                    editText.setError("email cannot be empty");
                    editText.requestFocus();
                }else{
                    mAuth.sendPasswordResetEmail(editText.getText().toString());
                    Toast.makeText(MainActivity4.this
                            , editText.getText().toString()+" Mail adresine şifre yenileme bağlantısı gönderildi."
                            , Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}