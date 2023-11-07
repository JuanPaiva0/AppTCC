package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.apptcc.Model.Usuario;
import com.example.apptcc.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Tela_Login extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.linkCadastreSe.setOnClickListener(v -> mudarParaTelaDeCadastro());
        binding.linkRecuperarSenha.setOnClickListener(view -> mudarParaTelaRdefinirSenha());
        binding.btnEntrar.setOnClickListener(view -> validaCampos());
    }


    public void validaCampos(){
        //Coleta de informações para a verificação dos campos
        String email = binding.emailLogin.getText().toString().trim();
        String cpf = binding.cpfLogin.getText().toString().trim();
        String senha = binding.senhaLogin.getText().toString().trim();

        if(!email.isEmpty()){
            if (!cpf.isEmpty()){
                if (!senha.isEmpty()){
                    //Após a verificar se nenhum dos campos estão vazio seguir para o método de login
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
        //Coletar o cpf do usuário através do campo de texto
        String cpf = binding.cpfLogin.getText().toString().trim();

        //Método de login proprio do firebase com as informações coletadas a partir dos campos de texto
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                //Após a tarefa ter sucesso com sucesso crir uma variável para ter acesso as informações do
                //usário que está logado atualmente
                FirebaseUser user = auth.getCurrentUser();

                //Fazer uma busca no coleção de usuários para verificar se esse cpf já está cadastrado
                db.collection("users").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Usuario usuario = documentSnapshot.toObject(Usuario.class);
                        if (usuario != null && usuario.getCpf().equals(cpf)){
                            if(user != null){
                                //Se todas as informações estajam corretas, é preciso finalizar essa tela
                                //e levar o usuário para a tela de home
                                finish();
                                mudarParaHome();
                            }
                        } else{
                            Toast.makeText(this, "CPF não cadastrado ou CPF invalido", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Documento não encontrado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {

                });
            } else{
                String resposta = task.getException().toString();
                opcoesErro(resposta);
                Log.e("Login", "Falha ao fazer login", task.getException());
            }
        });
    }


    //Método responsável pelo tratamento dos erro, para facilitar o a navegação do usuário
    public void opcoesErro(String resposta){
        if(resposta.contains("The email address is badly formatted")){
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
        } else if (resposta.contains("There is no user record corresponding to this identifier")) {
            Toast.makeText(this, "Email não cadastrado", Toast.LENGTH_SHORT).show();
        } else if (resposta.contains(" The password is invalid or the user does not have a password")){
            Toast.makeText(this, "Senha inválida", Toast.LENGTH_SHORT).show();
        }
    }

    //Método que não deixa o botão de voltar nativo do celular seja pressionado
    @Override
    public void onBackPressed() {
    }

    //--------------------- Metodos para mudança de telas ------------------------------------------
    public void mudarParaHome(){
        Intent it_mudarTela =  new Intent(this, NavigationScreen.class);
        startActivity(it_mudarTela);
    }

    public void mudarParaTelaDeCadastro(){
       Intent it_telaCadastro = new Intent(this, Tela_CadastroUsuario.class);
       startActivity(it_telaCadastro);
    }

    public void mudarParaTelaRdefinirSenha(){
        Intent it_TelaRedefinirSenha = new Intent(this, Tela_RecuperarSenha.class);
        startActivity(it_TelaRedefinirSenha);
    }
}