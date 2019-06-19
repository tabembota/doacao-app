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
    TextView textTitulo, textDescricao, textLocal, textData, textLink;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doacao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        doacao = (Doacao) getIntent().getSerializableExtra("DOACAO");

        textTitulo = findViewById(R.id.textViewDoacaoTitulo);
        textDescricao = findViewById(R.id.textViewDoacaoDescricao);
        textLocal = findViewById(R.id.textViewDoacaoLocal);
        textData = findViewById(R.id.textViewDoacaoData);
        textLink = findViewById(R.id.textViewDoacaoLink);
        image = findViewById(R.id.imageViewDoacaoFoto);

        textTitulo.setText(doacao.getTitulo());
        textDescricao.setText(doacao.getDescricao());
        textLocal.setText(doacao.getEmail());
        textData.setText((new Date(doacao.getData())).toString());
        //link.setText(doacao.getFiltro());
        image.setImageResource(doacao.getImagem());

    }

}
