package com.tabembota.doaacao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tabembota.doaacao.R;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.fragment.ConfiguracoesFragment;
import com.tabembota.doaacao.fragment.CriarOportunidadeFragment;
import com.tabembota.doaacao.fragment.PrincipalFragment;
import com.tabembota.doaacao.fragment.SalvosFragment;
import com.tabembota.doaacao.helper.UsuarioFirebase;

import java.security.Principal;

public class ListaDoacoesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private TextView textViewNavEmail, textViewNavName;

    //Inicializar todos os fragments que serão utilizados
    private static PrincipalFragment principalFragment = new PrincipalFragment();
    private SalvosFragment salvosFragment = new SalvosFragment();
    private ConfiguracoesFragment configuracoesFragment = new ConfiguracoesFragment();
    private CriarOportunidadeFragment criarOportunidadeFragment = new CriarOportunidadeFragment();

    private String name ="", email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        ft.replace(R.id.frameLayoutMain, principalFragment);
        ft.commit();

        //Faz Login
        /*Intent i = new Intent(this, LoginActivity.class);
        String name = "", email = "";
        startActivityForResult(i, 100);*/

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

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 100){
                name = data.getStringExtra("LOGIN_NAME");
                email = data.getStringExtra("LOGIN_EMAIL");

                TextView textViewNavEmail = findViewById(R.id.textViewNavEmail);
                TextView textViewNavName = findViewById(R.id.textViewNavName);
                textViewNavEmail.setText(email);
                textViewNavName.setText(name);
            }
        }
    }*/

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
            ft.replace(R.id.frameLayoutMain, principalFragment);
            ft.commit();

        }
        else if (id == R.id.itens_salvos) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, salvosFragment);
            ft.commit();

        } else if (id == R.id.filtrar_itens) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, configuracoesFragment);
            ft.commit();
        } else if (id == R.id.criar_oportunidade){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, criarOportunidadeFragment);
            ft.commit();
        } else if (id == R.id.configuracoes) {


        } else if (id == R.id.sair) {
            ConfiguracaoFirebase.getFirebaseAuth().signOut();
            startActivity(new Intent(ListaDoacoesActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static PrincipalFragment getPrincipalFragment(){
        return principalFragment;
    }
}
