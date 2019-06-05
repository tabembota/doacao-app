package com.tabembota.doaacao.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tabembota.doaacao.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText textInputEmail, textInputSenha;
    private String name, email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEmail = findViewById(R.id.textInputEmail);
        textInputSenha = findViewById(R.id.textInputSenha);

    }

    //Cant press back on login screen
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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
        email = textInputEmail.getText().toString();
        senha = textInputSenha.getText().toString();

        if(validarLogin(email, senha)){
            //startActivity(new Intent(LoginActivity.this, ListaDoacoesActivity.class));
            Intent intent = new Intent();
            intent.putExtra("LOGIN_EMAIL", email);
            intent.putExtra("LOGIN_NAME", email);
            setResult(RESULT_OK, intent);
            finish();
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
            if(requestCode == 101){
                email = data.getStringExtra("CADASTRO_EMAIL");
                name = data.getStringExtra("CADASTRO_NOME");
                senha = data.getStringExtra("CADASTRO_SENHA");

                Intent intent = new Intent();
                intent.putExtra("LOGIN_EMAIL", email);
                intent.putExtra("LOGIN_NAME", name);
                setResult(RESULT_OK, intent);
                finish();

            }
        }
    }
}
