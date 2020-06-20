package com.example.mangaq.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mangaq.R;
import com.example.mangaq.activity.fragment.FavoritosActivity;
import com.example.mangaq.activity.fragment.HomeActivity;
import com.example.mangaq.activity.fragment.PerfilActivity;
import com.example.mangaq.activity.fragment.PesquisaActivity;
import com.example.mangaq.activity.helper.ConfiguracaoFirebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //conf toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("MangáQ");
        setSupportActionBar(toolbar);

        //conf de objetos
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //conf bottom navigation view
        configuraBottomNavigationView();
    }

    private void configuraBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        //navegacao
        habilitarNavigation(bottomNavigationViewEx);

        //configura item selecionado
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    public void habilitarNavigation(final BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        irParaTela(HomeActivity.class);
                        break;
                    case R.id.ic_pesquisa:
                        irParaTela(PesquisaActivity.class);
                        break;
                    case R.id.ic_favoritos:
                        irParaTela(FavoritosActivity.class);
                        break;
                    case R.id.ic_perfil:
                        irParaTela(PerfilActivity.class);
                        break;
                }
                return false;
            }

            private void irParaTela (Class activityClass) {
                // startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                MainActivity.this.startActivity(new Intent(getApplicationContext(), activityClass));
                MainActivity.this.finish();
            }
        });

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
}
