package com.example.apptcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apptcc.databinding.ActivityTelaRecuperarSenhaBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Tela_recuperarSenha extends AppCompatActivity {

    private ActivityTelaRecuperarSenhaBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaRecuperarSenhaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();


        binding.btnRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validadDados();
            }
        });
    }

    public void validadDados(){
        String email = binding.emailRedefinirSenha.getText().toString().trim();

        if (!email.isEmpty()){
            recuperarSenha(email);
        } else {
            Toast.makeText(this, "Informe seu email", Toast.LENGTH_SHORT).show();
        }
    }

    public void recuperarSenha(String email){
        auth.sendPasswordResetEmail(
                email
        ).addOnCompleteListener(task ->{
            if (task.isSuccessful()){
                Toast.makeText(this, "Link enviado", Toast.LENGTH_SHORT).show();
                finish();
                mudarTelaParaLogin();
            } else {
                Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mudarTelaParaLogin(){
        Intent it_mudarTela = new Intent(this, MainActivity.class);
        startActivity(it_mudarTela);
    }
}