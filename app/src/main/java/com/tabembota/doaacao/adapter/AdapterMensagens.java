package com.tabembota.doaacao.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tabembota.doaacao.R;
import com.tabembota.doaacao.helper.UsuarioFirebase;
import com.tabembota.doaacao.model.Mensagem;

import java.util.List;


public class AdapterMensagens extends RecyclerView.Adapter<AdapterMensagens.MyViewHolder> {

    private List<Mensagem> listaMensagens;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;


    public AdapterMensagens(List<Mensagem> listaMensagens, Context context) {
        this.listaMensagens = listaMensagens;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item = null;

        if(i == TIPO_REMETENTE){

            item = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_mensagem_remetente, viewGroup, false);

        }else if(i == TIPO_DESTINATARIO){

            item = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_mensagem_destinatario, viewGroup, false);

        }

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Mensagem mensagem = listaMensagens.get(i);
        String msg = mensagem.getMensagem();

        myViewHolder.mensagem.setText(msg);
    }


    @Override
    public int getItemCount() {
        return listaMensagens.size();
    }

    @Override
    public int getItemViewType(int position) {

        Mensagem mensagem = listaMensagens.get(position);
        String idUsuario = UsuarioFirebase.getDadosUsuarioLogado().getIdUsuario();

        if(idUsuario.equals(mensagem.getIdUsuario())){
            return TIPO_REMETENTE;
        }

        return TIPO_DESTINATARIO;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mensagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mensagem = itemView.findViewById(R.id.textMensagemTexto);

        }
    }

}

