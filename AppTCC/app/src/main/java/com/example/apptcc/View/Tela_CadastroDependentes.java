package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.apptcc.Model.Dependente;
import com.example.apptcc.RecyclerView.Tela_DependenteRecycler;
import com.example.apptcc.databinding.ActivityCadastroDependentesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Tela_CadastroDependentes extends AppCompatActivity {
    private ActivityCadastroDependentesBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroDependentesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        binding.btnCadastroDpendentes.setOnClickListener(view -> {
            validaCampos();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        voltarTelaDependentes();
    }

    public void validaCampos(){
        //Coleta de dados para a validação dos campos
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
                            //Após a validação dos campos seguir para o método de validação de CPF
                            validaCpf(cpf);
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
        //Fazer a instancia do Firebase Firestore e definir a coleção de referência
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("users");

        //Fazer uma busca no banco de dados para verificar se o cpf informado está cadastrado
        ref.whereEqualTo("cpf", cpf).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()){

                //Caso o CPF já esteja cadastrado, mostrar uma menssagem na tela do usuário informado
                //que esse CPF já está cadastrado
                Toast.makeText(this, "Esse cpf já esta cadastrado", Toast.LENGTH_SHORT).show();
            } else {
                //Caso o CPF não esteja na coleção de usuários, coletar o email e senha para, realizar
                //o método de cadastro
                String email = binding.emailCadastroDependente.getText().toString().trim();
                String senha = binding.senhaCadastroDependente.getText().toString().trim();
                cadastrarDependente(email, senha);
            }
        });
    }

    private void cadastrarDependente(String email, String senha) {

        String usuarioResponsavel = auth.getCurrentUser().getUid();

        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseUser dependente = auth.getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference usersCollection = db.collection("users");

                Dependente infosDependente = new Dependente();
                String cpf = binding.cpfCadastroDependente.getText().toString();

                infosDependente.setNome(binding.nomeCadastroDependente.getText().toString());
                infosDependente.setSobrenome(binding.sobreNomeCadastroDependente.getText().toString());
                infosDependente.setEmail(binding.emailCadastroDependente.getText().toString());
                infosDependente.setCpf(binding.cpfCadastroDependente.getText().toString());
                infosDependente.setSenha(binding.senhaCadastroDependente.getText().toString());

                usersCollection.document(dependente.getUid()).set(infosDependente).addOnSuccessListener(aVoid -> {
                    db.collection("users").document(usuarioResponsavel)
                            .collection("dependentes")
                            .document(dependente.getUid())
                            .set(infosDependente).addOnSuccessListener(documentReference -> {
                                Toast.makeText(this, "Dependente cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                finish();
                                voltarTelaDependentes();
                            }).addOnFailureListener(e -> {

                            });
                }).addOnFailureListener(e -> {

                });
            } else {
                String resposta = task.getException().toString();
                opcoesErro(resposta);
            }
        });
    }

    public void voltarTelaDependentes(){
        Intent it_mudarTela = new Intent(this, Tela_DependenteRecycler.class);
        startActivity(it_mudarTela);
    }

    public void opcoesErro(String resposta){
        if(resposta.contains("The email address is badly formatted")){
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
        } else if (resposta.contains("least 6 characters")){
            Toast.makeText(this, "A senha precisa ter no minino 6 caracteres", Toast.LENGTH_SHORT).show();
        } else if (resposta.contains("The email address is already in use by another account")){
            Toast.makeText(this, "Email já cadastrado, uitlize outro email", Toast.LENGTH_SHORT).show();
        }
    }

}