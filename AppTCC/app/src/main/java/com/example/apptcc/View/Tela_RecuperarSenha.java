package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.apptcc.databinding.ActivityTelaRecuperarSenhaBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Tela_RecuperarSenha extends AppCompatActivity {

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
        //Coleta de dados para a verifação dos campos
        String email = binding.emailRedefinirSenha.getText().toString().trim();

        if (!email.isEmpty()){
            recuperarSenha(email);
        } else {
            Toast.makeText(this, "Informe seu email", Toast.LENGTH_SHORT).show();
        }
    }

    public void recuperarSenha(String email){
        //Após a coleta dos dados, usar o método proprio do firebase para a recuperação de senha
        auth.sendPasswordResetEmail(
                email
        ).addOnCompleteListener(task ->{
            if (task.isSuccessful()){
                //Após a tarefa estar completa informar uma messagem na tela dizendo que o link para
                // a definição da senha foi enviado no email enviado pelo usuário
                Toast.makeText(this, "Link enviado", Toast.LENGTH_SHORT).show();
                finish();
                mudarTelaParaLogin();
            } else {
                String resposta = task.getException().toString();
                opcoesErro(resposta);
                Log.e("Login", "Falha ao fazer login", task.getException());
            }
        });
    }

    //Método responsável pelo tratamento dos erro, para facilitar o a navegação do usuário
    public void opcoesErro(String resposta){
        if (resposta.contains("The email address is badly formatted")){
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
        } else if (resposta.contains("There is no user record corresponding to this identifier. The user may have been deleted")) {
            Toast.makeText(this, "Email não cadastrado", Toast.LENGTH_SHORT).show();
        }
    }

    //--------------------- Metodos para mudança de telas ------------------------------------------
    public void mudarTelaParaLogin(){
        Intent it_mudarTela = new Intent(this, Tela_Login.class);
        startActivity(it_mudarTela);
    }
}