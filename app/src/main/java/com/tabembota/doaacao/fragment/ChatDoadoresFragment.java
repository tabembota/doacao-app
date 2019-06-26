package com.tabembota.doaacao.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.activity.ChatActivity;
import com.tabembota.doaacao.activity.PrincipalActivity;
import com.tabembota.doaacao.adapter.AdapterUsuarios;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.helper.RecyclerItemClickListener;
import com.tabembota.doaacao.helper.UsuarioFirebase;
import com.tabembota.doaacao.model.Doacao;
import com.tabembota.doaacao.model.Interesse;
import com.tabembota.doaacao.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ChatDoadoresFragment extends Fragment {

    //Recycler View
    private RecyclerView recyclerViewDoadores;
    private AdapterUsuarios adapterUsuarios;
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private List<Doacao> listaDoacaoInteresseUsuario = new ArrayList<>();

    //Firebase
    private ChildEventListener childEventListenerInteresses;
    private DatabaseReference interessesRef;
    private Usuario usuarioApp = UsuarioFirebase.getDadosUsuarioLogado();

    public ChatDoadoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_doadores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((PrincipalActivity) getActivity()).mudarTitulo("Conversar com doadores");

        recyclerViewDoadores = view.findViewById(R.id.recyclerViewDoadores);

        recuperaUsuariosDoadores();
        configurarRecyclerView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        interessesRef.removeEventListener(childEventListenerInteresses);
    }

    private void recuperaUsuariosDoadores(){
        listaUsuarios.clear();

        interessesRef = ConfiguracaoFirebase.getDatabaseReference().child("interesse");
        childEventListenerInteresses = interessesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Interesse interesse = dataSnapshot.getValue(Interesse.class);
                //Log.d("MISSGAY", interesse.getUser_id());
                for(final Doacao doacao : PrincipalActivity.listaDoacoes){
                    if(interesse.getOp_id().equals(doacao.getOp_id())){

                        DatabaseReference usuariosRef = ConfiguracaoFirebase.getDatabaseReference().child("user").child(interesse.getUser_id());

                        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                                listaDoacaoInteresseUsuario.add(doacao);
                                listaUsuarios.add(usuario);
                                adapterUsuarios.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void configurarRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewDoadores.setLayoutManager(layoutManager);

        adapterUsuarios = new AdapterUsuarios(listaUsuarios, listaDoacaoInteresseUsuario, getContext());
        recyclerViewDoadores.setAdapter(adapterUsuarios);

        recyclerViewDoadores.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recyclerViewDoadores,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Usuario usuario = listaUsuarios.get(position);
                                Doacao doacao = listaDoacaoInteresseUsuario.get(position);

                                Intent i = new Intent(getContext(), ChatActivity.class);
                                i.putExtra("USUARIO", usuario);
                                i.putExtra("DOACAO", doacao);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }
}
