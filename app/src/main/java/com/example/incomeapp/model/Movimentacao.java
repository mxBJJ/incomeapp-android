package com.example.incomeapp.model;

import android.content.Intent;

import com.example.incomeapp.activity.DespesasActivity;
import com.example.incomeapp.activity.HomeActivity;
import com.example.incomeapp.config.ConfigFirebase;
import com.example.incomeapp.helper.Base64Custom;
import com.example.incomeapp.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private Double valor;
    private String key;


    public Movimentacao() {
    }


    public void saveMovimentacao(String dataString){

        FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAuth();

        String idUser = Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail());
        DatabaseReference databaseReference = ConfigFirebase.getDatabaseReference();

        databaseReference
                .child("movimentacao")
                .child(idUser)
                .child(DateCustom.formatDate(data))
                .push()
                .setValue(this);

    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
