package com.example.apptcc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptcc.databinding.ActivityTelaCadastroBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Tela_cadastro extends AppCompatActivity {

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

       binding.linkLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mudarParaTelaDeLogin();
           }
       });
    }

    public void validaCampos(){
        String nome = binding.NomeCadastro.getText().toString().trim();
        String email = binding.emailCadastro.getText().toString().trim();
        String cpf = binding.cpfCadastro.getText().toString().trim();
        String senha = binding.senhaCadastro.getText().toString().trim();

        if (!nome.isEmpty()){
            if (!email.isEmpty()){
                if (!cpf.isEmpty()){
                    if (!senha.isEmpty()){

                        criarConta(nome, email, cpf, senha);

                    } else {
                        Toast.makeText(this, "Informe seu nome", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Informe seu cpf", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Informe seu email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Informe seu nome", Toast.LENGTH_SHORT).show();
        }
    }

    public void criarConta(String nome, String email, String cpf, String senha){
        auth.createUserWithEmailAndPassword()
    }


    public void mudarParaTelaDeLogin(){
       Intent it_mudarTela = new Intent(this, MainActivity.class);
        startActivity(it_mudarTela);
    }

}