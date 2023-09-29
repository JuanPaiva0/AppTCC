package com.example.apptcc.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptcc.Model.Dependente;
import com.example.apptcc.Model.Usuario;
import com.example.apptcc.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class Adapter extends FirestoreRecyclerAdapter<Usuario, Adapter.MyViewHolder> {

    public Adapter(@NonNull FirestoreRecyclerOptions<Usuario> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_dependentes, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Usuario model) {
        String nomeCompleto = model.getNome() + " " + model.getSobrenome();
        holder.nome.setText(nomeCompleto);
        holder.cpf.setText(model.getCpf());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nome, cpf;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomeDependente);
            cpf = itemView.findViewById(R.id.cpfDependente);
        }
    }
}