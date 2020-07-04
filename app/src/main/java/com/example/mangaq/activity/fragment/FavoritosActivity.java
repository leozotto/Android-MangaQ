package com.example.mangaq.activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaq.R;
import com.example.mangaq.activity.activity.Capitulos;
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

public class FavoritosActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private FirebaseFirestore firestore;
    FirebaseStorage storage;
    RecyclerView historyList;

    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        //conf de objetos
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        //conf bottom navigation view
        ToolbarConfig toolbarConfig = new ToolbarConfig();
        toolbarConfig.configuraBottomNavigationView(FavoritosActivity.this);

        historyList = findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        historyList.setLayoutManager(linearLayoutManager);
        getHistoryList();
    }

    private void getHistoryList() {
        Query query = firestore
                .collection("usuarios")
                .document(autenticacao.getCurrentUser().getUid())
                .collection("favoritos");

        FirestoreRecyclerOptions<History> response = new FirestoreRecyclerOptions.Builder<History>()
                .setQuery(query, History.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<History, HistoryHolder>(response) {
            @Override
            public void onBindViewHolder(@NonNull HistoryHolder holder, int position, @NonNull History h) {
                FirebaseFirestore.getInstance()
                        .collection("historias")
                        .document(h.getId())
                        .get()
                        .addOnSuccessListener(historyDocumentSnapshot -> {
                            History history = historyDocumentSnapshot.toObject(History.class);
                            holder.getTvNome().setText(history.getNome());

                            FirebaseFirestore
                                    .getInstance()
                                    .collection("usuarios")
                                    .document(history.getAutor().getId())
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        holder.getTvAutor().setText(documentSnapshot.getString("nome"));
                                    })
                                    .addOnFailureListener(e -> {
                                        holder.getTvAutor().setText(" - ");
                                        Toast.makeText(FavoritosActivity.this, "Erro ao carregar autor!", Toast.LENGTH_SHORT).show();
                                    });

                            holder.getDataCriacao().setText(history.getDataCriacaoFormatada());
                            ImageManager.carregarImagemFirestoreEmImageViewPorUrl(storage, history.getCapa(), holder.getImageView(), FavoritosActivity.this);

                            holder.itemView.setOnClickListener(v -> abreCapitulos(history));

                            holder.getIvNaoFavorito().setVisibility(View.GONE);
                            holder.getIvFavorito().setVisibility(View.VISIBLE);

                            holder.getIvFavorito().setOnTouchListener((click, event) -> {
                                unmarkAsFavorite(history.getId());
                                holder.getIvFavorito().setVisibility(View.GONE);
                                holder.getIvNaoFavorito().setVisibility(View.VISIBLE);
                                return true;
                        });
                });
            }

            @Override
            public HistoryHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.list_item, group, false);

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

    private void unmarkAsFavorite(String historyId) {
        FirebaseFirestore
                .getInstance()
                .collection("usuarios")
                .document(autenticacao.getCurrentUser().getUid())
                .collection("favoritos")
                .document(historyId)
                .delete()
                .addOnFailureListener(e -> {
                    Toast.makeText(FavoritosActivity.this, "Erro ao desmarcar como favorito!", Toast.LENGTH_LONG).show();
                    IntentManager.finish(FavoritosActivity.this);
                });
    }

    private void abreCapitulos(History history) {
        Bundle bundle = new Bundle();
        bundle.putString("historyId", history.getId());
        bundle.putString("historyName", history.getNome());

        IntentManager.goTo(FavoritosActivity.this, Capitulos.class, bundle, true);
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
