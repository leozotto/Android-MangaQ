package com.example.mangaq.activity.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaq.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChapterHolder extends RecyclerView.ViewHolder {
    private TextView tvNome;
    private TextView tvAutor;
    private TextView dataCriacao;
    private CircleImageView imageView;

    public ChapterHolder(View view) {
        super(view);
        tvNome = view.findViewById(R.id.nome);
        tvAutor = view.findViewById(R.id.autor);
        imageView = view.findViewById(R.id.imagem);
        dataCriacao = view.findViewById(R.id.dataCriacao);
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
}
