package com.example.apptcc.API;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.apptcc.R;
import com.example.apptcc.databinding.ActivityTelaEscolhasVacinasBinding;

public class Tela_EscolhasVacinas extends AppCompatActivity {
    public static final String CATEGORY_KEY = null;
    private ActivityTelaEscolhasVacinasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaEscolhasVacinasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCriancasVacinas.setOnClickListener(view -> {
            String tabela = "/criancas";
            mudarTela(tabela);
        });

        binding.btnAdolescentesVacinas.setOnClickListener(view -> {
            String tabela = "/adolescente";
            mudarTela(tabela);
        });

        binding.btnGestantesVacinas.setOnClickListener(view -> {
            String tabela = "/gestante";
            mudarTela(tabela);
        });

        binding.btnIdosoAdultoVacina.setOnClickListener(view -> {
            String tabela = "/idoso-adulto";
            mudarTela(tabela);
        });
    }

    public void mudarTela(String tabela){
        Intent it = new Intent(this, Tela_ListaVacinas.class);
        it.putExtra(Tela_EscolhasVacinas.CATEGORY_KEY, tabela);
        startActivity(it);
    }
}