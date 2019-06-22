package com.tabembota.doaacao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.fragment.ConfiguracoesFragment;
import com.tabembota.doaacao.fragment.CriarOportunidadeFragment;
import com.tabembota.doaacao.fragment.ListaDoacoesFragment;
import com.tabembota.doaacao.fragment.SalvosFragment;
import com.tabembota.doaacao.helper.UsuarioFirebase;
import com.tabembota.doaacao.model.Doacao;
import com.tabembota.doaacao.model.Interesse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.*;


public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Componentes da interface
    private TextView textViewNavEmail, textViewNavName;

    //Inicializar todos os fragments que serão utilizados
    private static ListaDoacoesFragment listaDoacoesFragment = new ListaDoacoesFragment();
    private SalvosFragment salvosFragment = new SalvosFragment();
    private ConfiguracoesFragment configuracoesFragment = new ConfiguracoesFragment();
    private CriarOportunidadeFragment criarOportunidadeFragment = new CriarOportunidadeFragment();

    //Firebase
    private DatabaseReference referenciaSalvos = ConfiguracaoFirebase.getDatabaseReference();
    private ChildEventListener recuperarSalvosEventListener;

    //Variáveis quaisquer
    public List<Doacao> listaSalvos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                fecharTeclado();
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Colocando o primeiro item do Navigation Drawer como selecionado
        navigationView.setCheckedItem(R.id.lista_doacoes);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutMain, listaDoacoesFragment);
        ft.commit();

        //Atualiza Navigation Drawer
        //Obtém dados passados pelo intent
        Bundle bundle = getIntent().getExtras();
        //Extras iram existir se for a primeira o login chamar essa activity
        if (bundle != null){
            String name = bundle.getString("LOGIN_NAME");
            String email = bundle.getString("LOGIN_EMAIL");

            //Obtém view referente ao nav_header_main do navigation drawer
            View view = navigationView.getHeaderView(0);
            //Apontando para os TextViews em questão
            textViewNavEmail = view.findViewById(R.id.textViewNavEmail);
            textViewNavName = view.findViewById(R.id.textViewNavName);

            textViewNavEmail.setText(email);
            textViewNavName.setText(name);
        }
        //Caso contrario, a atividade será chamada de uma sub atividade (como detalhes doacao) e nao irá ter extras
        else{
            /*
            Toast.makeText(this,
                    "Um erro aconteceu ao atualizar o nome e o email. Tente novamente.",
                    Toast.LENGTH_SHORT).show();
            finish();*/
        }

        recuperarListaSalvos();

    }

    private void recuperarListaSalvos(){
        referenciaSalvos = referenciaSalvos.child("interesse");
        recuperarSalvosEventListener = referenciaSalvos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Interesse interesse = dataSnapshot.getValue(Interesse.class);
                Log.d("MISSGAY", interesse.getUser_id() + " " + interesse.getOp_id() + " " + interesse.getStopped_at());
                if(interesse.getUser_id().equals(UsuarioFirebase.getUsuarioAtual().getUid())){
                    DatabaseReference oportunidadeRef = ConfiguracaoFirebase.getDatabaseReference().child("oportunidade");
                    oportunidadeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Doacao doacao = dataSnapshot.getValue(Doacao.class);
                            listaSalvos.add(doacao);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
                Log.d("DatabaseError", "Erro: " + databaseError.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        referenciaSalvos.removeEventListener(recuperarSalvosEventListener);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.lista_doacoes){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, listaDoacoesFragment);
            ft.commit();

        }
        else if (id == R.id.itens_salvos) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, salvosFragment);
            ft.commit();
        } else if (id == R.id.criar_oportunidade){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, criarOportunidadeFragment);
            ft.commit();
        } else if (id == R.id.configuracoes) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, configuracoesFragment);
            ft.commit();

        } else if (id == R.id.sair) {
            ConfiguracaoFirebase.getFirebaseAuth().signOut();
            startActivity(new Intent(PrincipalActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        fecharTeclado();
        return true;
    }


    public static ListaDoacoesFragment getListaDoacoesFragment(){
        return listaDoacoesFragment;
    }

    public void mudarTitulo(String titulo){
        getSupportActionBar().setTitle(titulo);
    }

    public void fecharTeclado(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }
}
