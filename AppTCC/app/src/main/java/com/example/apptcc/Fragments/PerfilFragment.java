package com.example.apptcc.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apptcc.Model.Usuario;
import com.example.apptcc.R;
import com.example.apptcc.View.Tela_recuperarSenha;
import com.example.apptcc.databinding.FragmentPerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.security.auth.callback.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding bindind;
    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bindind = FragmentPerfilBinding.inflate(inflater, container, false);
        View view = bindind.getRoot();

        TextView txtNome = view.findViewById(R.id.txtNomeUsuario);
        TextView txtEmail = view.findViewById(R.id.txtEmailUsuario);
        TextView txtCpf = view.findViewById(R.id.txtCpfUsuario);
        buscaInfosUsuario(new infosCallback() {
            @Override
            public void infosUsuarios(String nome, String sobrenome, String email, String cpf) {
                txtNome.setText(nome + " " + sobrenome);
                txtEmail.setText(email);
                txtCpf.setText(cpf);
            }
        });

        bindind.txtPerfilRedefinirSenha.setOnClickListener(view1 -> {
            mudarParaTelaRdefinirSenha();
        });
        return view;
    }

    public interface infosCallback{
        void infosUsuarios(String nome, String sobrenome, String email, String cpf);
    }

    public void buscaInfosUsuario(infosCallback callback){
        CollectionReference collec = db.collection("users");
        FirebaseUser user = auth.getCurrentUser();

        collec.document(user.getUid()).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               DocumentSnapshot document = task.getResult();
               Usuario usuario = document.toObject(Usuario.class);
               String nome = usuario.getNome();
               String sobrenome = usuario.getSobrenome();
               String email = usuario.getEmail();
               String cpf = usuario.getCpf();

               callback.infosUsuarios(nome, sobrenome,email, cpf);
           }
        });
    }

    public void mudarParaTelaRdefinirSenha(){
        Intent it_TelaRedefinirSenha = new Intent(getActivity(), Tela_recuperarSenha.class);
        startActivity(it_TelaRedefinirSenha);
    }
}