package com.example.apptcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        link = findViewById(R.id.link_cadastre_se);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarParaTelaDeCadastro();
            }
        });
    }


    public void mudarParaTelaDeCadastro(){
       Intent it_telaCadastro = new Intent(this, Tela_cadastro.class);
       startActivity(it_telaCadastro);
    }
}