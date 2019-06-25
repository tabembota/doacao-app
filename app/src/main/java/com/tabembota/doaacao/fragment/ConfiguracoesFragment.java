package com.tabembota.doaacao.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.activity.PrincipalActivity;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.helper.UsuarioFirebase;
import com.tabembota.doaacao.model.Usuario;

import java.sql.Time;

import static android.support.constraint.Constraints.TAG;

public class ConfiguracoesFragment extends Fragment {

    //Usuario logado
    private FirebaseUser user = UsuarioFirebase.getUsuarioAtual();
    private Usuario usuario = UsuarioFirebase.getDadosUsuarioLogado();

    //Componentes da interface
    private EditText textNome, textBairro, textSenhaAntiga, textNovaSenha, textNovaSenhaConfirmacao;
    private Button botaoAplicarMudancas;
    private NavigationView navigationView;


    public ConfiguracoesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuracoes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((PrincipalActivity) getActivity()).mudarTitulo("Configurações");

        //Interface
        textNome = view.findViewById(R.id.textNome);
        textBairro = view.findViewById(R.id.textBairro);
        textSenhaAntiga = view.findViewById(R.id.textSenhaAntiga);
        textNovaSenha = view.findViewById(R.id.textNovaSenha);
        textNovaSenhaConfirmacao = view.findViewById(R.id.textNovaSenhaConfirmacao);
        botaoAplicarMudancas = view.findViewById(R.id.botaoAplicarMudancas);
        navigationView = getActivity().findViewById(R.id.nav_view);

        //Preenchendo interface
        textNome.setText(usuario.getNome());
        textBairro.setText(usuario.getBairro());

        //Botão configurado
        botaoAplicarMudancas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarInformacoes();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        textSenhaAntiga.setText("");
        textNovaSenha.setText("");
        textNovaSenhaConfirmacao.setText("");
    }

    private void mudarSenha(String email, String oldPass, final String newPass){

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, oldPass);

        final Activity activity = getActivity();

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("MISSGAY", "Password updated");
                                    } else {
                                        Toast.makeText(activity, "Erro: senha não foi atualizada. Tente novamente.", Toast.LENGTH_SHORT).show();
                                        Log.d("MISSGAY", "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(activity, "Erro: autenticação falhou. Sua senha não foi atualizada.", Toast.LENGTH_SHORT).show();
                            Log.d("MISSGAY", "Error auth failed");
                        }
                    }
                });
    }

    private void atualizarInformacoes(){
        final String senhaAntiga = textSenhaAntiga.getText().toString();
        final String novaSenha = textNovaSenha.getText().toString();
        String novaSenhaConfirmacao = textNovaSenhaConfirmacao.getText().toString();

        if(!senhaAntiga.isEmpty() || !novaSenha.isEmpty() || !novaSenhaConfirmacao.isEmpty()){
            if(!senhaAntiga.isEmpty()){
                if(!novaSenha.isEmpty()){
                    if(!novaSenhaConfirmacao.isEmpty()){
                        mudarSenha(usuario.getEmail(), senhaAntiga, novaSenha);

                    }
                    else{
                        Toast.makeText(getActivity(), "Reinsira sua nova senha.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Insira sua nova senha.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getActivity(), "Insira sua antiga senha.", Toast.LENGTH_SHORT).show();
            }
        }

        String bairroNovo = textBairro.getText().toString();
        String nomeNovo = textNome.getText().toString();

        DatabaseReference usuariosRef = ConfiguracaoFirebase.getDatabaseReference().child("user").child(user.getUid());
        usuario.setBairro(bairroNovo);
        usuario.setNome(nomeNovo);
        usuariosRef.setValue(usuario);

        UsuarioFirebase.atualizarNomeUsuario(nomeNovo);

        Snackbar.make(this.getView(), "As mudanças foram efetuadas em sua conta!", Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();

        navigationView.setCheckedItem(R.id.lista_doacoes);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ListaDoacoesFragment listaDoacoesFragment = PrincipalActivity.getListaDoacoesFragment();
        ft.replace(R.id.frameLayoutMain, listaDoacoesFragment);
        ft.commit();
    }

}
