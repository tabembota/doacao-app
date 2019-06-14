package com.tabembota.doaacao.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tabembota.doaacao.R;
import com.tabembota.doaacao.activity.PrincipalActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalvosFragment extends Fragment {

    private PrincipalActivity principalActivity = ((PrincipalActivity) getActivity());

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

        principalActivity.mudarTitulo("Doações salvas");

    }
}
