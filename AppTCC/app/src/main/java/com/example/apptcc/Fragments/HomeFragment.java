package com.example.apptcc.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apptcc.API.Tela_EscolhasVacinas;
import com.example.apptcc.Model.Usuario;
import com.example.apptcc.R;
import com.example.apptcc.RecyclerView.Tela_DependenteRecycler;
import com.example.apptcc.View.Tela_CadastroVacinas;
import com.example.apptcc.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        TextView txt = view.findViewById(R.id.txtBemVindo);
        buscarNomeUsuario(new nomeCallback() {
            @Override
            public void nomeUsuario(String nome) {
                txt.setText("Seja bem vindo, " + nome);
            }
        });

        binding.btnHomeFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTelaDependentes();
            }
        });

        binding.btnHomeAdicionarVacina.setOnClickListener(view1 -> {
            mudarTelaCadastroVacinas();
        });

        binding.btnHomeCalendario.setOnClickListener(view2 ->{
            mudarTelaCalendario();
        });

        return view;
    }

    //Interface que serve como mecanismo para fornecer uma função de retorno que só é invocada
    //quando os dados forem buscado corretamente
    public interface nomeCallback {
        void nomeUsuario(String nome);
    }


    public void buscarNomeUsuario(nomeCallback callback){
        //Definir a coleção de referência
        CollectionReference collec = db.collection("users");
        FirebaseUser user = auth.getCurrentUser();

        //Fazer uma pesquisa no banco com o ID do usuário que esta logado atualmete e buscar somente
        //seu primeiro nome
        collec.document(user.getUid()).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               DocumentSnapshot document = task.getResult();
               Usuario usuario  = document.toObject(Usuario.class);
               String nome = usuario.getNome();
               callback.nomeUsuario(nome);
           }
        });
    }

    //--------------------- Metodos para mudança de telas ------------------------------------------
    public void mudarTelaDependentes(){
        Intent it_mudarTela = new Intent(getActivity(), Tela_DependenteRecycler.class);
        startActivity(it_mudarTela);
    }

    public void mudarTelaCadastroVacinas(){
        Intent it = new Intent(getActivity(), Tela_CadastroVacinas.class);
        startActivity(it);
    }

    public void mudarTelaCalendario(){
        Intent it = new Intent(getActivity(), Tela_EscolhasVacinas.class);
        startActivity(it);
    }
}