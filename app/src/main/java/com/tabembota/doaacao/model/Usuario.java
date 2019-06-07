package com.tabembota.doaacao.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;

public class Usuario {
    private String nome;
    private String email;

    public Usuario() {
    }

    public void salvar(FirebaseUser firebaseUser){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        databaseReference.child("user").child(firebaseUser.getUid()).setValue(this);
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

}
