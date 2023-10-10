package com.example.apptcc.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptcc.Model.Dependente;
import com.example.apptcc.Model.Usuario;
import com.example.apptcc.R;
import com.example.apptcc.View.CadastroDependentes;
import com.example.apptcc.View.NavigationScreen;
import com.example.apptcc.View.Tela_buscarDependente;
import com.example.apptcc.databinding.ActivityTelaDependenteRecyclerBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class TelaDependenteRecycler extends AppCompatActivity {
    private ActivityTelaDependenteRecyclerBinding binding;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private Adapter adapter;
    private FirebaseAuth auth;
    List<Dependente> dependentesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaDependenteRecyclerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_dependente);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        setupRecyclerView();

        adapter.setOnItemClickListener(dependente -> OnItemClick(dependente));

        binding.btnCadastrarDependente.setOnClickListener(view -> {
            Button btnSim = popup.findViewById(R.id.btnPopUpSim);
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
    }

    private void setupRecyclerView() {
        CollectionReference ref = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("dependentes");
        Query query = ref.orderBy("nome");

        FirestoreRecyclerOptions<Usuario> options = new FirestoreRecyclerOptions.Builder<Usuario>()
                .setQuery(query, Usuario.class)
                .build();

        recyclerView = binding.recyclerViewDependentes;

        if (adapter != null) {
            adapter.stopListening();
        }

        adapter = new Adapter(options);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        adapter.startListening();
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter = null;
    }

    public void OnItemClick(Usuario dependente){
        Log.d("TelaDependenteRecycler", "OnItemClick chamado.");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("users")
                .document(auth.getCurrentUser().getUid()).
                collection("dependentes");

        String cpfDependente = dependente.getCpf();


        ref.whereEqualTo("cpf", cpfDependente).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                    Dependente dependenteSelecionado = task.getResult().toObjects(Dependente.class).get(0);
                    String email = dependenteSelecionado.getEmail();
                    String senha = dependenteSelecionado.getSenha();

                    auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Log.d("TelaDependenteRecycler", "Login bem-sucedido.");
                            finish();
                            mudarParaHome();
                        } else {
                            Log.e("TelaDependenteRecycler", "Erro ao fazer login: " + task1.getException());
                        }
                    });
                } else {
                    Log.e("TelaDependenteRecycler", "Nenhum dependente encontrado com o CPF: " + cpfDependente);
                }
            } else {
                Log.e("TelaDependenteRecycler", "Erro na consulta do Firestore: " + task.getException());
            }
        });

    }


    public void mudarTelaBuscaDependente(){
        Intent it_mudarTela = new Intent(this, Tela_buscarDependente.class);
        startActivity(it_mudarTela);
    }

    public void mudarTelaCadastarDependente(){
        Intent it_mudarTela = new Intent(this, CadastroDependentes.class);
        startActivity(it_mudarTela);
    }

    public void mudarParaHome(){
        Intent it_mudarTela = new Intent(this, NavigationScreen.class);
        startActivity(it_mudarTela);
    }
}