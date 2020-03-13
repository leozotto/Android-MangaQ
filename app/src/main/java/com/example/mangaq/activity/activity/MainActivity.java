package com.example.mangaq.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mangaq.R;
import com.example.mangaq.activity.helper.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

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
        //autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu );

        return super.onCreateOptionsMenu(menu);
    }
    //@Override
  //  public boolean onOptionsItemSelected(MenuItem item){

//        switch(item.getItemId()){
//            case R.id.menu_sair :
//                deslogarUsuario();
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                break;

        //}
      //  return super.onOptionsItemSelected(item);
    }
//    private void deslogarUsuario(){
//        try {
//            autenticacao.signOut();
//        }catch(Exception e){
//            e.printStackTrace();
//        }

    //}
//}
