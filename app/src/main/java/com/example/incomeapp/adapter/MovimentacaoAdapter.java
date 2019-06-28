package com.example.incomeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.incomeapp.R;
import com.example.incomeapp.model.Movimentacao;

import java.text.DecimalFormat;
import java.util.List;
import java.util.zip.Inflater;

public class MovimentacaoAdapter extends RecyclerView.Adapter<MovimentacaoAdapter.MyViewHolder> {

    List<Movimentacao> movimentacaoList;
    Context context;


    public MovimentacaoAdapter(List<Movimentacao> movimentacaoList, Context context) {
        this.movimentacaoList = movimentacaoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(context).inflate(R.layout.it_card, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Movimentacao movimentacao = movimentacaoList.get(position);

        holder.categoria.setText(movimentacao.getCategoria());
        holder.descricao.setText(movimentacao.getDescricao());
        holder.data.setText(movimentacao.getData());


        if (movimentacao.getTipo() == "d" || movimentacao.getTipo().equals("d")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.valor.setText(" -" + movimentacao.getValor());
        }else{
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            holder.valor.setTextColor(context.getResources().getColor(R.color.positive));
            holder.valor.setText("R$ " + String.valueOf(decimalFormat.format(movimentacao.getValor())));
        }
    }

    @Override
    public int getItemCount() {
        return movimentacaoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricao, categoria, valor, data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.description);
            categoria = itemView.findViewById(R.id.category);
            valor = itemView.findViewById(R.id.value);
            data = itemView.findViewById(R.id.data);
        }
    }

}


