package com.example.mangaq.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.example.mangaq.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class PesquisaFragment extends Fragment {

    private ProgressBar progressBar;
    private CircleImageView imageView;
    public GridView gridViewPerfil;

    public PesquisaFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_pesquisa, container, false);
    }


}
