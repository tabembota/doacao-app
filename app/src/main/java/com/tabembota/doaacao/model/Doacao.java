package com.tabembota.doaacao.model;


import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;

public class Doacao implements Comparable<Doacao>, Serializable {

    private String user_id;
    private String op_id;
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

    public String getOp_id() {
        return op_id;
    }

    public void setOp_id(String op_id) {
        this.op_id = op_id;
    }

    @Exclude
    @Override
    public int compareTo(Doacao o) {
        if(this.getData() > o.getData())
            return 1;
        else if (this.getData() == o.getData())
            return 0;
        return -1;
    }

    @Exclude
    public boolean igual_a(Doacao teste){
        return (
               this.getTitulo().equals(    teste.getTitulo()    )
            && this.getOp_id().equals(     teste.getOp_id()     )
            && this.getDescricao().equals( teste.getDescricao() )
            && this.getUser_id().equals(   teste.getUser_id()   )
            && this.getEmail().equals(     teste.getEmail()     )
            && this.getImagem()   ==       teste.getImagem()
            && this.getData()     ==       teste.getData()
            && this.getFiltro()   ==       teste.getFiltro()
        );
    }
}
