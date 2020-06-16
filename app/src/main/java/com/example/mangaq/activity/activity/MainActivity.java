package com.example.mangaq.activity.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.viewPager,new FeedFragment()).commit();


    }

    private void configuraBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        //navegacao
        habilitarNavegation(bottomNavigationViewEx);

        //configura item selecionado
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

    }
    public static void habilitarNavegation(final Context context, final BottomNavigationViewEx viewEx){
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.ic_home:
                        viewEx.setSelectedItemId(R.id.ic_home);
                        Intent intent = new Intent(context, HomeActivity.class);
                        context.startActivity(intent);
                        break;
                    case R.id.ic_pesquisa:
                        Intent intent1 = new Intent(context, PesquisaActivity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_favoritos:
                        Intent intent2 = new Intent(context, FavoritosActivity.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_perfil:
                        Intent intent3 = new Intent(context, PerfilActivity.class);
                        context.startActivity(intent3);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu );

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

       switch(item.getItemId()){
            case R.id.menu_sair :
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    private void deslogarUsuario(){
        try {
            autenticacao.signOut();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
