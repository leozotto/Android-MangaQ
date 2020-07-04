package com.example.mangaq.activity.util;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mangaq.R;
import com.example.mangaq.activity.fragment.MainActivity;
import com.example.mangaq.activity.fragment.FavoritosActivity;
import com.example.mangaq.activity.fragment.PerfilActivity;
import com.example.mangaq.activity.fragment.PesquisaActivity;
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
        viewEx.setOnNavigationItemSelectedListener(item -> {
            if (selected == item.getItemId()) {
                return false;
            }
            selected = item.getItemId();

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
        });
    }
}
