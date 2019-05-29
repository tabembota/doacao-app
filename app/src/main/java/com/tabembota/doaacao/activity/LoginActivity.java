package com.tabembota.doaacao.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tabembota.doaacao.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText textInputEmail, textInputSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEmail = findViewById(R.id.textInputEmail);
        textInputSenha = findViewById(R.id.textInputSenha);

    }

    private boolean validarLogin(String email, String senha){
        if(!email.isEmpty() && email.contains("@")){
            if(!senha.isEmpty() && senha.length() > 6){

                Toast.makeText(this, "Bem-vindo ao DoAção!", Toast.LENGTH_SHORT).show();
                return true;

            }
            else{
                Toast.makeText(this, "Insira uma senha válida.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Insira um e-mail válido.", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public void login(View view){
        String email = textInputEmail.getText().toString();
        String senha = textInputSenha.getText().toString();

        if(validarLogin(email, senha)){
            startActivity(new Intent(LoginActivity.this, ListaDoacoesActivity.class));
            finish();
        }
    }

    public void cadastrar(View view){
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
        finish();
    }
}
