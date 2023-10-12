package com.example.apptcc.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptcc.Model.Vacinas;
import com.example.apptcc.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterVacinas extends FirestoreRecyclerAdapter<Vacinas, AdapterVacinas.MyViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterVacinas(@NonNull FirestoreRecyclerOptions<Vacinas> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Vacinas model) {
        holder.nome.setText(model.getNome());
        holder.lote.setText(model.getLote());
        holder.data.setText(model.getData());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_vacinas, parent, false);
        return new MyViewHolder(itemLista);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nome, lote, data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.txtListaNomeVacina);
            lote = itemView.findViewById(R.id.txtListaLoteVacina);
            data = itemView.findViewById(R.id.txtListaDataVacina);
        }
    }
}
