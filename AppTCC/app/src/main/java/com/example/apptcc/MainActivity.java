package com.example.apptcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptcc.Model.Usuario;
import com.example.apptcc.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();


        binding.linkCadastreSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarParaTelaDeCadastro();
            }
        });
        binding.btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaCampos();
                mudarParaHome();
            }
        });
    }


    public void validaCampos(){
        String email = binding.emailLogin.getText().toString().trim();
        String cpf = binding.cpfLogin.getText().toString().trim();
        String senha = binding.senhaLogin.getText().toString().trim();

        if(!email.isEmpty()){
            if (!cpf.isEmpty()){
                if (!senha.isEmpty()){

                    login(email, senha);

                } else {
                    Toast.makeText(this, "Informe sua senha", Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(this, "Informe seu CPF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Informe seu email", Toast.LENGTH_SHORT).show();
        }
    }

    public void login(String email, String senha){
        String cpf = binding.cpfLogin.getText().toString().trim();
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                FirebaseUser user = auth.getCurrentUser();

                db.collection("users").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Usuario usuario = documentSnapshot.toObject(Usuario.class);
                        if (usuario != null && usuario.getCpf().equals(cpf)){

                        } else{
                            Toast.makeText(this, "CPF não cadastrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Documento não encontrado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {

                });
            } else{
                Toast.makeText(this, "Falha ao fazer login", Toast.LENGTH_SHORT).show();
                Log.e("Login", "Falha ao fazer login", task.getException());
            }
        });
    }



    public void mudarParaHome(){
        Intent it_mudarTela =  new Intent();
        startActivity(it_mudarTela);
    }

    public void mudarParaTelaDeCadastro(){
       Intent it_telaCadastro = new Intent(this, Tela_cadastro.class);
       startActivity(it_telaCadastro);
    }
}