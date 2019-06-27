package com.tabembota.doaacao.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.helper.UsuarioFirebase;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText textInputEmail, textInputSenha;
    private String name, email, senha;
    private FirebaseAuth usuarioRef = ConfiguracaoFirebase.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEmail = findViewById(R.id.textInputEmail);
        textInputSenha = findViewById(R.id.textInputSenha);

        textInputEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    textInputEmail.clearFocus();
                    textInputSenha.requestFocus();
                    return true;
                }
                return false;
            }
        });

        textInputSenha.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    View viewActivity = findViewById(android.R.id.content);
                    login(viewActivity);
                    return true;
                }
                return false;
            }
        });

    }

    private boolean validarLogin(String email, String senha){
        if(!email.isEmpty()){
            if(!senha.isEmpty()){
                return true;
            }
            else{
                Toast.makeText(this, "Insira sua senha.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Insira um e-mail válido.", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public void login(View view){
        email = textInputEmail.getText().toString();
        senha = textInputSenha.getText().toString();

        if(validarLogin(email, senha)){
            usuarioRef.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,
                                        "Logado com sucesso!",
                                        Toast.LENGTH_SHORT).show();


                                iniciarTelaPrincipal(
                                        UsuarioFirebase.getDadosUsuarioLogado().getNome(),
                                        email
                                );
                            }
                            else{

                                String excecao = "";
                                try{
                                    throw task.getException();
                                }
                                catch(FirebaseAuthInvalidUserException e){
                                    excecao = "Usuário não está cadastrado.";
                                }
                                catch(FirebaseAuthInvalidCredentialsException e){
                                    excecao = "E-mail e senha não correspondem a um usuário válido.";
                                }
                                catch(Exception e){
                                    excecao = "Erro ao logar usuário: " + e.getMessage();
                                    e.printStackTrace();
                                }
                                Toast.makeText(LoginActivity.this,
                                        excecao,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void iniciarTelaPrincipal(String nome, String email){
        Intent intent = new Intent();
        intent.putExtra("LOGIN_EMAIL", email);
        intent.putExtra("LOGIN_NOME", nome);
        setResult(RESULT_OK, intent);
        finish();
    }
}
