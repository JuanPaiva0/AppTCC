package com.example.apptcc.API;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apptcc.Model.VacinasAPI;
import com.example.apptcc.R;
import com.example.apptcc.databinding.ActivityTelaListaVacinasBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tela_ListaVacinas extends AppCompatActivity {
    private ActivityTelaListaVacinasBinding binding;
    private RecyclerView recycler;
    private Adapter_ListaVacina adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaListaVacinasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recycler = binding.recyclerNomeVacinas;


        Intent it = getIntent();

        if (it != null && it.hasExtra(Tela_EscolhasVacinas.CATEGORY_KEY)){
            String tabela = it.getStringExtra(Tela_EscolhasVacinas.CATEGORY_KEY);
            Log.d("getCategory", "Category: " + tabela);

            String apiUrl = "apivacina.azurewebsites.net/vacinas" + tabela;
            Log.d("getCategory", "Category: " + apiUrl);


            new FetchVacinasTask().execute(apiUrl);
        }
    }

    private class FetchVacinasTask extends AsyncTask<String, Void, List<Object>> {

        @Override
        protected List<Object> doInBackground(String... urls) {
            List<Object> vacinasList = new ArrayList<>();

            try {
                URL url = new URL("https://" + urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Configurar a conexão
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                // Conectar e obter a resposta
                int responseCode = urlConnection.getResponseCode();
                Log.d("FetchVacinasTask", "Response Code: " + responseCode);  // Adiciona este log
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String jsonResponse = convertInputStreamToString(inputStream);
                    Log.d("FetchVacinasTask", "JSON Response: " + jsonResponse);  // Adiciona este log

                    // Converte o JSON diretamente para uma lista de strings
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<String>>(){}.getType();
                    vacinasList = gson.fromJson(jsonResponse, listType);
                }

                // Fechar a conexão
                urlConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("FetchVacinasTask", "Erro ao realizar a solicitação HTTP: " + e.getMessage());  // Adiciona este log
            }

            return vacinasList;
        }

        @Override
        protected void onPostExecute(List<Object> vacinas) {
            if (vacinas != null) {
                List<VacinasAPI> list = new ArrayList<>();

                for (Object obj : vacinas){
                    if (obj instanceof String){
                        VacinasAPI vacinasAPI = new VacinasAPI();
                        vacinasAPI.setNome_vacina((String) obj);
                        list.add(vacinasAPI);
                    }
                }

                adapter = new Adapter_ListaVacina(list);
                recycler.setAdapter(adapter);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                recycler.setLayoutManager(layoutManager);
                adapter.notifyDataSetChanged();

                adapter.setOnItemClickListener(new Adapter_ListaVacina.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent it = getIntent();

                        if (it != null && it.hasExtra(Tela_EscolhasVacinas.CATEGORY_KEY)){
                            String tabela = it.getStringExtra(Tela_EscolhasVacinas.CATEGORY_KEY);
                            Log.d("getCategory", "Category: " + tabela);

                            int posicao = position + 1;
                            String apiUrl = "apivacina.azurewebsites.net/vacinas" + tabela + "/" + posicao;
                            Log.d("getCategory", "finalURL: " + apiUrl);

                            new FetchDetalhesVacinas().execute(apiUrl);
                        }
                    }
                });

            } else {
                Log.e("onPostExecute", "Lista de vacinas nula");
            }
        }

        private String convertInputStreamToString(InputStream inputStream) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("FetchVacinasTask", "Erro ao ler o InputStream: " + e.getMessage());  // Adiciona este log
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("FetchVacinasTask", "Erro ao fechar o InputStream: " + e.getMessage());  // Adiciona este log
                }
            }

            return stringBuilder.toString();
        }
    }

    private class FetchDetalhesVacinas extends AsyncTask<String, Void, VacinasAPI> {

        @Override
        protected VacinasAPI doInBackground(String... urls) {
            VacinasAPI vacina = null;

            try {
                URL url = new URL("https://" + urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Configurar a conexão
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                // Conectar e obter a resposta
                int responseCode = urlConnection.getResponseCode();
                Log.d("FetchVacinasTask", "Response Code: " + responseCode);  // Adiciona este log
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String jsonResponse = convertInputStreamToString(inputStream);
                    Log.d("FetchDetalhesVacinas", "JSON Response: " + jsonResponse);

                    // Converte o JSON diretamente para um objeto VacinasAPI
                    Gson gson = new Gson();
                    vacina = gson.fromJson(jsonResponse, VacinasAPI.class);
                }

                // Fechar a conexão
                urlConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("FetchVacinasTask", "Erro ao realizar a solicitação HTTP: " + e.getMessage());  // Adiciona este log
            }

            return vacina;
        }

        @Override
        protected void onPostExecute(VacinasAPI vacina) {
            if (vacina != null) {
                showPopup(vacina);
            } else {
                Log.e("onPostExecute", "Detalhes da vacina nulos");
            }
        }

        private String convertInputStreamToString(InputStream inputStream) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("FetchVacinasTask", "Erro ao ler o InputStream: " + e.getMessage());  // Adiciona este log
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("FetchVacinasTask", "Erro ao fechar o InputStream: " + e.getMessage());  // Adiciona este log
                }
            }

            return stringBuilder.toString();
        }

    }


    private void showPopup(VacinasAPI vacina) {
        Dialog popup = new Dialog(Tela_ListaVacinas.this);
        popup.setContentView(R.layout.popup_infos_vacinas);
        TextView btnVoltar = popup.findViewById(R.id.btn_popupVoltar);
        ImageView btnVolta = popup.findViewById(R.id.btn_popupVoltar1);

        if (vacina.getProtecao_contra() != null) {
            TextView nomeVacina = popup.findViewById(R.id.txtInfos_NomeVacina);
            TextView protecaoContra = popup.findViewById(R.id.txtInfos_ProtecaoContra);
            TextView composicao = popup.findViewById(R.id.txtInfos_Composicao);
            TextView numDoses = popup.findViewById(R.id.txtInfos_NumDoses);
            TextView idadeRec = popup.findViewById(R.id.txtInfos_IdadeRec);
            TextView intervaloDoses = popup.findViewById(R.id.txtInfos_IntervaloDoses);

            nomeVacina.setText("Nome: " + vacina.getNome_vacina());
            protecaoContra.setText("Proteção contra:\n " + vacina.getProtecao_contra());
            composicao.setText("Composição:\n " + vacina.getComposicao());
            numDoses.setText("Número de doses:\n " + vacina.getNum_doses());
            idadeRec.setText("Idade recomendada:\n " + vacina.getIdade_recomendada());
            intervaloDoses.setText("Intervalo entre doses:\n " + vacina.getIntervalo_doses());
        }

        popup.show();

        btnVoltar.setOnClickListener(view -> {
            popup.dismiss();
        });
        btnVolta.setOnClickListener(view1 -> {
            popup.dismiss();
        });
    }
}
