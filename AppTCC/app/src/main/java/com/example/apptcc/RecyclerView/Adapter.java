package com.example.apptcc.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptcc.Model.Usuario;
import com.example.apptcc.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private List<Usuario> dependentesList;

    public Adapter(List<Usuario> dependentesList){
        this.dependentesList = dependentesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itensLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_dependentes, parent, false);

        return new MyViewHolder(itensLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario dependente = dependentesList.get(position);

        holder.nome.setText(dependente.getNome() + " " + dependente.getSobrenome());
        holder.cpf.setText(dependente.getCpf());

    }

    @Override
    public int getItemCount() {

        return dependentesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nome;
        TextView cpf;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.nomeDependente);
            cpf = itemView.findViewById(R.id.cpfDependente);
        }
    }
}
