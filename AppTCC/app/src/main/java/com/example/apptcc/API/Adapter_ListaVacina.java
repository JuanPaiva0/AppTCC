package com.example.apptcc.API;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptcc.Model.VacinasAPI;
import com.example.apptcc.R;

import java.util.List;

public class Adapter_ListaVacina extends RecyclerView.Adapter<Adapter_ListaVacina.MyViewHolder> {

    private List<String> vacinasList;

    public Adapter_ListaVacina(List<String> vacinasList) {
        this.vacinasList = vacinasList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_nome_vacinas, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String nomeVacina = vacinasList.get(position);
        holder.nomeVacina.setText(nomeVacina);
    }

    @Override
    public int getItemCount() {
        return vacinasList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeVacina;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeVacina = itemView.findViewById(R.id.txtNomeVacina);
        }
    }
}