package com.tabembota.doaacao.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.tabembota.doaacao.helper.EnviarEmail;
import com.tabembota.doaacao.helper.UsuarioFirebase;
import com.tabembota.doaacao.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText textInputNome, textInputEmail, textInputSenha, textInputBairro;
    private FirebaseAuth usuarioRef = ConfiguracaoFirebase.getFirebaseAuth();
    private Usuario usuario;
    private Button btCadastrar;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        textInputNome = findViewById(R.id.textInputNome);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputSenha = findViewById(R.id.textInputSenha);
        textInputBairro = findViewById(R.id.textInputBairro);
        btCadastrar = findViewById(R.id.btCadastrar);

        progressBar = findViewById(R.id.progressBar);

    }

    private void setarClicavel(boolean escolha){
        if(escolha){
            textInputNome.setFocusableInTouchMode(true);
            textInputEmail.setFocusableInTouchMode(true);
            textInputBairro.setFocusableInTouchMode(true);
            textInputSenha.setFocusableInTouchMode(true);
        }
        else{
            textInputNome.setFocusable(false);
            textInputEmail.setFocusable(false);
            textInputSenha.setFocusable(false);
            textInputBairro.setFocusable(false);
        }

        btCadastrar.setClickable(escolha);
    }

    public void cadastrarUsuario(View view){
        //Obtém entradas do usuário
        String nome = textInputNome.getText().toString();
        String email = textInputEmail.getText().toString();
        String senha = textInputSenha.getText().toString();
        String bairro = textInputBairro.getText().toString();

        //Validação da entrada
        if(!nome.isEmpty()){
            if(!email.isEmpty()){
                if(!bairro.isEmpty()) {
                    if (!senha.isEmpty()) {
                        //Se tudo certo, autentica usuário
                        autenticaUsuario(nome, email, senha, bairro);
                    } else {
                        Toast.makeText(this, "Insira uma senha.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "Insira um bairro", Toast.LENGTH_SHORT).show();
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

    private void autenticaUsuario(String nome, String email, String senha, String bairro){
        progressBar.setVisibility(View.VISIBLE);

        setarClicavel(false);

        usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setBairro(bairro);

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
                        usuario.setIdUsuario(UsuarioFirebase.getUsuarioAtual().getUid());
                        usuario.salvar(UsuarioFirebase.getUsuarioAtual());

                        String assunto = "Bem vindx!";
                        String corpo = "Estamos muito felizes em anunciar que a partir de agora, você, " + usuario.getNome() + ", é um "+
                                "usuário cadastrado no DoAção, um aplicativo voltado para ajudar entidades que trabalham com caridade. " +
                                "Nossa missão é transformar a caridade algo fácil de se realizar, por meio da visibilidade " +
                                "das oportunidades existentes. Não deixe para depois: comece a doar hoje mesmo!\n\nEquipe TaBemBota";

                        EnviarEmail.enviarEmail(assunto, corpo, usuario);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    progressBar.setVisibility(View.GONE);

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

                    progressBar.setVisibility(View.GONE);
                    setarClicavel(true);

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
