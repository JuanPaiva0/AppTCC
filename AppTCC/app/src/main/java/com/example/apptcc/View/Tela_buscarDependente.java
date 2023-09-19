package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.apptcc.Model.Usuario;
import com.example.apptcc.R;
import com.example.apptcc.RecyclerView.TelaDependenteRecycler;
import com.example.apptcc.databinding.ActivityTelaBuscarDependenteBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Tela_buscarDependente extends AppCompatActivity {
    private ActivityTelaBuscarDependenteBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaBuscarDependenteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_infos_dependentes);
        auth = FirebaseAuth.getInstance();


        binding.btnBuscarDependente.setOnClickListener(view -> {
            popup.show();
            TextView txtNome = popup.findViewById(R.id.txtNomeDependente);
            TextView txtCpf = popup.findViewById(R.id.txtCpfDependente);
            Button btnMudarCpf = popup.findViewById(R.id.btnPopUpMudarCpf);
            Button btnCadastrarDependente = popup.findViewById(R.id.btnPopUpCadastrarDependente);
            buscaDependente(new infosCallback() {
                @Override
                public void infosUsuarios(String nome, String sobrenome, String cpf) {

                    txtNome.setText(nome + " " + sobrenome);
                    txtCpf.setText(cpf);
                }
            });

            btnMudarCpf.setOnClickListener(view1 -> {
                popup.dismiss();
            });

            btnCadastrarDependente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cadastrarDependentes();
                }
            });

        });

    }

    public interface infosCallback{
        void infosUsuarios(String nome, String sobrenome, String cpf);
    }


    public void buscaDependente(infosCallback callback){
        CollectionReference collec = db.collection("users");

        String cpf = binding.txtBuscaDependente.getText().toString().trim();

        collec.whereEqualTo("cpf", cpf).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documents = task.getResult();
                if (!documents.isEmpty()) {
                    DocumentSnapshot document = documents.getDocuments().get(0);
                    String nome = document.getString("nome");
                    String sobrenome = document.getString("sobrenome");
                    String userCpf = document.getString("cpf");

                    callback.infosUsuarios(nome, sobrenome, userCpf);
                }
            }
        });
    }

    public void cadastrarDependentes(){
        CollectionReference collec = db.collection("users");
        FirebaseUser user = auth.getCurrentUser();

        String cpf = binding.txtBuscaDependente.getText().toString().trim();

        collec.whereEqualTo("cpf", cpf).get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               QuerySnapshot documents = task.getResult();
               if (!documents.isEmpty()){
                   DocumentSnapshot document = documents.getDocuments().get(0);

                   Usuario dependente = new Usuario();

                   dependente.setNome(document.getString("nome"));
                   dependente.setSobrenome(document.getString("sobrenome"));
                   dependente.setEmail(document.getString("email"));
                   dependente.setCpf(document.getString("cpf"));

                   db.collection("users").document(user.getUid())
                           .collection("dependentes")
                           .add(dependente).addOnSuccessListener(documentReference -> {
                                finish();
                                voltarTelaDependentes();
                           }).addOnFailureListener(e -> {

                           });
               }
           }
        });
    }

    public void voltarTelaDependentes(){
        Intent it_mudarTela = new Intent(this, TelaDependenteRecycler.class);
        startActivity(it_mudarTela);
    }
}