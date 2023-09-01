package com.example.apptcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptcc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.linkCadastreSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarParaTelaDeCadastro();
            }
        });


        binding.btnEntrar.setOnClickListener(v -> validaCampos());





    }


    public void validaCampos(){
        String cpf = binding.cpfLogin.getText().toString().trim();
        String senha = binding.senhaLogin.getText().toString().trim();

        if(!cpf.isEmpty()){
            if (!senha.isEmpty()){

            } else {
                Toast.makeText(this, "Informe sua senha", Toast.LENGTH_SHORT).show();
            }

        } else{
            Toast.makeText(this, "Informe seu CPF", Toast.LENGTH_SHORT).show();
        }
    }



    public void mudarParaTelaDeCadastro(){
       Intent it_telaCadastro = new Intent(this, Tela_cadastro.class);
       startActivity(it_telaCadastro);
    }
}