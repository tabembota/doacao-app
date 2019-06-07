package com.tabembota.doaacao.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    //Cant press back on login screen
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private boolean validarLogin(String email, String senha){
        if(!email.isEmpty() && email.contains("@")){
            if(!senha.isEmpty() && senha.length() > 6){
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
                                    excecao = "Erro ao cadastrar usuário: " + e.getMessage();
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
                //senha = data.getStringExtra("CADASTRO_SENHA");

                Intent intent = new Intent(LoginActivity.this, ListaDoacoesActivity.class);
                intent.putExtra("LOGIN_EMAIL", email);
                intent.putExtra("LOGIN_NAME", name);
                startActivity(intent);
                finish();
            }
        }
    }

    private void iniciarTelaPrincipal(String nome, String email){
        Intent intent = new Intent(LoginActivity.this, ListaDoacoesActivity.class);
        intent.putExtra("LOGIN_EMAIL", email);
        intent.putExtra("LOGIN_NAME", nome);
        startActivity(intent);
        finish();
    }
}
