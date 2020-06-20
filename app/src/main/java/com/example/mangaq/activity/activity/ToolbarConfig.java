package com.example.mangaq.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.mangaq.R;
import com.example.mangaq.activity.fragment.FavoritosActivity;
import com.example.mangaq.activity.fragment.PerfilActivity;
import com.example.mangaq.activity.fragment.PesquisaActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ToolbarConfig {
    private static long selected;

    public void configuraBottomNavigationView(Activity activity) {
        BottomNavigationViewEx bottomNavigationViewEx = activity.findViewById(R.id.bottomNavigation);
        //navegacao
        habilitarNavigation(activity, bottomNavigationViewEx);

        //configura item selecionado
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    private void habilitarNavigation(final Activity activity, BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (selected == item.getItemId()) {
                    return false;
                }
                selected = item.getItemId();

                switch (item.getItemId()) {
                    case R.id.ic_home:
                        irParaTela(activity, MainActivity.class);
                        break;
                    case R.id.ic_pesquisa:
                        irParaTela(activity, PesquisaActivity.class);
                        break;
                    case R.id.ic_favoritos:
                        irParaTela(activity, FavoritosActivity.class);
                        break;
                    case R.id.ic_perfil:
                        irParaTela(activity, PerfilActivity.class);
                        break;
                }
                return false;
            }

            private void irParaTela(Activity activity, Class targetActivityClass) {
                Intent intent = new Intent(activity, targetActivityClass);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }
}
