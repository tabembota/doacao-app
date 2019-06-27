package com.tabembota.doaacao.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.fragment.ChatDoadoresFragment;
import com.tabembota.doaacao.fragment.ConfiguracoesFragment;
import com.tabembota.doaacao.fragment.CriarOportunidadeFragment;
import com.tabembota.doaacao.fragment.ListaDoacoesFragment;
import com.tabembota.doaacao.fragment.SalvosFragment;
import com.tabembota.doaacao.helper.UsuarioFirebase;
import com.tabembota.doaacao.model.Doacao;
import com.tabembota.doaacao.model.Interesse;
import com.tabembota.doaacao.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Componentes da interface
    private TextView textViewNavEmail, textViewNavName;

    //Inicializar todos os fragments que serão utilizados
    private static ListaDoacoesFragment listaDoacoesFragment = new ListaDoacoesFragment();
    private SalvosFragment salvosFragment = new SalvosFragment();
    private ConfiguracoesFragment configuracoesFragment = new ConfiguracoesFragment();
    private CriarOportunidadeFragment criarOportunidadeFragment = new CriarOportunidadeFragment();
    private ChatDoadoresFragment chatDoadoresFragment = new ChatDoadoresFragment();

    //Firebase
    private DatabaseReference referenciaSalvos = ConfiguracaoFirebase.getDatabaseReference();
    private ChildEventListener recuperarSalvosEventListener;
    private ChildEventListener childEventListenerDoacoes;
    private ChildEventListener childEventNovosInteresses;
    private Usuario usuarioApp = UsuarioFirebase.getDadosUsuarioLogado();
    private DatabaseReference doacoesRef;
    private DatabaseReference interessesNotifRef;

    //Variáveis quaisquer
    public static List<Doacao> listaSalvos = new ArrayList<>();
    public static List<Doacao> listaDoacoes = new ArrayList<>();
    private List<Interesse> listaInteresses = new ArrayList<>();

    private static String CHANNEL_ID = "NOTIFICACAO_INTERESSADOS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Criando canal da notificação
        createNotificationChannel();

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


        }
        //Caso contrario, a atividade será chamada de uma sub atividade (como detalhes doacao) e nao irá ter extras
        else{
            /*
            Toast.makeText(this,
                    "Um erro aconteceu ao atualizar o nome e o email. Tente novamente.",
                    Toast.LENGTH_SHORT).show();
            finish();*/
        }

        //Obtém view referente ao nav_header_main do navigation drawer
        View view = navigationView.getHeaderView(0);
        //Apontando para os TextViews em questão
        textViewNavEmail = view.findViewById(R.id.textViewNavEmail);
        textViewNavName = view.findViewById(R.id.textViewNavName);

        textViewNavEmail.setText(usuarioApp.getEmail());
        textViewNavName.setText(usuarioApp.getNome());

        recuperarListaSalvos();
        notificarNovosInteresses();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaListaDoacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        doacoesRef.removeEventListener(childEventListenerDoacoes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        referenciaSalvos.removeEventListener(recuperarSalvosEventListener);
        interessesNotifRef.removeEventListener(childEventNovosInteresses);
    }

    private void recuperarListaSalvos(){
        listaSalvos.clear();

        referenciaSalvos = referenciaSalvos.child("interesse");
        recuperarSalvosEventListener = referenciaSalvos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Interesse interesse = dataSnapshot.getValue(Interesse.class);

                if(interesse.getUser_id().equals(usuarioApp.getIdUsuario())){
                    DatabaseReference oportunidadeRef = ConfiguracaoFirebase.getDatabaseReference().child("oportunidade");

                    oportunidadeRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Doacao doacao = dataSnapshot.getValue(Doacao.class);
                            if(doacao.getOp_id().equals(interesse.getOp_id())){
                                listaSalvos.add(doacao);
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                            Doacao doacao = dataSnapshot.getValue(Doacao.class);
                            if(doacao.getOp_id().equals(interesse.getOp_id())){
                                listaSalvos.remove(doacao);
                            }
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

    private void recuperaListaDoacoes(){
        listaDoacoes.clear();

        doacoesRef = ConfiguracaoFirebase.getDatabaseReference().child("oportunidade");
        childEventListenerDoacoes = doacoesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Doacao doacao = dataSnapshot.getValue(Doacao.class);
                if(doacao.getUser_id().equals(usuarioApp.getIdUsuario())){
                    listaDoacoes.add(doacao);

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

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(!listaDoacoesFragment.isAdded()){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayoutMain, listaDoacoesFragment);
                ft.commit();
            }
            else{
                super.onBackPressed();
            }
        }
    }

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
        } else if(id == R.id.chat_doadores){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, chatDoadoresFragment);
            ft.commit();
        } else if (id == R.id.configuracoes) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, configuracoesFragment);
            ft.commit();

        } else if (id == R.id.sair) {
            ConfiguracaoFirebase.getFirebaseAuth().signOut();
            startActivity(new Intent(PrincipalActivity.this, MainIntroActivity.class));
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

    private void notificarNovosInteresses(){
        interessesNotifRef = ConfiguracaoFirebase.getDatabaseReference().child("interesse");
        interessesNotifRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Interesse interesse = data.getValue(Interesse.class);
                    listaInteresses.add(interesse);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        childEventNovosInteresses = interessesNotifRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Interesse interesse = dataSnapshot.getValue(Interesse.class);

                for(Doacao doacao : listaDoacoes){
                    for(Interesse interesseComp : listaInteresses){
                        if(interesse.getOp_id().equals(doacao.getOp_id()) && !interesse.equals(interesseComp)){

                            listaInteresses.add(interesse);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_stars_white_24dp)
                                    .setContentTitle("Doação: " + doacao.getTitulo())
                                    .setContentText("Há novos interessados em sua doação!")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                            // notificationId is a unique int for each notification that you must define
                            notificationManager.notify(0, builder.build());
                        }
                    }

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

            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
