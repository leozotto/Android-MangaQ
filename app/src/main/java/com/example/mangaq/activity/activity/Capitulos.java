package com.example.mangaq.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaq.R;
import com.example.mangaq.activity.holder.ChapterHolder;
import com.example.mangaq.activity.model.Chapter;
import com.example.mangaq.activity.util.ImageManager;
import com.example.mangaq.activity.util.IntentManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

public class Capitulos extends AppCompatActivity {
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private RecyclerView chapterList;
    private LinearLayoutManager linearLayoutManager;
    private TextView historyName;

    private String historyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitulos);

        // Load firestore instances
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Bind Components
        chapterList = findViewById(R.id.recyclerView);
        historyName = findViewById(R.id.historyName);

        // Get intent data
        Intent intent = getIntent();
        historyId = intent.getBundleExtra("bundleExtra").getString("historyId");
        historyName.setText(intent.getBundleExtra("bundleExtra").getString("historyName"));

        // Get histories
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        chapterList.setLayoutManager(linearLayoutManager);
        buscarListaCapitulosHistoria();
    }

    private void buscarListaCapitulosHistoria() {
        Query query = firestore
                .collection("historias")
                .document(historyId)
                .collection("capitulos");

        FirestoreRecyclerOptions<Chapter> response = new FirestoreRecyclerOptions.Builder<Chapter>()
                .setQuery(query, Chapter.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Chapter, ChapterHolder>(response) {
            @Override
            public void onBindViewHolder(@NonNull ChapterHolder holder, int position, @NonNull Chapter capitulo) {
                holder.getTvNome().setText(capitulo.getNome());
                holder.getDataCriacao().setText(capitulo.getDataCriacaoFormatada());
                ImageManager.carregarImagemFirestoreEmImageViewPorUrl(storage, capitulo.getCapa(), holder.getImageView(), Capitulos.this);

                holder.itemView.setOnClickListener(v -> abreLeitura(capitulo));
            }

            @Override
            public ChapterHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.list_item_capitulo, group, false);

                return new ChapterHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        chapterList.setAdapter(adapter);
    }

    private void abreLeitura(Chapter capitulo) {
        Bundle bundle = new Bundle();
        bundle.putString("chapterId", capitulo.getId());

        IntentManager.goTo(Capitulos.this, LeituraHistoria.class, bundle,true);
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

    public void voltarParaHome(View view) {
        IntentManager.finish(Capitulos.this);
    }
}