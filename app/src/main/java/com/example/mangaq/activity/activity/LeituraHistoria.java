package com.example.mangaq.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mangaq.R;
import com.example.mangaq.activity.util.ImageManager;
import com.example.mangaq.activity.util.IntentManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeituraHistoria extends AppCompatActivity {
    // Firebase
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;

    // Components
    Button backPage, nextPage, goBackToHome;
    ImageView ivCurrentview;
    TextView titleChapter, tvCurrentPage, tvTheEnd;

    // History data
    private String historyId;
    private String historyName;
    private String chapterId;
    private String chaptername;
    private String initialChapterGroup;

    // Page data
    private ArrayList<String> pages;
    private Map<String, Object> escolha;
    private boolean stateOpacityMin;
    private int currentPage;

    // Others
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitura_historia);

        // Load firestore instances
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Bind Components
        titleChapter = findViewById(R.id.titleChapter);
        backPage = findViewById(R.id.backPage);
        ivCurrentview = findViewById(R.id.ivCurrentview);
        nextPage = findViewById(R.id.nextPage);
        tvCurrentPage = findViewById(R.id.tvCurrentPage);
        tvTheEnd = findViewById(R.id.tvTheEnd);
        goBackToHome = findViewById(R.id.goBackToHome);

        // Get intent data
        Intent intent = getIntent();
        historyId = intent.getBundleExtra("bundleExtra").getString("historyId");
        historyName = intent.getBundleExtra("bundleExtra").getString("historyName");
        chapterId = intent.getBundleExtra("bundleExtra").getString("chapterId");
        chaptername = intent.getBundleExtra("bundleExtra").getString("chapterName");
        initialChapterGroup = intent.getBundleExtra("bundleExtra").getString("initialChapterGroup");

        // Initialize State Data
        stateOpacityMin = false;
        currentPage = 0;
        pages = new ArrayList<>();
        escolha = null;
        titleChapter.setText(historyName + ": " + chaptername);

        // Get history from historic reads
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        documentReference = firestore.document("usuarios/" + currentUser.getUid() + "/historicoAtividade/" + historyId + "_" + chapterId);

        getHistoryActivities();

        backPage.setVisibility(View.GONE);

        // Listeners
        ivCurrentview.setOnTouchListener((click, event) -> {
            toggleOpacity();
            return false;
        });
    }

    // load data
    private void getHistoryActivities() {
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    String historicOfReadKey = historyId + "_" + chapterId;

                    if (documentSnapshot.get("id") == null) {
                        createHistoric(historicOfReadKey);
                        getHistoryActivities();
                    } else {
                        ((List<String>) documentSnapshot.get("grupos"))
                                .forEach(grupo -> {
                                    getPages(grupo);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LeituraHistoria.this, "Erro ao carregar informações do usuário!", Toast.LENGTH_SHORT).show();
                });
    }

    private void createHistoric(String historicOfReadKey) {
        Map<String, Object> historicOfRead = new HashMap<>();
        historicOfRead.put("id", historicOfReadKey);
        historicOfRead.put("grupos", Arrays.asList(initialChapterGroup));

        documentReference.set(historicOfRead)
                .addOnFailureListener(e -> {
                    Toast.makeText(LeituraHistoria.this, "Erro ao salvar historico de leitura!", Toast.LENGTH_LONG).show();
                    IntentManager.finish(LeituraHistoria.this);
                });
    }

    private void getPages(String grupo) {
        firestore.document(grupo).get()
                .addOnSuccessListener(documentSnapshot -> {
                    ((List<String>) documentSnapshot.get("paginas")).forEach(pagina -> {
                        pages.add(pagina);
                    });

                    if (documentSnapshot.get("temEscolha") != null) {
                        escolha = (Map<String, Object>) documentSnapshot.get("escolha");
                    }

                    if (pages.size() > 0) {
                        tvCurrentPage.setText(1 + "/" + pages.size());
                        ImageManager.carregarImagemFirestoreEmImageViewPorUrl(storage, pages.get(0), ivCurrentview, LeituraHistoria.this);
                    } else {
                        nextPage.setVisibility(View.GONE);
                        openEndOfChapter();
                    }

                    String nextGroup = documentSnapshot.getString("proximoGrupo");
                    if (nextGroup != null) {
                        getPages(nextGroup);
                    }

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LeituraHistoria.this, "Erro ao buscar grupos!", Toast.LENGTH_LONG).show();
                });
    }

    // Content Managers
    private void changeState () {
        if (!hasNextPage() && hasPendingQuest()) {

        } else if (!hasNextPage()) {
            openEndOfChapter();
        } else {
            if (tvTheEnd.getVisibility() == View.VISIBLE) {
                tvTheEnd.setVisibility(View.GONE);
            }
            if (ivCurrentview.getVisibility() == View.GONE) {
                ivCurrentview.setVisibility(View.VISIBLE);
            }
        }
    }

    private void openEndOfChapter() {
        ivCurrentview.setVisibility(View.GONE);
        nextPage.setVisibility(View.GONE);
        tvTheEnd.setVisibility(View.VISIBLE);
    }

    public void selecionarOpcao(View view) {
        System.out.println("oi");
        // TODO: adicionar grupoAlvo no historico de leitura
        // TODO: Atualizar hasQuest
        // TODO: carregar grupoAlvo na lista de paginas
    }

    // State Helpers
    private boolean hasNextPage () {
        return currentPage != pages.size() - 1;
    }

    private boolean hasPreviusPage () {
        return currentPage > 0;
    }

    private boolean hasPendingQuest () {
        return escolha != null;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        tvCurrentPage.setText((currentPage + 1) + "/" + pages.size());
    }

    // Called from activity
    public void goToNextPage(View view) {
        if (backPage.getVisibility() == View.GONE) {
            backPage.setVisibility(View.VISIBLE);
        }
        if (currentPage == pages.size()) {
            nextPage.setVisibility(View.GONE);
        }

        changeState();
        if (!hasNextPage()) return;
        setCurrentPage(currentPage + 1);
        ImageManager.carregarImagemFirestoreEmImageViewPorUrl(storage, pages.get(currentPage), ivCurrentview, LeituraHistoria.this);
    }

    public void goToPreviusPage(View view) {
        if (nextPage.getVisibility() == View.GONE) {
            nextPage.setVisibility(View.VISIBLE);
        }
        if (currentPage == 1) {
            backPage.setVisibility(View.GONE);
        }

        changeState();
        if (!hasPreviusPage()) return;
        setCurrentPage(currentPage - 1);
        ImageManager.carregarImagemFirestoreEmImageViewPorUrl(storage, pages.get(currentPage), ivCurrentview, LeituraHistoria.this);
    }

    public void setGoBackToChapters(View view) {
        IntentManager.finish(LeituraHistoria.this);
    }

    public void toggleOpacity() {
        tvCurrentPage.setAlpha(stateOpacityMin ? 0.3f : 1f);
        titleChapter.setAlpha(stateOpacityMin ? 0.3f : 1f);
        backPage.setAlpha(stateOpacityMin ? 0.3f : 1f);
        nextPage.setAlpha(stateOpacityMin ? 0.3f : 1f);
        goBackToHome.setAlpha(stateOpacityMin ? 0.3f : 1f);

        stateOpacityMin = !stateOpacityMin;
    }
}