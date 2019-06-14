package com.tabembota.doaacao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

import com.tabembota.doaacao.R;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.fragment.ConfiguracoesFragment;
import com.tabembota.doaacao.fragment.CriarOportunidadeFragment;
import com.tabembota.doaacao.fragment.ListaDoacoesFragment;
import com.tabembota.doaacao.fragment.SalvosFragment;


public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Componentes da interface
    private TextView textViewNavEmail, textViewNavName;

    //Inicializar todos os fragments que serão utilizados
    private static ListaDoacoesFragment listaDoacoesFragment = new ListaDoacoesFragment();
    private SalvosFragment salvosFragment = new SalvosFragment();
    private ConfiguracoesFragment configuracoesFragment = new ConfiguracoesFragment();
    private CriarOportunidadeFragment criarOportunidadeFragment = new CriarOportunidadeFragment();

    private String name = "", email = "";

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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        if (bundle != null){
            name = bundle.getString("LOGIN_NAME");
            email = bundle.getString("LOGIN_EMAIL");

            //Obtém view referente ao nav_header_main do navigation drawer
            View view = navigationView.getHeaderView(0);
            //Apontando para os TextViews em questão
            textViewNavEmail = view.findViewById(R.id.textViewNavEmail);
            textViewNavName = view.findViewById(R.id.textViewNavName);

            textViewNavEmail.setText(email);
            textViewNavName.setText(name);
        }
        else{
            Toast.makeText(this,
                    "Um erro aconteceu ao atualizar o nome e o email. Tente novamente.",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

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
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if(inputManager.isActive()){
            inputManager.hideSoftInputFromWindow(new View(this).getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }
}
