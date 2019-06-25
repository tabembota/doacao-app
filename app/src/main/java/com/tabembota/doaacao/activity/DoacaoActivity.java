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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DoacaoActivity extends AppCompatActivity {

    Doacao doacao;
    TextView textTitulo, textDescricao, textData;
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
        textData = findViewById(R.id.textViewDoacaoData);
        image = findViewById(R.id.imageViewDoacaoFoto);

        textTitulo.setText(doacao.getTitulo());
        textDescricao.setText(doacao.getDescricao());

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String data = format.format(doacao.getData());
        textData.setText(data);

        image.setImageResource(doacao.getImagem());

    }

}
