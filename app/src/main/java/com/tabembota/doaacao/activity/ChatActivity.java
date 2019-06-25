package com.tabembota.doaacao.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.tabembota.doaacao.R;
import com.tabembota.doaacao.adapter.AdapterMensagens;
import com.tabembota.doaacao.config.ConfiguracaoFirebase;
import com.tabembota.doaacao.helper.Base64Custom;
import com.tabembota.doaacao.helper.UsuarioFirebase;
import com.tabembota.doaacao.model.Doacao;
import com.tabembota.doaacao.model.Mensagem;
import com.tabembota.doaacao.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    //Informação do destinatário
    private String idUsuarioDestinatario;

    //Informação do remetente
    private String idUsuarioRemetente;
    private Usuario usuarioApp = UsuarioFirebase.getDadosUsuarioLogado();

    //Componentes da interface
    private TextView textViewNome;
    private EditText editMensagem;
    private RecyclerView recyclerMensagens;

    //Firebase
    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;

    //Recycler View
    private AdapterMensagens adapter;
    private List<Mensagem> listaMensagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar dados do usuário destinatário do intent
        Usuario usuarioDestinatario = (Usuario) getIntent().getSerializableExtra("USUARIO");
        idUsuarioDestinatario = usuarioDestinatario.getIdUsuario();
        Log.d("MISSGAY", idUsuarioDestinatario);

        //Configurações iniciais
        textViewNome = findViewById(R.id.textViewNomeChat);
        textViewNome.setText(usuarioDestinatario.getNome());

        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);

        //Recupera os dados do usuário remetente
        idUsuarioRemetente = usuarioApp.getIdUsuario();

        //Configurando a lista de mensagens
        configuraRecyclerView();

        database = ConfiguracaoFirebase.getDatabaseReference();
        mensagensRef = database.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

    }

    private void configuraRecyclerView(){
        //Configuração adapter
        adapter = new AdapterMensagens(listaMensagens, getApplicationContext());

        //Configuração recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapter);
    }

    public void enviarMensagem(View v){
        String textoMensagem = editMensagem.getText().toString();

        if(!textoMensagem.isEmpty()){
            Mensagem mensagem = new Mensagem();

            mensagem.setIdUsuario(idUsuarioRemetente);
            mensagem.setMensagem(textoMensagem);

            //Salvar mensagem para o remetente
            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);
            //Salvar mensagem para o destinatário
            salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);
        }
    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        database.child("mensagens")
                .child(idRemetente).child(idDestinatario)
                .push()
                .setValue(mensagem);

        //Limpar texto
        editMensagem.setText("");
    }

    private void recuperarMensagens(){

        listaMensagens.clear();

        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                listaMensagens.add(mensagem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                Log.d("analisando", "mudo!");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("analisando", "removeu!");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                Log.d("analisando", "moveu!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("analisando", "cancelo!");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }
}
