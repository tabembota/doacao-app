package com.tabembota.doaacao.fragment;


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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.RecyclerItemClickListener;
import com.tabembota.doaacao.activity.PrincipalActivity;
import com.tabembota.doaacao.adapter.DoacaoAdapter;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.model.Doacao;

import java.util.ArrayList;
import java.util.List;

public class ListaDoacoesFragment extends Fragment {

    private RecyclerView recyclerViewListaDoacoes;
    private List<Doacao> listaDoacao = new ArrayList<>();
    private DoacaoAdapter doacaoAdapter;

    private ChildEventListener recuperarOportunidadesEventListener;
    private DatabaseReference oportunidadesRef;

    private PrincipalActivity principalActivity = ((PrincipalActivity) getActivity());

    public ListaDoacoesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewListaDoacoes = view.findViewById(R.id.recyclerViewListaDoacoes);
        oportunidadesRef = ConfiguracaoFirebase.getDatabaseReference().child("oportunidade");

        principalActivity.mudarTitulo("Lista de doações");

        //Configurando RecyclerView
        configurarRecyclerView();
        recuperarDadosListaDeDoacoes();

        Log.d("MISSGAY", "crio dinovo");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        oportunidadesRef.removeEventListener(recuperarOportunidadesEventListener);
    }

    private void recuperarDadosListaDeDoacoes(){
        listaDoacao.clear();

        recuperarOportunidadesEventListener = oportunidadesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Doacao doacao = dataSnapshot.getValue(Doacao.class);
                listaDoacao.add(doacao);
                doacaoAdapter.notifyDataSetChanged();
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaDoacoes.setLayoutManager(layoutManager);

        doacaoAdapter = new DoacaoAdapter(listaDoacao, getContext());
        recyclerViewListaDoacoes.setAdapter(doacaoAdapter);

        recyclerViewListaDoacoes.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recyclerViewListaDoacoes,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Intent i
                                Toast.makeText(getContext(), "Potato", Toast.LENGTH_SHORT).show();
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
