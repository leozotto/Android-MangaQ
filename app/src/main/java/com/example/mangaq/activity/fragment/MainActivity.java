package com.example.mangaq.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaq.R;
import com.example.mangaq.activity.activity.Capitulos;
import com.example.mangaq.activity.activity.LoginActivity;
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

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private FirebaseFirestore firestore;
    FirebaseStorage storage;
    RecyclerView historyList;

    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //conf toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("MangáQ");
        setSupportActionBar(toolbar);

        //conf de objetos
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        //conf bottom navigation view
        ToolbarConfig toolbarConfig = new ToolbarConfig();
        toolbarConfig.configuraBottomNavigationView(MainActivity.this);

        historyList = findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        historyList.setLayoutManager(linearLayoutManager);
        getHistoryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sair) {
            deslogarUsuario();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getHistoryList() {
        Query query = firestore.collection("historias");

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
                            Toast.makeText(MainActivity.this, "Erro ao carregar autor!", Toast.LENGTH_SHORT).show();
                        });

                holder.getDataCriacao().setText(historia.getDataCriacaoFormatada());
                ImageManager.carregarImagemFirestoreEmImageViewPorUrl(storage, historia.getCapa(), holder.getImageView(), MainActivity.this);

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
                    Toast.makeText(MainActivity.this, "Erro ao marcar como favorito!", Toast.LENGTH_LONG).show();
                    IntentManager.finish(MainActivity.this);
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
                    Toast.makeText(MainActivity.this, "Erro ao desmarcar como favorito!", Toast.LENGTH_LONG).show();
                    IntentManager.finish(MainActivity.this);
                });
    }

    private void abreCapitulos(History historia) {
        Bundle bundle = new Bundle();
        bundle.putString("historyId", historia.getId());
        bundle.putString("historyName", historia.getNome());

        IntentManager.goTo(MainActivity.this, Capitulos.class, bundle, true);
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
