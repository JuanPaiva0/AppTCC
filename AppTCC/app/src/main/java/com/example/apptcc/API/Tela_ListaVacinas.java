package com.example.apptcc.API;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.apptcc.Model.VacinasAPI;
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

    private class FetchVacinasTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... urls) {
            List<String> vacinasList = new ArrayList<>();

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
        protected void onPostExecute(List<String> vacinas) {
            if (vacinas != null) {
                adapter = new Adapter_ListaVacina(vacinas);
                recycler.setAdapter(adapter);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                recycler.setLayoutManager(layoutManager);
                adapter.notifyDataSetChanged();
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
}
