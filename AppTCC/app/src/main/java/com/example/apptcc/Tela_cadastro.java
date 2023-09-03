package com.example.apptcc;

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
import com.google.firebase.firestore.FirebaseFirestore;

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
        String nome = binding.NomeCadastro.getText().toString().trim();
        String email = binding.emailCadastro.getText().toString().trim();
        String cpf = binding.cpfCadastro.getText().toString().trim();
        String senha = binding.senhaCadastro.getText().toString().trim();

        if (!nome.isEmpty()){
            if (!email.isEmpty()){
                if (!cpf.isEmpty()){
                    if (!senha.isEmpty()){

                        criarConta(email, senha);

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

    public void criarConta(String email, String senha){
        auth.createUserWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(task ->{
            if (task.isSuccessful()){
                FirebaseUser user = auth.getCurrentUser();

                Usuario usuario = new Usuario();

                usuario.setNome(binding.NomeCadastro.getText().toString().trim());
                usuario.setCpf(binding.cpfCadastro.getText().toString().trim());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("user").document(user.getUid()).set(usuario).addOnSuccessListener(aVoid ->{

                    Toast.makeText(this, "Usuário registrado com sucesso", Toast.LENGTH_SHORT).show();

                }).addOnFailureListener(e ->{

                });
            } else{
                Toast.makeText(this, "Falha ao registrar usuario", Toast.LENGTH_SHORT).show();
                Log.e("Registro", "Falha ao registrar usuário", task.getException());
            }
        });
    }


    public void mudarParaTelaDeLogin(){
       Intent it_mudarTela = new Intent(this, MainActivity.class);
        startActivity(it_mudarTela);
    }

}