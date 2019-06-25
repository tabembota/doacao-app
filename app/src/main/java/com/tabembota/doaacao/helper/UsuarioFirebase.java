package com.tabembota.doaacao.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.model.Usuario;

public class UsuarioFirebase {
    public static String getIdentificadorUsuario(){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        return Base64Custom.codificarBase64(
                autenticacao.getCurrentUser().getEmail()
        );

    }

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        return autenticacao.getCurrentUser();
    }

    public static boolean atualizarNomeUsuario(String nome){

        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.");
                    }
                }
            });
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario getDadosUsuarioLogado(){
        FirebaseUser firebaseUser = getUsuarioAtual();
        DatabaseReference usuarioRef = ConfiguracaoFirebase.getDatabaseReference().child("user").child(firebaseUser.getUid());

        final Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario.setBairro(dataSnapshot.getValue(Usuario.class).getBairro());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return usuario;

    }
}
