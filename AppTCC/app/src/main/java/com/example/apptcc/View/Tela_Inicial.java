package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.apptcc.databinding.ActivityTelaInicialBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Tela_Inicial extends AppCompatActivity {
    private ActivityTelaInicialBinding binding;
    private FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Tela inicial que precede a tela de login
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaInicialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        binding.btnAcessar.setOnClickListener(v -> mudarParaTelaLogin());
    }

    //--------------------- Metodos para mudança de telas ------------------------------------------
    public void mudarParaTelaLogin(){
        Intent it_telaLogin = new Intent(this, Tela_Login.class);
        startActivity(it_telaLogin);
    }
}