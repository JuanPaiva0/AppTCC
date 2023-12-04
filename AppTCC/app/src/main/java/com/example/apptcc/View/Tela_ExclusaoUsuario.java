package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.apptcc.databinding.ActivityTelaExclusaoUsuarioBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Tela_ExclusaoUsuario extends AppCompatActivity {
    private ActivityTelaExclusaoUsuarioBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaExclusaoUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();


        binding.btnExclusaoUsuario.setOnClickListener(view -> {
            excluirUsuario();
        });
    }

    public void excluirUsuario(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("users");

        //Método nativo do firebase para a exclusão de documento
        db.collection("users").document(auth.getCurrentUser().getUid()).delete()
                .addOnSuccessListener(task -> {
                    Toast.makeText(this, "Usuário excluido com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                    mudatTelaLogin();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao excluir usuário", Toast.LENGTH_SHORT).show();
                    Log.e("Excluir usuario", e.getMessage());
                });
    }

    //--------------------- Metodos para mudança de telas ------------------------------------------
    public void mudatTelaLogin(){
        Intent it = new Intent(this, Tela_Login.class);
        startActivity(it);
    }
}