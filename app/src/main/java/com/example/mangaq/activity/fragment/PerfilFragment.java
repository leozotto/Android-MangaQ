package com.example.mangaq.activity.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.mangaq.R;
import com.example.mangaq.activity.activity.CadastroActivity;
import com.example.mangaq.activity.activity.LoginActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class PerfilFragment extends Fragment {

    private ProgressBar progressBar;
    private CircleImageView imageView;
    public GridView gridViewPerfil;
    
    public PerfilFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }
  


}
