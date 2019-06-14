package com.tabembota.doaacao.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListaDoacoesFragment extends Fragment {
    //Recycler view e seus capangas
    private RecyclerView recyclerViewListaDoacoes;
    private List<Doacao> listaDoacao = new ArrayList<>();
    private DoacaoAdapter doacaoAdapter;

    //Firebase
    private ChildEventListener recuperarOportunidadesEventListener;
    private DatabaseReference oportunidadesRef;

    //Filtros
    private ArrayList<Integer> filtroEscolhido = new ArrayList<>();

    public ListaDoacoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewListaDoacoes = view.findViewById(R.id.recyclerViewListaDoacoes);
        oportunidadesRef = ConfiguracaoFirebase.getDatabaseReference().child("oportunidade");

        ((PrincipalActivity) getActivity()).mudarTitulo("Lista de doações");

        //Configurando RecyclerView
        configurarRecyclerView();
        recuperarDadosListaDeDoacoes();
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
                if(filtroEscolhido.isEmpty() || filtroEscolhido.contains(doacao.getFiltro())){
                    listaDoacao.add(doacao);
                }

                Collections.sort(listaDoacao);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filtros, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.aplicar_filtro) {
            View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_filtro, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

            final CheckBox cbVoluntariado, cbMoveis, cbAlimentos, cbRoupas;
            cbVoluntariado = popupView.findViewById(R.id.cbVoluntariado);
            cbMoveis = popupView.findViewById(R.id.cbMoveis);
            cbAlimentos = popupView.findViewById(R.id.cbAlimentos);
            cbRoupas = popupView.findViewById(R.id.cbRoupas);

            Button btAplicarFiltro = popupView.findViewById(R.id.btAplicarFiltro);

            btAplicarFiltro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filtroEscolhido.clear();

                    if(cbVoluntariado.isChecked()){
                        filtroEscolhido.add(0);
                    }
                    if(cbMoveis.isChecked()){
                        filtroEscolhido.add(1);
                    }
                    if(cbAlimentos.isChecked()){
                        filtroEscolhido.add(2);
                    }
                    if(cbRoupas.isChecked()){
                        filtroEscolhido.add(3);
                    }

                    oportunidadesRef.removeEventListener(recuperarOportunidadesEventListener);
                    recuperarDadosListaDeDoacoes();

                    popupWindow.dismiss();
                }
            });

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, popupWindow.getHeight());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
