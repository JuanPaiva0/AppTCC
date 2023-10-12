package com.example.apptcc.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptcc.Model.Vacinas;
import com.example.apptcc.R;
import com.example.apptcc.databinding.FragmentVacinasBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VacinasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VacinasFragment extends Fragment {

    private FragmentVacinasBinding binding;
    private RecyclerView recyclerView;
    private AdapterVacinas adapterVacinas;
    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VacinasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VacinasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VacinasFragment newInstance(String param1, String param2) {
        VacinasFragment fragment = new VacinasFragment();
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
        binding = FragmentVacinasBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        setupRecycler();

        return view;
    }

    public void setupRecycler(){
        CollectionReference ref = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("vacinas");
        Query query = ref.orderBy("nome");

        FirestoreRecyclerOptions<Vacinas> options = new FirestoreRecyclerOptions.Builder<Vacinas>()
                .setQuery(query, Vacinas.class)
                .build();

        recyclerView = binding.recyclerVacinas;

        adapterVacinas = new AdapterVacinas(options);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterVacinas);

        adapterVacinas.startListening();
    }
}