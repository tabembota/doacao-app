package com.tabembota.doaacao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.helper.UsuarioFirebase;

public class MainIntroActivity extends IntroActivity {

    private FirebaseAuth usuarioRef = ConfiguracaoFirebase.getFirebaseAuth();
    private String name, email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .canGoBackward(false)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Já logado
        if(usuarioRef.getCurrentUser() != null){
            iniciarTelaPrincipal(
                    UsuarioFirebase.getDadosUsuarioLogado().getNome(),
                    UsuarioFirebase.getDadosUsuarioLogado().getEmail()
            );
        }
    }

    public void cadastrar(View view){
        Intent i = new Intent(this, CadastroActivity.class);
        startActivityForResult(i, 101);
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == 100){
                email = data.getStringExtra("LOGIN_EMAIL");
                name = data.getStringExtra("LOGIN_NOME");

                Intent intent = new Intent(this, PrincipalActivity.class);
                intent.putExtra("LOGIN_EMAIL", email);
                intent.putExtra("LOGIN_NAME", name);
                startActivity(intent);
                finish();
            }
            if(requestCode == 101){
                email = data.getStringExtra("CADASTRO_EMAIL");
                name = data.getStringExtra("CADASTRO_NOME");
                //senha = data.getStringExtra("CADASTRO_SENHA");

                Intent intent = new Intent(this, PrincipalActivity.class);
                intent.putExtra("LOGIN_EMAIL", email);
                intent.putExtra("LOGIN_NAME", name);
                startActivity(intent);
                finish();
            }
        }
    }


    public void logar(View view){
        Intent i = new Intent(this, LoginActivity.class);
        startActivityForResult(i, 100);
    }

    private void iniciarTelaPrincipal(String nome, String email){
        Intent intent = new Intent(MainIntroActivity.this, PrincipalActivity.class);
        intent.putExtra("LOGIN_EMAIL", email);
        intent.putExtra("LOGIN_NAME", nome);
        startActivity(intent);
        finish();
    }

}
