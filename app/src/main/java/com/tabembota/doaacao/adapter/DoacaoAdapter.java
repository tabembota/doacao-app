package com.tabembota.doaacao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tabembota.doaacao.R;
import com.tabembota.doaacao.model.Doacao;

import java.util.List;

public class DoacaoAdapter extends RecyclerView.Adapter<DoacaoAdapter.MyViewHolder> {

    private List<Doacao> listaDoacoes;
    private Context context;

    public DoacaoAdapter(List<Doacao> listaDoacoes, Context context) {
        this.listaDoacoes = listaDoacoes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_adapter_doacao, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Doacao doacao = listaDoacoes.get(i);

        //myViewHolder.imagemDoacao.setImageResource(doacao.getImagem());
        myViewHolder.textoTitulo.setText(doacao.getTitulo());
        myViewHolder.textoDescricao.setText(doacao.getDescricao());
        //myViewHolder.textoData.setText(doacao.getData().toString());

    }

    @Override
    public int getItemCount() {
        return listaDoacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imagemDoacao;
        TextView textoTitulo, textoDescricao, textoData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagemDoacao = itemView.findViewById(R.id.imagemDoacao);
            textoTitulo = itemView.findViewById(R.id.textoTitulo);
            textoDescricao = itemView.findViewById(R.id.textoDescricao);
            textoData = itemView.findViewById(R.id.textoData);

        }
    }

}
