package com.example.mangaq.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mangaq.R;
import com.example.mangaq.activity.holder.HistoryHolder;
import com.example.mangaq.activity.model.History;
import com.example.mangaq.activity.util.ImageManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class Capitulos extends AppCompatActivity {
    private String historyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitulos);

        Intent intent = getIntent();
        historyId = intent.getStringExtra("historyId");
    }
    private void buscarListaHistorias() {
        Query query = firestore.collection("historias");

        FirestoreRecyclerOptions<History> response = new FirestoreRecyclerOptions.Builder<History>()
                .setQuery(query, History.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<History, HistoryHolder>(response) {
            @Override
            public void onBindViewHolder(@NonNull HistoryHolder holder, int position, @NonNull History historia) {
                holder.getTvNome().setText(historia.getNome());
                holder.getTvAutor().setText(historia.getAutor().toString());
                holder.getDataCriacao().setText(historia.getDataCriacaoFormatada());
                ImageManager.carregarImagemFirestoreEmImageViewPorUrl(storage, historia.getCapa(), holder.getImageView(), MainActivity.this);

                holder.itemView.setOnClickListener(v -> abreCapitulos(historia));
            }

            @Override
            public HistoryHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.list_item, group, false);

                return new HistoryHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        historyList.setAdapter(adapter);
    }
}