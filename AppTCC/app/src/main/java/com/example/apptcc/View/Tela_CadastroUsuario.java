package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptcc.Model.Usuario;
import com.example.apptcc.databinding.ActivityTelaCadastroBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Tela_CadastroUsuario extends AppCompatActivity {

    TextView link;
    private ActivityTelaCadastroBinding binding;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        binding.btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaCampos();
            }
        });

       binding.linkLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mudarParaTelaDeLogin();
           }
       });
    }

    public void validaCampos(){
        //Coleta de dados para a validação dos campos
        String nome = binding.NomeCadastro.getText().toString().trim();
        String sobreNome = binding.sobreNomeCadastro.getText().toString().trim();
        String email = binding.emailCadastro.getText().toString().trim();
        String cpf = binding.cpfCadastro.getText().toString().trim();
        String senha = binding.senhaCadastro.getText().toString().trim();

        if (!nome.isEmpty()){
            if (!sobreNome.isEmpty()){
                if (!email.isEmpty()){
                    if (!senha.isEmpty()){
                        if (!cpf.isEmpty()){
                            if (cpf.length() == 11) {
                                //Após a validação seguir para a validação do CPF
                                validaCpf(cpf);
                            } else{
                                Toast.makeText(this, "Insira um número valido de CPF", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Informe seu cpf", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Informe sua senha", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Informe seu email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Informe seu sobrenome", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Informe seu nome", Toast.LENGTH_SHORT).show();
        }
    }

    public void criarConta(String email, String senha){
        //Realizar a criação do login do usuário através do método nativo do firebase
        auth.createUserWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(task ->{
            if (task.isSuccessful()){
                //Após a tarefa estar completa criar variável para ter acesso as informações do usuário
                FirebaseUser user = auth.getCurrentUser();

                //Criar um objeto e inserir todas as informações do usuário
                Usuario usuario = new Usuario();

                usuario.setNome(binding.NomeCadastro.getText().toString().trim());
                usuario.setSobrenome(binding.sobreNomeCadastro.getText().toString().trim());
                usuario.setEmail(binding.emailCadastro.getText().toString().trim());
                usuario.setCpf(binding.cpfCadastro.getText().toString().trim());
                usuario.setSenha(binding.senhaCadastro.getText().toString().trim());

                //Após isso inserir todas as informações no banco de dados
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(user.getUid()).set(usuario).addOnSuccessListener(aVoid ->{
                    finish();
                    mudarParaHome();
                    Toast.makeText(this, "Usuário registrado com sucesso", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {

                });
            } else{
                String resposta = task.getException().toString();
                opcoesErro(resposta);
                Log.e("Registro", "Falha ao registrar usuário", task.getException());
            }
        });
    }

    public void validaCpf(String cpf){
        //Instanciat o banco de dados e definir qual será a coleção de referência
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("users");

        //Verificar no banco de dados se já existe um documento com o CPF informado
        ref.whereEqualTo("cpf", cpf).get().addOnCompleteListener(task -> {
           if (task.isSuccessful() && !task.getResult().isEmpty()){
               //o CPF já esta cadastrado
               Toast.makeText(this, "Esse cpf já esta cadastrado", Toast.LENGTH_SHORT).show();
           } else {
               String email = binding.emailCadastro.getText().toString().trim();
               String senha = binding.senhaCadastro.getText().toString().trim();
                criarConta(email, senha);
           }
        });
    }

    //Método responsável pelo tratamento dos erro, para facilitar o a navegação do usuário
    public void opcoesErro(String resposta){
        if(resposta.contains("The email address is badly formatted")){
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
        } else if (resposta.contains("least 6 characters")){
            Toast.makeText(this, "A senha precisa ter no minino 6 caracteres", Toast.LENGTH_SHORT).show();
        } else if (resposta.contains("The email address is already in use by another account")){
            Toast.makeText(this, "Email já cadastrado, uitlize outro email", Toast.LENGTH_SHORT).show();
        }
    }

    //--------------------- Metodos para mudança de telas ------------------------------------------
    public void mudarParaHome(){
        Intent it_mudarTela = new Intent(this, NavigationScreen.class);
        startActivity(it_mudarTela);
    }

    public void mudarParaTelaDeLogin(){
       Intent it_mudarTela = new Intent(this, Tela_Login.class);
        startActivity(it_mudarTela);
    }
}