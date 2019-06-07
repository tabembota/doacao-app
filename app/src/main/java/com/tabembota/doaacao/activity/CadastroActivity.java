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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.helper.Base64Custom;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.helper.UsuarioFirebase;
import com.tabembota.doaacao.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText textInputNome, textInputEmail, textInputSenha;
    private FirebaseAuth usuarioRef = ConfiguracaoFirebase.getFirebaseAuth();
    private Usuario usuario;

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
        //Obtém entradas do usuário
        String nome = textInputNome.getText().toString();
        String email = textInputEmail.getText().toString();
        String senha = textInputSenha.getText().toString();

        //Validação da entrada
        if(!nome.isEmpty()){
            if(!email.isEmpty()){
                if(!senha.isEmpty()){
                    //Se tudo certo, autentica usuário
                    autenticaUsuario(nome, email, senha);
                }
                else{
                    Toast.makeText(this, "Insira uma senha.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Insira um e-mail.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Insira um nome.", Toast.LENGTH_SHORT).show();
        }
    }

    private void autenticaUsuario(String nome, String email, String senha){
        usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setIdUsuario(Base64Custom.codificarBase64(email));

        //Da referência à base de usuários, cria uma autenticação
        usuarioRef.createUserWithEmailAndPassword(
                email,
                senha
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            //Se for completada a criação
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //e a tarefa for bem sucedida,
                if(task.isSuccessful()){
                    //Salva usuários também no realtime database
                    //(usaremos os dados dele depois)
                    try{
                        UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                        usuario.salvar();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    //Mensagem de sucesso e fechamento da activity
                    Toast.makeText(CadastroActivity.this,
                            "Cadastrado com sucesso!",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("CADASTRO_EMAIL", usuario.getEmail());
                    intent.putExtra("CADASTRO_NOME", usuario.getNome());
                    setResult(RESULT_OK, intent);
                    finish();

                }
                //Caso não seja bem sucedida, tratar.
                else{

                    String excecao = "";
                    try{
                        throw task.getException();
                    }
                    catch(FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }
                    catch(FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor, digite um e-mail válido.";
                    }
                    catch(FirebaseAuthUserCollisionException e){
                        excecao = "Essa conta já foi cadastrada.";
                    }
                    catch(Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
