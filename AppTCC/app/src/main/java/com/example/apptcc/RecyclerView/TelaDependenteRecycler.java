package com.example.apptcc.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.apptcc.Model.Usuario;
import com.example.apptcc.R;
import com.example.apptcc.View.CadastroDependentes;
import com.example.apptcc.View.Tela_buscarDependente;
import com.example.apptcc.databinding.ActivityTelaDependenteRecyclerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TelaDependenteRecycler extends AppCompatActivity {
    private ActivityTelaDependenteRecyclerBinding binding;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaDependenteRecyclerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_dependente);
        recyclerView = binding.recyclerViewDependentes;
        db = FirebaseFirestore.getInstance();



        binding.btnCadastrarDependente.setOnClickListener(view -> {
            Button btnSim =  popup.findViewById(R.id.btnPopUpSim);
            Button btnNao = popup.findViewById(R.id.btnPopUpNao);
            popup.show();

            btnSim.setOnClickListener(view1 -> {
                popup.dismiss();
                mudarTelaBuscaDependente();
            });

            btnNao.setOnClickListener(view2 -> {
                popup.dismiss();
                mudarTelaCadastarDependente();
            });
        });
        verificarDependentes();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public void verificarDependentes(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            CollectionReference collec = db.collection("dependentes");

            collec.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    List<Usuario> dependentesList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Usuario dependentes = document.toObject(Usuario.class);
                        dependentesList.add(dependentes);
                    }

                    if (!dependentesList.isEmpty()) {
                        setupRecyclerView(dependentesList);
                        binding.txtSemDependentes.setVisibility(View.GONE);
                    } else {
                        binding.recyclerViewDependentes.setVisibility(View.GONE);
                        binding.txtSemDependentes.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void setupRecyclerView(List<Usuario> dependentesList){
        Adapter adapter = new Adapter(dependentesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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