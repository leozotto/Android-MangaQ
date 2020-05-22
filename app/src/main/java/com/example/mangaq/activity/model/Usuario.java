package com.example.mangaq.activity.model;

public class Usuario {
    private String id;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private String apelido;
    private String endereco;


    public Usuario() {

    }
    public Usuario( String nome, String sobrenome, String apelido, String endereco) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.apelido = apelido;
        this.endereco = endereco;
    }


    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }


    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


}
