package com.example.mangaq.activity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mangaq.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void abrirCadastro(View view) {
        Intent i = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(i);
    }
    public void abrirHome(View view) {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

}
