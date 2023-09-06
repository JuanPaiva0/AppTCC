package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.apptcc.R;
import com.example.apptcc.databinding.ActivityMainBinding;
import com.example.apptcc.databinding.ActivityTelaInicialBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TelaInicial extends AppCompatActivity {
    private ActivityTelaInicialBinding binding;
    private FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaInicialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.btnAcessar.setOnClickListener(v -> mudarParaTelaLogin());
    }

    public void mudarParaTelaLogin(){
        Intent it_telaLogin = new Intent(this, MainActivity.class);
        startActivity(it_telaLogin);
    }
}