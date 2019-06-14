package com.tabembota.doaacao.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.activity.PrincipalActivity;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.helper.UsuarioFirebase;
import com.tabembota.doaacao.model.Doacao;


public class CriarOportunidadeFragment extends Fragment {

    private EditText editTextTitulo, editTextDescricao, editTextEmail;
    private RadioGroup rgTag;
    private Button btCriar;
    private NavigationView navigationView;

    private int tagescolhida = 0;

    public CriarOportunidadeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_criar_oportunidade, container, false);

        btCriar = view.findViewById(R.id.btCriar);
        btCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarOportunidade();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTitulo = view.findViewById(R.id.editTextTitulo);
        editTextDescricao = view.findViewById(R.id.editTextDescricao);
        editTextEmail = view.findViewById(R.id.editTextEmail);

        navigationView = getActivity().findViewById(R.id.nav_view);

        rgTag = view.findViewById(R.id.rgTag);

        rgTag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbVoluntariado){
                    //Voluntariado
                    tagescolhida = 0;
                }
                else if(checkedId == R.id.rbMoveis){
                    //Móveis
                    tagescolhida = 1;
                }
                else if(checkedId == R.id.rbAlimentos){
                    //Alimentos
                    tagescolhida = 2;
                }
                else if(checkedId == R.id.rbRoupas){
                    //Roupas
                    tagescolhida = 3;
                }
            }
        });

        ((PrincipalActivity) getActivity()).mudarTitulo("Criar oportunidade");
    }

    public void criarOportunidade(){
        String titulo;
        String descricao;
        String email;

        titulo = editTextTitulo.getText().toString();
        descricao = editTextDescricao.getText().toString();
        email = editTextEmail.getText().toString();

        if(!titulo.isEmpty()){
            if(!descricao.isEmpty()){
                if(!email.isEmpty() && email.contains("@") && email.contains(".")){

                    Doacao doacao = new Doacao(
                            UsuarioFirebase.getUsuarioAtual().getUid(),
                            titulo,
                            descricao,
                            email,
                            R.drawable.logo,
                            tagescolhida
                    );

                    DatabaseReference referencia = ConfiguracaoFirebase.getDatabaseReference();
                    referencia.child("oportunidade")
                            .push()
                            .setValue(doacao);

                    Toast.makeText(getContext(),
                            "Oportunidade de doação criada com sucesso! Você receberá um e-mail de confirmação em breve (ainda não).",
                            Toast.LENGTH_SHORT).show();

                    navigationView.setCheckedItem(R.id.lista_doacoes);

                    editTextTitulo.setText("");
                    editTextDescricao.setText("");
                    editTextEmail.setText("");
                    rgTag.check(R.id.rbVoluntariado);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ListaDoacoesFragment listaDoacoesFragment = PrincipalActivity.getListaDoacoesFragment();
                    ft.replace(R.id.frameLayoutMain, listaDoacoesFragment);
                    ft.commit();

                }
                else{
                    Toast.makeText(getContext(),
                            "Insira um e-mail válido.",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getContext(),
                        "Insira uma descrição.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getContext(),
                    "Insira um título.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
