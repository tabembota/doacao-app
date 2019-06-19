package com.tabembota.doaacao.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tabembota.doaacao.R;
import com.tabembota.doaacao.model.Doacao;

import java.util.Date;

public class DoacaoActivity extends AppCompatActivity {

    Doacao doacao;
    TextView titulo, descricao, local, data, link;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doacao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        doacao = (Doacao) getIntent().getSerializableExtra("DOACAO");

        titulo = findViewById(R.id.textViewDoacaoTitulo);
        descricao = findViewById(R.id.textViewDoacaoDescricao);
        local = findViewById(R.id.textViewDoacaoLocal);
        data = findViewById(R.id.textViewDoacaoData);
        link = findViewById(R.id.textViewDoacaoLink);
        image = findViewById(R.id.imageViewDoacaoFoto);

        titulo.setText(doacao.getTitulo());
        descricao.setText(doacao.getDescricao());
        local.setText(doacao.getEmail());
        data.setText((new Date(doacao.getData())).toString());
        //link.setText(doacao.getFiltro());
        image.setImageResource(doacao.getImagem());

    }

}
