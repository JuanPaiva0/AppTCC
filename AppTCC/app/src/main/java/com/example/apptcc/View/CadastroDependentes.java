package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.apptcc.R;
import com.example.apptcc.databinding.ActivityCadastroDependentesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastroDependentes extends AppCompatActivity {
    private ActivityCadastroDependentesBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroDependentesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }

    public void validaCampos(){
        String nome = binding.nomeCadastroDependente.getText().toString().trim();
        String sobrenome = binding.sobreNomeCadastroDependente.getText().toString().trim();
        String email = binding.emailCadastroDependente.getText().toString().trim();
        String senha = binding.senhaCadastroDependente.getText().toString().trim();
        String cpf = binding.cpfCadastroDependente.getText().toString().trim();

        if (!nome.isEmpty()){
            if (!sobrenome.isEmpty()){
                if (!email.isEmpty()){
                    if (!senha.isEmpty()){
                        if (!cpf.isEmpty()){

                        } else {
                            Toast.makeText(this, "Informe o cpf do dependente", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Informe a senha do dependente", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Informe o email do dependente", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Informe o sobrenome do dependente", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Informe o nome do dependente", Toast.LENGTH_SHORT).show();
        }
    }

    public void validaCpf(String cpf){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("users");

        ref.whereEqualTo("cpf", cpf).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()){
                //o CPF já esta cadastrado
                Toast.makeText(this, "Esse cpf já esta cadastrado", Toast.LENGTH_SHORT).show();
            } else {
                String email = binding.emailCadastroDependente.getText().toString().trim();
                String senha = binding.senhaCadastroDependente.getText().toString().trim();
                cadastarDependente(email, senha);
            }
        });
    }

    private void cadastarDependente(String email, String senha) {
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseUser dependente = auth.getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collec = db.collection("users");


            }
        });
    }
}