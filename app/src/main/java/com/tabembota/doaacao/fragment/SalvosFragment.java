package com.tabembota.doaacao.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.helper.RecyclerItemClickListener;
import com.tabembota.doaacao.activity.ChatActivity;
import com.tabembota.doaacao.activity.PrincipalActivity;
import com.tabembota.doaacao.adapter.AdapterDoacao;
import com.tabembota.doaacao.model.Doacao;
import com.tabembota.doaacao.model.Usuario;


public class SalvosFragment extends Fragment {

    private RecyclerView recyclerViewSalvos;
    private AdapterDoacao adapterDoacao;

    public SalvosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_salvos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewSalvos = view.findViewById(R.id.recyclerViewSalvos);

        ((PrincipalActivity) getActivity()).mudarTitulo("Doações salvas");

        configurarRecyclerView();

    }

    @Override
    public void onStart() {
        super.onStart();

        adapterDoacao.notifyDataSetChanged();
    }

    void configurarRecyclerView(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewSalvos.setLayoutManager(layoutManager);

        adapterDoacao = new AdapterDoacao(PrincipalActivity.listaSalvos, getActivity());
        recyclerViewSalvos.setAdapter(adapterDoacao);

        recyclerViewSalvos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recyclerViewSalvos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                final Doacao doacao = PrincipalActivity.listaSalvos.get(position);

                                DatabaseReference usuarioRef = ConfiguracaoFirebase.getDatabaseReference().child("user").child(doacao.getUser_id());
                                usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                                        Intent i = new Intent(getContext(), ChatActivity.class);
                                        //i.putExtra("DOACAO", doacao);
                                        i.putExtra("USUARIO", usuario);
                                        startActivity(i);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
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
