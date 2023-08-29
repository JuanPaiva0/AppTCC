package com.example.apptcc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Tela_cadastro extends AppCompatActivity {

    TextView link;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

       link = findViewById(R.id.link_login);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarParaTelaDeLogin();
           }
        });
    }

    public void mudarParaTelaDeLogin(){
       Intent it_mudarTela = new Intent(this, MainActivity.class);
        startActivity(it_mudarTela);

    }

}