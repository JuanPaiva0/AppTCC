package com.example.apptcc.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apptcc.Model.Usuario;
import com.example.apptcc.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class AdapterDependentes extends FirestoreRecyclerAdapter<Usuario, AdapterDependentes.MyViewHolder> {

    private FirebaseAuth auth;
    private Usuario dependente;

    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Usuario dependente);
    }

    public void onItemClick(int position) {
        if (position < getItemCount()) {
            Usuario dependente = getItem(position);
            if (dependente != null) {
                listener.onItemClick(dependente);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AdapterDependentes(@NonNull FirestoreRecyclerOptions<Usuario> options) {
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    Log.d("Adapter", "Clique no item da lista.");
                    listener.onItemClick(model); // Passa o dependente como argumento
                }
            }
        });

        Log.d("Adapter", "Dependente: " + nomeCompleto);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome, cpf;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomeDependente);
            cpf = itemView.findViewById(R.id.cpfDependente);
        }
    }
}
