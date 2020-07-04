package com.example.mangaq.activity.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mangaq.R;
import com.example.mangaq.activity.model.Usuario;
import com.example.mangaq.activity.util.IntentManager;
import com.example.mangaq.activity.util.ToolbarConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {
    private EditText editNome, editSobrenome, editEndereco, editApelido, editIdade;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private List<Usuario> listPessoa = new ArrayList<Usuario>();
    private ArrayAdapter<Usuario> arrayAdapterPessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        editNome = findViewById(R.id.editNome);
        editSobrenome = findViewById(R.id.editSobrenome);
        editApelido = findViewById(R.id.editApelido);
        editIdade = findViewById(R.id.editIdade);
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
        carregarDados(null);

    }

    public void salvarPerfil(View view) {
         FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        String nome = editNome.getText().toString();
        String sobrenome = editSobrenome.getText().toString();
        String apelido = editApelido.getText().toString();
        String idade = editIdade.getText().toString();
        String endereco = editEndereco.getText().toString();

        Map<String, Object> dadosUsuario = new HashMap<>();

        dadosUsuario.put("id",id);
        dadosUsuario.put("nome", nome);
        dadosUsuario.put("sobrenome", sobrenome);
        dadosUsuario.put("apelido", apelido);
        dadosUsuario.put("idade", idade);
        dadosUsuario.put("endereco", endereco);

        db.collection("usuarios").document(user.getUid())
                .set(dadosUsuario)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String message = "Dados Cadastrados com Sucesso!";
                        Toast.makeText(PerfilActivity.this, message, Toast.LENGTH_SHORT).show();
                        IntentManager.goTo(PerfilActivity.this, PerfilActivity.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String message = "Falha ao Cadastrar Dados!";
                        Toast.makeText(PerfilActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                });

        }

        public void carregarDados(View view){
            CollectionReference usuarios = db.collection("usuarios");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String id = user.getUid();

            usuarios.whereEqualTo("id",id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                List<Usuario> listUsuario = new ArrayList<>();
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    listUsuario.add(document.toObject(Usuario.class));
                                }
                                String nome ="";
                                String sobrenome ="";
                                String apelido ="";
                                String idade = "";
                                String endereco ="";

                                for(Usuario u : listUsuario){
                                    nome = u.getNome();
                                    sobrenome = u.getSobrenome();
                                    apelido = u.getApelido();
                                    idade = u.getIdade();
                                    endereco = u.getEndereco();
                                }
                                editNome.setText(nome);
                                editSobrenome.setText(sobrenome);
                                editApelido.setText(apelido);
                                editIdade.setText(idade);
                                editEndereco.setText(endereco);
                            }
                        }
                    });
        }
 }
