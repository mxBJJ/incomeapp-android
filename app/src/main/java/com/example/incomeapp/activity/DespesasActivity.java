package com.example.incomeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.incomeapp.R;
import com.example.incomeapp.config.ConfigFirebase;
import com.example.incomeapp.helper.Base64Custom;
import com.example.incomeapp.helper.DateCustom;
import com.example.incomeapp.model.Movimentacao;
import com.example.incomeapp.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText fieldDate, fieldDescription, fieldCategory;
    private EditText value;
    private FloatingActionButton save;
    private Movimentacao movimentacao;
    private FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAuth();
    private DatabaseReference databaseReference = ConfigFirebase.getDatabaseReference();
    private Double despesaTotal;
    private Double despesaAtualizada;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);
        getSupportActionBar().hide();

        back = findViewById(R.id.arrowBack);
        fieldDate = findViewById(R.id.editDate);
        fieldDescription = findViewById(R.id.editDescription);
        fieldCategory = findViewById(R.id.editCategory);
        value = findViewById(R.id.editValue);
        save = findViewById(R.id.fabSaveExpense);

        fieldDate.setText(DateCustom.currentDate());

        recuperarDespesaTotal();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!fieldDescription.getText().toString().isEmpty()) {
                    if (!fieldCategory.getText().toString().isEmpty()) {

                        String data = fieldDate.getText().toString();
                        Double valorRecuperado = Double.parseDouble(value.getText().toString());
                        movimentacao = new Movimentacao();
                        movimentacao.setValor(valorRecuperado);
                        movimentacao.setCategoria(fieldCategory.getText().toString());
                        movimentacao.setDescricao(fieldDescription.getText().toString());
                        movimentacao.setData(data);
                        movimentacao.setTipo("d");

                        despesaAtualizada = despesaTotal + valorRecuperado;
                        atualizarDespesa(despesaAtualizada);

                        movimentacao.saveMovimentacao(data);
                        Toast.makeText(getApplicationContext(), "Despesa salva com sucesso!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Preencher o campo categoria!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Preencher o campo descrição!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void recuperarDespesaTotal() {

        String idUser = Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail());
        DatabaseReference userRef = databaseReference.child("usuarios").child(idUser);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                despesaTotal = user.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarDespesa(Double despesa) {
        String idUser = Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail());
        DatabaseReference userRef = databaseReference.child("usuarios").child(idUser);

        userRef.child("despesaTotal").setValue(despesa);
    }
}