package com.example.apptcc.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptcc.Model.Dependente;
import com.example.apptcc.R;
import com.example.apptcc.View.CadastroDependentes;
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
    private FirebaseAuth auth;
    private Adapter adapter;
    List<Dependente> dependentesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaDependenteRecyclerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_dependente);

        recyclerView = binding.recyclerViewDependentes;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();



        verificarDependentes();

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
    }

    public void verificarDependentes() {
        CollectionReference collec = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("dependentes");

        Query query = collec;

        FirestoreRecyclerOptions<Dependente> options = new FirestoreRecyclerOptions.Builder<Dependente>()
                .setQuery(query, Dependente.class)
                .build();

        adapter = new Adapter(options.getSnapshots());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }





//    public void verificarDependentes(){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//
//        if (user != null){
//            CollectionReference collec = db.collection(user.getUid()).getFirestore().collection("dependentes");
//
//            collec.addSnapshotListener((value, error) -> {
//             for (DocumentChange dc : value.getDocumentChanges()){
//                 if (dc.getType() == DocumentChange.Type.ADDED){
//                     dependentesList.add(dc.getDocument().toObject(Dependente.class));
//                 }
//             }
//             adapter.updateDados(dependentesList);
//            });
//        }
//    }

//    public void verificarDependentes() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//
//        if (user != null) {
//            CollectionReference collec = db.collection("dependentes");
//
//            collec.get().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    List<Usuario> dependentesList = new ArrayList<>();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Usuario dependentes = document.toObject(Usuario.class);
//                        dependentesList.add(dependentes);
//                    }
//                    if (!dependentesList.isEmpty()) {
//                        binding.txtSemDependentes.setVisibility(View.GONE);
//                        adapter.updateDados(dependentesList);
//
//                    } else {
//                        binding.recyclerViewDependentes.setVisibility(View.GONE);
//                        binding.txtSemDependentes.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//        }
//    }

    public void mudarTelaBuscaDependente(){
        Intent it_mudarTela = new Intent(this, Tela_buscarDependente.class);
        startActivity(it_mudarTela);
    }

    public void mudarTelaCadastarDependente(){
        Intent it_mudarTela = new Intent(this, CadastroDependentes.class);
        startActivity(it_mudarTela);
    }
}
