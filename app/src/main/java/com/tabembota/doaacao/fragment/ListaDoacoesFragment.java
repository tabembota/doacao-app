package com.tabembota.doaacao.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.helper.EnviarEmail;
import com.tabembota.doaacao.helper.RecyclerItemClickListener;
import com.tabembota.doaacao.activity.DoacaoActivity;
import com.tabembota.doaacao.activity.PrincipalActivity;
import com.tabembota.doaacao.adapter.AdapterDoacao;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.helper.EnviarEmailService;
import com.tabembota.doaacao.helper.UsuarioFirebase;
import com.tabembota.doaacao.model.Doacao;
import com.tabembota.doaacao.model.Interesse;
import com.tabembota.doaacao.model.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaDoacoesFragment extends Fragment {
    //Recycler view e seus capangas
    private RecyclerView recyclerViewListaDoacoes;
    private List<Doacao> listaDoacao = new ArrayList<>();
    private AdapterDoacao adapterDoacao;
    private ProgressBar progressBar;

    //Firebase
    private ChildEventListener recuperarOportunidadesEventListener;
    private DatabaseReference oportunidadesRef;
    private Usuario usuarioApp = UsuarioFirebase.getDadosUsuarioLogado();

    //Filtros
    private ArrayList<Integer> filtroEscolhido = new ArrayList<>();

    //Variáveis quaisquer
    private int recuperouDados = 0;

    public ListaDoacoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_lista_doacoes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewListaDoacoes = view.findViewById(R.id.recyclerViewListaDoacoes);
        progressBar = view.findViewById(R.id.progressBar);
        oportunidadesRef = ConfiguracaoFirebase.getDatabaseReference().child("oportunidade");

        ((PrincipalActivity) getActivity()).mudarTitulo("Lista de doações");

        //Configurando RecyclerView
        configurarRecyclerView();
        swipe();

    }

    @Override
    public void onStart() {
        super.onStart();
        exibirProgress(true);
        recuperarDadosListaDeDoacoes();
        fechaProgressAposTempo(5000);
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
                if(!doacao.getUser_id().equals(usuarioApp.getIdUsuario())){
                    if(filtroEscolhido.isEmpty() || filtroEscolhido.contains(doacao.getFiltro())){
                        listaDoacao.add(doacao);
                    }

                    Collections.sort(listaDoacao);

                    adapterDoacao.notifyDataSetChanged();
                    recuperouDados = 1;
                    exibirProgress(false);
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
                Toast.makeText(getActivity(),
                        "Verifique sua conexão com a internet.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaDoacoes.setLayoutManager(layoutManager);

        adapterDoacao = new AdapterDoacao(listaDoacao, getContext());
        recyclerViewListaDoacoes.setAdapter(adapterDoacao);

        recyclerViewListaDoacoes.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recyclerViewListaDoacoes,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                final Doacao doacao = listaDoacao.get(position);

                                DatabaseReference usuariosRef = ConfiguracaoFirebase.getDatabaseReference()
                                        .child("user")
                                        .child(doacao.getUser_id());

                                usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                                        Intent i = new Intent(getContext(), DoacaoActivity.class);
                                        i.putExtra("DOACAO", doacao);
                                        i.putExtra("USUARIO", usuario);
                                        startActivity(i);
                                        //Toast.makeText(getContext(), "Potato", Toast.LENGTH_SHORT).show();
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
            LinearLayout linearLayoutPopUp = popupView.findViewById(R.id.layoutFundoPopUp);
            ConstraintLayout constraintLayout = popupView.findViewById(R.id.constraintLayoutPopUp);
            cbVoluntariado = popupView.findViewById(R.id.cbVoluntariado);
            cbMoveis = popupView.findViewById(R.id.cbMoveis);
            cbAlimentos = popupView.findViewById(R.id.cbAlimentos);
            cbRoupas = popupView.findViewById(R.id.cbRoupas);

            Button btAplicarFiltro = popupView.findViewById(R.id.btAplicarFiltro);

            if(filtroEscolhido.contains(0)){
                cbVoluntariado.setChecked(true);
            }
            if(filtroEscolhido.contains(1)){
                cbMoveis.setChecked(true);
            }
            if(filtroEscolhido.contains(2)){
                cbAlimentos.setChecked(true);
            }
            if(filtroEscolhido.contains(3)){
                cbRoupas.setChecked(true);
            }

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            linearLayoutPopUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

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

    private void exibirProgress(boolean exibirProgress) {
        progressBar.setVisibility(exibirProgress ? View.VISIBLE : View.GONE);
        recyclerViewListaDoacoes.setVisibility(!exibirProgress ? View.VISIBLE : View.GONE);
    }

    private void fechaProgressAposTempo(int tempo){
        final int t = tempo;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(recuperouDados == 0){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(getActivity(),
                                    "Não foi possível obter as informações da internet. Tente novamente.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        thread.start();
    }

    private void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                if(i == ItemTouchHelper.START || i == ItemTouchHelper.END){
                    salvarOportunidade(viewHolder);
                }
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewListaDoacoes);

    }

    private boolean checarSeDoacaoEstaInteressada(Doacao alvo){
        for (Doacao teste : PrincipalActivity.listaSalvos){
            if(alvo.igual_a(teste)){
                return true;
            }
        }

        return false;
    }

    private void salvarOportunidade(RecyclerView.ViewHolder viewHolder){

        int position = viewHolder.getAdapterPosition();
        final Doacao doacao = listaDoacao.get(position);

        if (!checarSeDoacaoEstaInteressada(doacao)) {

            final Interesse interesse = new Interesse();
            interesse.setOp_id(doacao.getOp_id());
            interesse.setUser_id(usuarioApp.getIdUsuario());
            interesse.setTime_stamp(0);
            interesse.setStopped_at(1);

            //EnviarEmail.enviarEmailInteresse(doacao.getTitulo(), doacao.getEmail(), doacao.getUser_id(), usuarioApp);

            //Salvar no firebase
            final DatabaseReference interesseRef = ConfiguracaoFirebase.getDatabaseReference().child("interesse");

            DatabaseReference interesseRefValue = interesseRef.push();

            final String interesse_id = interesseRefValue.getKey();

            interesseRefValue.setValue(interesse);

            Snackbar.make(viewHolder.itemView, "Interesse marcado com sucesso!", Snackbar.LENGTH_LONG)
                    .setAction("Desfazer", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PrincipalActivity.listaSalvos.remove(doacao);

                            interesseRef.child(interesse_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().removeValue();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Toast.makeText(getActivity(), "Desfeito.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                    .show();
        }
        else
            Toast.makeText(getActivity(), "Você já marcou interesse nessa doação.", Toast.LENGTH_SHORT).show();

        adapterDoacao.notifyDataSetChanged();
    }
}
