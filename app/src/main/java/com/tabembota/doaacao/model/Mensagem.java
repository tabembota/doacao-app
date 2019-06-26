package com.tabembota.doaacao.model;

import com.google.firebase.database.Exclude;

public class Mensagem {

    private String idUsuario;
    private String mensagem;

    public Mensagem() {

    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Exclude
    @Override
    public boolean equals(Object other){

        if(other instanceof Mensagem){
            return this.idUsuario.equals(((Mensagem) other).idUsuario)
            && this.mensagem.equals(((Mensagem) other).mensagem);
        }

        return false;
    }

}


