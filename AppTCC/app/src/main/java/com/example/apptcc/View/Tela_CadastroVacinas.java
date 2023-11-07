package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.apptcc.Model.Vacinas;
import com.example.apptcc.databinding.ActivityTelaCadastroVacinasBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Tela_CadastroVacinas extends AppCompatActivity {
    private ActivityTelaCadastroVacinasBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaCadastroVacinasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        binding.btnAdicionarVacina.setOnClickListener(view -> {
            adicionarVacina();
        });
    }

    public void adicionarVacina(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("vacinas");

        Vacinas vacinas = new Vacinas();

        vacinas.setNome(binding.txtCadastroNomeVacina.getText().toString());
        vacinas.setLote(binding.txtCadastroLoteVacina.getText().toString().toUpperCase());
        vacinas.setData(binding.txtCadastroDataVacina.getText().toString());

        ref.add(vacinas).addOnCompleteListener(task -> {
            mudarTelaHome();
            Toast.makeText(this, "Vacina incluida com sucesso", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Log.e("CadastroVAcinas", "Erro ao adicionar Vacinas", e);
        });
    }

    public void mudarTelaHome(){
        Intent it = new Intent(this, NavigationScreen.class);
        startActivity(it);
    }
}