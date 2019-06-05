package com.tabembota.doaacao.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tabembota.doaacao.R;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText textInputNome, textInputEmail, textInputSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        textInputNome = findViewById(R.id.textInputNome);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputSenha = findViewById(R.id.textInputSenha);

    }
    
    //Cant press back on login screen
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void cadastrarUsuario(View view){
        String nome = textInputNome.getText().toString();
        String email = textInputEmail.getText().toString();
        String senha = textInputSenha.getText().toString();

        if(!nome.isEmpty()){
            if(!email.isEmpty() && email.contains("@")){
                if(!senha.isEmpty() && senha.length() > 6){

                    Intent intent = new Intent();
                    intent.putExtra("CADASTRO_EMAIL", email);
                    intent.putExtra("CADASTRO_NOME", nome);
                    intent.putExtra("CADASTRO_SENHA", senha);
                    setResult(RESULT_OK, intent);
                    finish();

                    //Toast.makeText(this, "Realizar cadastro", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(this, "Insira uma senha válida (mínimo de 6 caracteres).", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Insira um e-mail válido.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Insira um nome.", Toast.LENGTH_SHORT).show();
        }
    }
}
