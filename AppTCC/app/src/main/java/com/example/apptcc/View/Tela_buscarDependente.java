package com.example.apptcc.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import com.example.apptcc.Model.Usuario;
import com.example.apptcc.R;
import com.example.apptcc.databinding.ActivityTelaBuscarDependenteBinding;
import com.google.firebase.auth.FirebaseAuth;
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
        popup.setContentView(R.layout.popup_dependente);
        TextView txtNome = popup.findViewById(R.id.txtNomeDependente);
        TextView txtCpf = popup.findViewById(R.id.txtCpfDependente);

        binding.btnBuscarDependente.setOnClickListener(view -> {
            popup.show();
            buscaDependente(new infosCallback() {
                @Override
                public void infosUsuarios(String nome, String sobrenome, String cpf) {
                    txtNome.setText(nome + " " + sobrenome);
                    txtCpf.setText(cpf);
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
}