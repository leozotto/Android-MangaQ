package com.example.mangaq.activity.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaq.R;
import com.example.mangaq.activity.model.History;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryHolder extends RecyclerView.ViewHolder {
    private TextView tvNome;
    private TextView tvAutor;
    private TextView dataCriacao;
    private CircleImageView imageView;
    private ImageView ivFavorito;
    private ImageView ivNaoFavorito;

    public HistoryHolder(View itemView) {
        super(itemView);
        tvNome = itemView.findViewById(R.id.nome);
        tvAutor = itemView.findViewById(R.id.autor);
        imageView = itemView.findViewById(R.id.imagem);
        ivFavorito = itemView.findViewById(R.id.ivFavorito);
        ivNaoFavorito = itemView.findViewById(R.id.ivNaoFavorito);
        dataCriacao = itemView.findViewById(R.id.dataCriacao);
    }

    public TextView getTvNome() {
        return tvNome;
    }

    public void setTvNome(TextView tvNome) {
        this.tvNome = tvNome;
    }

    public TextView getTvAutor() {
        return tvAutor;
    }

    public void setTvAutor(TextView tvAutor) {
        this.tvAutor = tvAutor;
    }

    public TextView getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(TextView dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public CircleImageView getImageView() {
        return imageView;
    }

    public void setImageView(CircleImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getIvFavorito() {
        return ivFavorito;
    }

    public void setIvFavorito(ImageView ivFavorito) {
        this.ivFavorito = ivFavorito;
    }

    public ImageView getIvNaoFavorito() {
        return ivNaoFavorito;
    }

    public void setIvNaoFavorito(ImageView ivNaoFavorito) {
        this.ivNaoFavorito = ivNaoFavorito;
    }
}

