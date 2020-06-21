package com.example.mangaq.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mangaq.R;

import java.util.ArrayList;

public class LeituraHistoria extends AppCompatActivity {
    private Button goToPreviousPage, goToNextPage;
    private ArrayList<String > pages;
    private int currentPage = 0;
    private String historyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitura_historia);

        Intent intent = getIntent();
        historyId = intent.getStringExtra("historyId");

        // this.goToPreviousPage = findViewById(R.id.);
        // this.goToNextPage = findViewById(R.id.);

        // loadBlockImageIntoPageList();
    }
}