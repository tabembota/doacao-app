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
import android.widget.Toast;

import com.tabembota.doaacao.R;
import com.tabembota.doaacao.RecyclerItemClickListener;
import com.tabembota.doaacao.adapter.DoacaoAdapter;
import com.tabembota.doaacao.model.Doacao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment {

    private RecyclerView recyclerViewListaDoacoes;
    private List<Doacao> listaDoacao = new ArrayList<>();
    private DoacaoAdapter doacaoAdapter;
    private int flagCriado = 0;


    public PrincipalFragment() {
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

        recyclerViewListaDoacoes = getView().findViewById(R.id.recyclerViewListaDoacoes);

        //Configurando RecyclerView
        recuperarDadosListaDeDoacoes();
        configurarRecyclerView();

        Log.d("MISSGAY", "crio dinovo");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void recuperarDadosListaDeDoacoes(){
        listaDoacao.clear();
        Doacao doacao;
        Date date = new Date();
        doacao = new Doacao(
            "Klabin doando pano",
                "Estamos doando pano somente para quem conhece a empresa",
                98765,
                "Estúdio da Klabin",
                "Klabinando",
                "Não tem",
                date
        );
        listaDoacao.add(doacao);

        doacao = new Doacao(
                "Miss sendo miss",
                "Venha doar o miss, é só buscar",
                86757,
                "Casa do miss = formula",
                "Aluguel",
                "Tem, mas nao ta com a gente",
                date
        );
        listaDoacao.add(doacao);

        doacao = new Doacao(
                "Reuniao da ADA",
                "Reuniao da ADA que o cinco nunca vai e reclama",
                1234,
                "Sei la, c2?",
                "Role CUTavel",
                "Park",
                date
        );
        listaDoacao.add(doacao);

        //doacaoAdapter.notifyDataSetChanged();
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
