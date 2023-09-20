package com.example.apptcc.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.apptcc.R;
import com.example.apptcc.View.CadastroDependentes;
import com.example.apptcc.View.Tela_buscarDependente;
import com.example.apptcc.databinding.ActivityTelaDependenteRecyclerBinding;

public class TelaDependenteRecycler extends AppCompatActivity {
    private ActivityTelaDependenteRecyclerBinding binding;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaDependenteRecyclerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_dependente);

        binding.btnCadastrarDependente.setOnClickListener(view -> {
            Button btnSim =  popup.findViewById(R.id.btnPopUpSim);
            Button btnNao = popup.findViewById(R.id.btnPopUpNao);
            popup.show();

            btnSim.setOnClickListener(view1 -> {
                mudarTelaBuscaDependente();
            });

            btnNao.setOnClickListener(view2 -> {
                mudarTelaCadastarDependente();
            });
        });

        recyclerView = binding.recyclerViewDependentes;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


    }

    public void mudarTelaBuscaDependente(){
        Intent it_mudarTela = new Intent(this, Tela_buscarDependente.class);
        startActivity(it_mudarTela);
    }

    public void mudarTelaCadastarDependente(){
        Intent it_mudarTela = new Intent(this, CadastroDependentes.class);
        startActivity(it_mudarTela);
    }
}