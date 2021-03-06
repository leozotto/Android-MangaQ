package com.example.mangaq.activity.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Date;

@IgnoreExtraProperties
public class History {
    private String id;
    private String nome;
    private DocumentReference autor;
    private String capa;
    private Date dataCriacao;

    public History () { }

    public History(String id, String nome, DocumentReference autor, String capa, Date dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.capa = capa;
        this.dataCriacao = dataCriacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public DocumentReference getAutor() {
        return autor;
    }

    public void setAutor(DocumentReference autor) {
        this.autor = autor;
    }

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public String getDataCriacaoFormatada() {
        return new SimpleDateFormat("dd/MM/yyyy").format(dataCriacao);
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
