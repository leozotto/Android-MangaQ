package com.example.mangaq.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mangaq.R;
import com.example.mangaq.activity.activity.ToolbarConfig;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {
    private EditText editNome, editSobrenome, editEndereco, editApelido;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        editNome = findViewById(R.id.editNome);
        editSobrenome = findViewById(R.id.editSobrenome);
        editApelido = findViewById(R.id.editApelido);
        editEndereco = findViewById(R.id.editEndereco);

        mAuth = FirebaseAuth.getInstance();

        //conf bottom navigation view
        ToolbarConfig toolbarConfig = new ToolbarConfig();
        toolbarConfig.configuraBottomNavigationView(PerfilActivity.this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        db = FirebaseFirestore.getInstance();
    }

    public void menuPrincipal(View view) {
        Intent menuPrincipal = new Intent(PerfilActivity.this, PerfilActivity.class);
        startActivity(menuPrincipal);
        finish();
    }

    public void salvarPerfil(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String nome = editNome.getText().toString();
        String sobrenome = editSobrenome.getText().toString();
        String apelido = editApelido.getText().toString();
        String endereço = editEndereco.getText().toString();

        Map<String,Object> dadosUsuario = new HashMap<>();

        dadosUsuario.put("nome",nome);
        dadosUsuario.put("sobrenome",sobrenome);
        dadosUsuario.put("apelido",apelido);
        dadosUsuario.put("endereço",endereço);

        db.collection("usuarios").document(user.getUid())
                .set(dadosUsuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String message = "Dados Cadastrados com Sucesso!";
                        Toast.makeText(PerfilActivity.this,message,Toast.LENGTH_SHORT).show();
                        menuPrincipal(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String message = "Falha ao Cadastrar Dados!";
                        Toast.makeText(PerfilActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
