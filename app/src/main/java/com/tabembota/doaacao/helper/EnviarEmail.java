package com.tabembota.doaacao.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnviarEmail {

    //API
    private final static String urlAPI = "https://us-central1-doacao-fa8a7.cloudfunctions.net/";
    private static String nomeNecessitado;
    private static String emailNecessitado;

    public static void enviarEmail(String assuntoBruto, String corpo, Usuario usuarioApp){
        Log.d("MISSGAY", "Chamou");
        final EnviarEmailService service;
        Call<Integer> call;
        Retrofit retrofit;

        retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final String email = usuarioApp.getEmail();
        final String assunto = "DoAção: ".concat(assuntoBruto);

        service = retrofit.create(EnviarEmailService.class);

        call = service.enviarEmail(email, assunto, corpo);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        Log.d("MISSGAY", "Enviou");
    }

    public static void enviarEmailInteresse(String assuntoBruto, String corpo, String idUsuarioNecessitado, final Usuario usuarioApp){
        final EnviarEmailService service;
        Call<Integer> call;
        Retrofit retrofit;

        retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final String email = usuarioApp.getEmail();
        final String assunto = "DoAção: ".concat(assuntoBruto);

        service = retrofit.create(EnviarEmailService.class);

        call = service.enviarEmail(email, assunto, corpo);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        DatabaseReference referencia = ConfiguracaoFirebase.getDatabaseReference().child("user").child(idUsuarioNecessitado);
        referencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario necessitado = dataSnapshot.getValue(Usuario.class);
                nomeNecessitado = necessitado.getNome();
                emailNecessitado = necessitado.getEmail();

                Call<Integer> call2;

                String resposta = "Parabéns, " + nomeNecessitado + "! Você possui uma nova pessoa doadora interessada: " +
                        usuarioApp.getNome() + ". Entre em contato com ela para combinarem tudo entre si!";

                call2 = service.enviarEmail(emailNecessitado, assunto, resposta);
                call2.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {

                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
