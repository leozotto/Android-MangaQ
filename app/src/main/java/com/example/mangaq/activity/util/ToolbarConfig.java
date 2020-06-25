package com.example.mangaq.activity.util;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.mangaq.R;
import com.example.mangaq.activity.activity.MainActivity;
import com.example.mangaq.activity.fragment.FavoritosActivity;
import com.example.mangaq.activity.fragment.PerfilActivity;
import com.example.mangaq.activity.fragment.PesquisaActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ToolbarConfig {

    public void configuraBottomNavigationView(Activity activity) {
        BottomNavigationViewEx bottomNavigationViewEx = activity.findViewById(R.id.bottomNavigation);
        //configuracao bottom nav(animacao)
//        bottomNavigationViewEx.enableAnimation(true);
//        bottomNavigationViewEx.enableItemShiftingMode(true);
//        bottomNavigationViewEx.enableShiftingMode(false);
//        bottomNavigationViewEx.setTextVisibility(true);

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

                switch (item.getItemId()) {
                    case R.id.ic_home:
                        IntentManager.goTo(activity, MainActivity.class);
                        return true;
                    case R.id.ic_pesquisa:
                        IntentManager.goTo(activity, PesquisaActivity.class);
                        return true;
                    case R.id.ic_favoritos:
                        IntentManager.goTo(activity, FavoritosActivity.class);
                        return true;
                    case R.id.ic_perfil:
                        IntentManager.goTo(activity, PerfilActivity.class);
                        return true;
                }
                return false;
            }
        });
    }
}
