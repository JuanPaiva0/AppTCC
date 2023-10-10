package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.apptcc.R;
import com.example.apptcc.databinding.ActivityTelaConfigPerfilBinding;

public class Tela_ConfigPerfil extends AppCompatActivity {
    private ActivityTelaConfigPerfilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaConfigPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtExcluirDependente.setOnClickListener(view -> {
            mudarTelaExclusaoDependente();
        });

    }

    public void mudarTelaExclusaoDependente(){
        Intent it = new Intent(this, Tela_ExclusaoDependente.class);
        startActivity(it);
    }
}