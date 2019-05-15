package com.tabembota.doaacao.model;

import java.util.Date;

public class Doacao{

    private String titulo;
    private String descricao;
    private int imagem;
    private String local;
    private String tipo;
    private String link;
    private Date data;

    public Doacao(String titulo, String descricao, int imagem, String local, String tipo, String link, Date data) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;
        this.local = local;
        this.tipo = tipo;
        this.link = link;
        this.data = data;
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

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
