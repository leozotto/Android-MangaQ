package com.example.mangaq.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaq.R;
import com.example.mangaq.activity.helper.ConfiguracaoFirebase;
import com.example.mangaq.activity.holder.HistoryHolder;
import com.example.mangaq.activity.model.History;
import com.example.mangaq.activity.util.ImageManager;
import com.example.mangaq.activity.util.IntentManager;
import com.example.mangaq.activity.util.ToolbarConfig;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.concurrent.atomic.AtomicReference;

public class PesquisaActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private FirebaseFirestore firestore;
    EditText listPesquisa;

    FirebaseStorage storage;
    RecyclerView historyList;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);
        listPesquisa = findViewById(R.id.listPesquisa);

        //conf de objetos
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        //conf bottom navigation view
        ToolbarConfig toolbarConfig = new ToolbarConfig();
        toolbarConfig.configuraBottomNavigationView(PesquisaActivity.this);

        historyList = findViewById(R.id.historyList);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        historyList.setLayoutManager(linearLayoutManager);
        eventoEdit();
        buscarListaHistorias("O");
    }


    private void eventoEdit() {
        listPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String palavra = listPesquisa.getText().toString().trim();
                buscarListaHistorias(palavra);
            }

        });
    }

    private void buscarListaHistorias(String palavra) {
        boolean reloadAdapter = adapter!=null;
        Query query = firestore.collection("historias").orderBy("nome").startAt("!").endAt("SUBSTRING\uf8ff");

        FirestoreRecyclerOptions<History> response = new FirestoreRecyclerOptions.Builder<History>()
                .setQuery(query, History.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<History, HistoryHolder>(response) {

            @Override
            public void onBindViewHolder(@NonNull HistoryHolder holder, int position, @NonNull History historia) {
                System.out.println("AQUI");
                holder.getTvNome().setText(historia.getNome());

                FirebaseFirestore
                        .getInstance()
                        .collection("usuarios")
                        .document(historia.getAutor().getId())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            holder.getTvAutor().setText(documentSnapshot.getString("nome"));
                        })
                        .addOnFailureListener(e -> {
                            holder.getTvAutor().setText(" - ");
                            Toast.makeText(PesquisaActivity.this, "Erro ao carregar autor!", Toast.LENGTH_SHORT).show();
                        });

                holder.getDataCriacao().setText(historia.getDataCriacaoFormatada());
                ImageManager.carregarImagemFirestoreEmImageViewPorUrl(storage, historia.getCapa(), holder.getImageView(), PesquisaActivity.this);

            }

            @Override
            public HistoryHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.list_item, group, false);
                System.out.println("oncreateviewholder");

                return new HistoryHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };



        if(reloadAdapter){
            historyList.setVisibility(View.GONE);
            historyList.setAdapter(null);
            historyList.setLayoutManager(null);
            historyList.setAdapter(adapter);
            historyList.setLayoutManager(linearLayoutManager);
            historyList.getLayoutManager().removeAllViews();
            historyList.getRecycledViewPool().clear();
            adapter.notifyDataSetChanged();
//            historyList.swapAdapter(adapter,false);
//            historyList.setLayoutManager(linearLayoutManager);
//            adapter.notifyDataSetChanged();
            historyList.setVisibility(View.VISIBLE);
            System.out.println("entrou no if");
        }else{
            adapter.notifyDataSetChanged();
            historyList.setAdapter(adapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }

}
