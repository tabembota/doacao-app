package com.tabembota.doaacao.model;


import com.google.firebase.database.Exclude;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Doacao implements Comparable<Doacao>{

    private String user_id;
    private String titulo;
    private String descricao;
    private String email;
    private int imagem;
    private int filtro;
    private long data;



    public Doacao(String user_id, String titulo, String descricao, String email, int imagem, int filtro) {
        this.user_id = user_id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.email = email;
        this.imagem = imagem;
        this.filtro = filtro;
        this.data = new Date().getTime();
    }

    public Doacao(){}

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

    public long getData(){
        return data;
    }

    public void setData(long data){
        this.data = data;
    }


    @Override
    public int compareTo(Doacao o) {
        if(this.getData() > o.getData())
            return 1;
        else if (this.getData() == o.getData())
            return 0;
        return -1;
    }
}
