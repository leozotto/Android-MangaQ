package com.example.mangaq.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

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
        buscarListaHistorias();
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

    private void buscarListaHistorias() {
        Query query = firestore.collection("historias");

        FirestoreRecyclerOptions<History> response = new FirestoreRecyclerOptions.Builder<History>()
                .setQuery(query, History.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<History, HistoryHolder>(response) {
            @Override
            public void onBindViewHolder(@NonNull HistoryHolder holder, int position, @NonNull History model) {
                holder.getTvNome().setText(model.getNome());
                holder.getTvAutor().setText(model.getAutor().toString());
                holder.getDataCriacao().setText(model.getDataCriacaoFormatada());
                MainActivity.this.carregarCapaHistoria(model.getCapa(), holder.getImageView());

                holder.itemView.setOnClickListener(v -> {
                    Snackbar.make(historyList, model.getNome() + ", " + model.getAutor().toString() + " at ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                });
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

    private void carregarCapaHistoria(String url, ImageView imageView) {
        StorageReference storageReference = storage.getReferenceFromUrl(url);
        storageReference.getDownloadUrl().addOnSuccessListener(
                uri -> Picasso.get().load(uri).into(imageView)
        ).addOnFailureListener(
                e -> Toast.makeText(MainActivity.this, "Erro ao carregar imagem!", Toast.LENGTH_SHORT).show()
        );
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
