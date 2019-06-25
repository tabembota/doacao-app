package com.tabembota.doaacao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tabembota.doaacao.R;
import com.tabembota.doaacao.model.Usuario;

import java.util.List;

public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.MyViewHolder> {

    private List<Usuario> listaUsuarios;
    private Context context;

    public AdapterUsuarios(List<Usuario> listaUsuarios, Context context) {
        this.listaUsuarios = listaUsuarios;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_adapter_usuario, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Usuario usuario = listaUsuarios.get(i);

        myViewHolder.email.setText(usuario.getEmail());
        myViewHolder.nome.setText(usuario.getNome());

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nome, email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNome);
            email = itemView.findViewById(R.id.textEmail);
        }
    }
}
