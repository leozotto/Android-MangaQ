package com.example.mangaq.activity.fragment;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mangaq.R;
import com.example.mangaq.activity.util.ToolbarConfig;
import com.google.firebase.firestore.FirebaseFirestore;

public class FavoritosActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);



        //conf bottom navigation view
        ToolbarConfig toolbarConfig = new ToolbarConfig();
        toolbarConfig.configuraBottomNavigationView(FavoritosActivity.this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        db = FirebaseFirestore.getInstance();
    }
}
