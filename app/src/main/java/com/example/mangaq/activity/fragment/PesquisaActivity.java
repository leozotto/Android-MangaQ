package com.example.mangaq.activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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

import java.util.HashMap;
import java.util.Map;
import com.jakewharton.rxbinding4.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class PesquisaActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth autenticacao;
    private FirebaseStorage storage;

    private LinearLayoutManager linearLayoutManager;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView historyList;

    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        //conf de objetos
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        //conf bottom navigation view
        ToolbarConfig toolbarConfig = new ToolbarConfig();
        toolbarConfig.configuraBottomNavigationView(PesquisaActivity.this);

        historyList = findViewById(R.id.recyclerViewSearch);
        searchView = findViewById(R.id.searchView);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        historyList.setLayoutManager(linearLayoutManager);

        RxSearchView.queryTextChanges(searchView)
                .debounce(750, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    searchHistoryList(charSequence);
                });
    }

    private void searchHistoryList(CharSequence charSequence) {
        String search = charSequence.toString().toUpperCase();

        if (adapter == null) {
            loadAdapter(search);
        } else {
            reloadAdapter(search);
        }
    }

    private void loadAdapter(String search) {
        Query query = firestore
                .collection("historias")
                .orderBy("NOME")
                .limitToLast(20);

        FirestoreRecyclerOptions<History> response = new FirestoreRecyclerOptions.Builder<History>()
                .setQuery(query, History.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<History, HistoryHolder>(response) {
            @Override
            public void onBindViewHolder(@NonNull HistoryHolder holder, int position, @NonNull History historia) {
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

                holder.itemView.setOnClickListener(v -> abreCapitulos(historia));

                FirebaseFirestore
                        .getInstance()
                        .collection("usuarios")
                        .document(autenticacao.getCurrentUser().getUid())
                        .collection("favoritos")
                        .document(historia.getId())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.getData() != null) {
                                holder.getIvNaoFavorito().setVisibility(View.GONE);
                                holder.getIvFavorito().setVisibility(View.VISIBLE);
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(), "Falha ao carregar dados", Toast.LENGTH_SHORT).show();
                        });

                holder.getIvFavorito().setOnTouchListener((click, event) -> {
                    unmarkAsFavorite(historia.getId());
                    holder.getIvFavorito().setVisibility(View.GONE);
                    holder.getIvNaoFavorito().setVisibility(View.VISIBLE);
                    return true;
                });

                holder.getIvNaoFavorito().setOnTouchListener((click, event) -> {
                    markAsFavorite(historia.getId());
                    holder.getIvNaoFavorito().setVisibility(View.GONE);
                    holder.getIvFavorito().setVisibility(View.VISIBLE);
                    return true;
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

        adapter.startListening();
    }

    private void reloadAdapter(String search) {
        Query query = firestore
                .collection("historias")
                .orderBy("NOME")
                .startAt(search)
                .endAt(search + "\uf8ff")
                .limitToLast(20);

        FirestoreRecyclerOptions<History> response = new FirestoreRecyclerOptions.Builder<History>()
                .setQuery(query, History.class)
                .build();

        // Change options of adapter.
        adapter.updateOptions(response);
    }

    private void markAsFavorite(String historyId) {
        Map<String, Object> historyDocument = new HashMap<>();
        historyDocument.put("id", historyId);

        FirebaseFirestore
                .getInstance()
                .collection("usuarios")
                .document(autenticacao.getCurrentUser().getUid())
                .collection("favoritos")
                .document(historyId)
                .set(historyDocument)
                .addOnFailureListener(e -> {
                    Toast.makeText(PesquisaActivity.this, "Erro ao marcar como favorito!", Toast.LENGTH_LONG).show();
                    IntentManager.finish(PesquisaActivity.this);
                });
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
                    Toast.makeText(PesquisaActivity.this, "Erro ao desmarcar como favorito!", Toast.LENGTH_LONG).show();
                    IntentManager.finish(PesquisaActivity.this);
                });
    }

    private void abreCapitulos(History historia) {
        Bundle bundle = new Bundle();
        bundle.putString("historyId", historia.getId());
        bundle.putString("historyName", historia.getNome());

        IntentManager.goTo(PesquisaActivity.this, Capitulos.class, bundle, true);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
