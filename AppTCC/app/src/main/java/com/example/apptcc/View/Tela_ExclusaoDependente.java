package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptcc.R;
import com.example.apptcc.databinding.ActivityTelaExclusaoDependenteBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Tela_ExclusaoDependente extends AppCompatActivity {
    private ActivityTelaExclusaoDependenteBinding binding;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaExclusaoDependenteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_exclusao_dependentes);

        binding.btnExclusaoBusca.setOnClickListener(view -> {
            popup.show();
            TextView txtNome = popup.findViewById(R.id.txtExclusaoNome);
            TextView txtCpf = popup.findViewById(R.id.txtExclusaoCpf);
            Button btnExcluir = popup.findViewById(R.id.btnExcluirDependente);
            Button btnMudarCpf = popup.findViewById(R.id.btnExclusaoMudarCpf);

            buscaDependente(new infosCallback() {
                @Override
                public void infosDependentes(String nome, String sobrenome, String cpf) {
                    txtNome.setText(nome + " " + sobrenome);
                    txtCpf.setText(cpf);
                }
            });

            btnExcluir.setOnClickListener(view1 -> {
                exclusaoDependente();
            });

            btnMudarCpf.setOnClickListener(view2 -> {
                popup.dismiss();
            });

        });
    }

    public interface infosCallback{
        void infosDependentes(String nome, String sobrenome, String cpf);
    }


    public void buscaDependente(Tela_ExclusaoDependente.infosCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collec = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("dependentes");

        String cpf = binding.txtExclusaoBusca.getText().toString().trim();

        collec.whereEqualTo("cpf", cpf).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documents = task.getResult();
                if (!documents.isEmpty()) {
                    DocumentSnapshot document = documents.getDocuments().get(0);
                    String nome = document.getString("nome");
                    String sobrenome = document.getString("sobrenome");
                    String userCpf = document.getString("cpf");

                    callback.infosDependentes(nome, sobrenome, userCpf);
                }
            }
        });
    }

    public void exclusaoDependente(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("dependentes");

        String cpf = binding.txtExclusaoBusca.getText().toString().trim();

        ref.whereEqualTo("cpf", cpf).get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
            QuerySnapshot documents = task.getResult();
            if (!documents.isEmpty()){
                DocumentSnapshot document = task.getResult().getDocuments().get(0);

                String idDependente = document.getId();
                ref.document(idDependente).delete().addOnCompleteListener(aVoid -> {
                    mudarTelaHome();
                    Toast.makeText(this, "Dependente excluído com sucesso", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao excluir o dependente", Toast.LENGTH_SHORT).show();
                    Log.e("Exclusão de Dependente", "Erro ao excluir o dependente", e);
                });
            } else{
                Toast.makeText(this, "Dependente não encontrado", Toast.LENGTH_SHORT).show();
            }
           } else {
               Toast.makeText(this, "Erro na consulta", Toast.LENGTH_SHORT).show();
               Log.e("Consulta de Dependente", "Erro na consulta", task.getException());
           }
        });
    }

    public void mudarTelaHome(){
        Intent it = new Intent(this, NavigationScreen.class);
        startActivity(it);
    }
}