package com.example.incomeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.incomeapp.R;
import com.example.incomeapp.adapter.MovimentacaoAdapter;
import com.example.incomeapp.config.ConfigFirebase;
import com.example.incomeapp.helper.Base64Custom;
import com.example.incomeapp.model.Movimentacao;
import com.example.incomeapp.model.Usuario;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FloatingActionButton abrirDespesas;
    private FloatingActionButton abrirSaldos;
    private DatabaseReference databaseReference = ConfigFirebase.getDatabaseReference();
    private FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAuth();
    private TextView textWellcome, textFullValue;
    private Double despesaTotal = 0.00;
    private Double receitaTotal = 0.00;
    private Double fullValue = 0.00;
    private String userName;
    private DatabaseReference userRef;
    private ValueEventListener valueEventListener;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MovimentacaoAdapter adapter;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;
    private MaterialCalendarView calendarView;
    private ValueEventListener valueEventListenerMovimentacoes;
    private ProgressBar progressBar;
    private Movimentacao movimentacao;
    private BottomNavigationView bottomNavigationView;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_logout:
                logout();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.menu_config:
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        firebaseAuth.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarHome();
        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, 3000);
        recuperarMovimentacoes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setElevation(0);

//        abrirDespesas = findViewById(R.id.fab);
//        abrirSaldos = findViewById(R.id.fab2);
        textWellcome = findViewById(R.id.textUserName);
        textFullValue = findViewById(R.id.textFullValue);
        recyclerView = findViewById(R.id.recyclerView);
        calendarView = findViewById(R.id.calendarView);
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.main_menu);


        swipe();
        configCalendar();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new MovimentacaoAdapter(movimentacoes, this);

        recyclerView.setAdapter(adapter);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.saldosItem:
                        abrirSaldos();
                        break;

                    case R.id.despesaItem:
                        abrirDespesas();
                        break;

                }

                return false;
            }
        });


//
//        abrirSaldos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                abrirSaldos();
//            }
//        });
//
//
//        abrirDespesas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                abrirDespesas();
//            }
//        });


    }


    public void recuperarHome() {

        String idUser = Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail());

        userRef = databaseReference.child("usuarios").child(idUser);

        valueEventListener = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);

                despesaTotal = user.getDespesaTotal();
                receitaTotal = user.getReceitaTotal();

                fullValue = receitaTotal - despesaTotal;

                textWellcome.setText("Olá, " + user.getNome());

                DecimalFormat decimalFormat = new DecimalFormat("#.##");

                String fullValueResult = decimalFormat.format(fullValue);

                textFullValue.setText("R$  " + fullValueResult);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void abrirSaldos() {
        startActivity(new Intent(HomeActivity.this, SaldosActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }


    public void abrirDespesas() {

        startActivity(new Intent(HomeActivity.this, DespesasActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void configCalendar() {
        CharSequence meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        calendarView.setTitleMonths(meses);

        CalendarDay dataAtual = calendarView.getCurrentDate();

        String mes = String.format("%02d", dataAtual.getMonth() + 1);

        mesAnoSelecionado = String.valueOf(mes + "" + dataAtual.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                String mes = String.format("%02d", date.getMonth() + 1);

                mesAnoSelecionado = String.valueOf(mes + "" + date.getYear());
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);


                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                }, 3000);

                progressBar.setVisibility(View.VISIBLE);
                recuperarMovimentacoes();
            }
        });

    }

    public void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertDiaglog = new AlertDialog.Builder(this);

        alertDiaglog.setTitle("Excluir Movimentação da conta");
        alertDiaglog.setMessage("Você tem certeza que deseja exluir essa movimentação da sua conta?");
        alertDiaglog.setCancelable(false);


        alertDiaglog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(position);

                String idUser = Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail());

                movimentacaoRef = databaseReference.child("movimentacao")
                        .child(idUser)
                        .child(mesAnoSelecionado);

                movimentacaoRef.child(movimentacao.getKey()).removeValue();

                adapter.notifyItemRemoved(position);
                atualizarSaldo();
            }
        });

        alertDiaglog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(HomeActivity.this, "Exclusão cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alert = alertDiaglog.create();
        alert.show();
        recuperarMovimentacoes();


    }

    private void atualizarSaldo() {

        String idUser = Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail());
        userRef = databaseReference.child("usuarios").child(idUser);

        if (movimentacao.getTipo().equals("r")) {
            receitaTotal = receitaTotal - movimentacao.getValor();
            userRef.child("receitaTotal").setValue(receitaTotal);

        }

        if (movimentacao.getTipo().equals("d")) {
            despesaTotal = despesaTotal - movimentacao.getValor();
            userRef.child("despesaTotal").setValue(despesaTotal);
        }
    }

    public void recuperarMovimentacoes() {

        String idUser = Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail());

        movimentacaoRef = databaseReference.child("movimentacao")
                .child(idUser)
                .child(mesAnoSelecionado);


        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                movimentacoes.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);

                    movimentacao.setKey(dados.getKey());
                    movimentacoes.add(movimentacao);


                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListener);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }
}
