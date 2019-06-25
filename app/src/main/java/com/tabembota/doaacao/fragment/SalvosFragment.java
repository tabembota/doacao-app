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

import com.tabembota.doaacao.R;
import com.tabembota.doaacao.RecyclerItemClickListener;
import com.tabembota.doaacao.activity.PrincipalActivity;
import com.tabembota.doaacao.adapter.DoacaoAdapter;


public class SalvosFragment extends Fragment {

    private RecyclerView recyclerViewSalvos;
    private DoacaoAdapter doacaoAdapter;

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

        Log.d("MISSGAY", PrincipalActivity.listaSalvos.toString());

    }

    void configurarRecyclerView(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewSalvos.setLayoutManager(layoutManager);

        doacaoAdapter = new DoacaoAdapter(PrincipalActivity.listaSalvos, getActivity());
        recyclerViewSalvos.setAdapter(doacaoAdapter);

        recyclerViewSalvos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recyclerViewSalvos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

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
