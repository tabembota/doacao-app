package com.tabembota.doaacao.model;

import java.util.Date;

public class Doacao{

    private String user_id;
    private String titulo;
    private String descricao;
    private String email;
    private int imagem;
    private int filtro;

    public Doacao(String user_id, String titulo, String descricao, String email, int imagem, int filtro) {
        this.user_id = user_id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.email = email;
        this.imagem = imagem;
        this.filtro = filtro;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public int getFiltro() {
        return filtro;
    }

    public void setFiltro(int filtro) {
        this.filtro = filtro;
    }
}
