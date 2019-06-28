package com.example.incomeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.incomeapp.R;
import com.example.incomeapp.config.ConfigFirebase;
import com.example.incomeapp.helper.Base64Custom;
import com.example.incomeapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText password;
    private Button join;
    private FirebaseAuth firebaseAuth;
    private Usuario usuario;
    private View v;
    private FrameLayout frameLayout;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getSupportActionBar().hide();

        nome = findViewById(R.id.loginEmail);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.loginPassword);
        join = findViewById(R.id.join);
        v = findViewById(android.R.id.content);
        frameLayout = findViewById(R.id.frameLayout);
        progressBar = findViewById(R.id.progressBar);



        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                String textNome = nome.getText().toString();
                String textEmail = email.getText().toString();
                String textPassword = password.getText().toString();


                if (!textNome.isEmpty()) {
                    if (!textEmail.isEmpty()) {
                        if (!textPassword.isEmpty()) {
                            usuario = new Usuario();

                            usuario.setNome(textNome);
                            usuario.setEmail(textEmail);
                            usuario.setPassword(textPassword);

                            registerUser();

                        } else {
                            frameLayout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CadastroActivity.this, "Preencher o campo senha.", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        frameLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CadastroActivity.this, "Preencher o campo email.", Toast.LENGTH_LONG).show();

                    }

                } else {
                    frameLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CadastroActivity.this, "Preencher o campo nome.", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void registerUser() {

        firebaseAuth = ConfigFirebase.getFirebaseAuth();

        firebaseAuth.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    frameLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    String idUser = Base64Custom.encodeBase64(usuario.getEmail());
                    usuario.setIdUser(idUser);
                    usuario.saveUser();
                    finish();
                    openHome();

                } else {

                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        frameLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        excecao = "A senha é fraca demais";
                    } catch (FirebaseAuthEmailException e) {
                        frameLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        excecao = "O email digitado não é válido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        frameLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        excecao = "Usuário já cadastrado";
                    } catch (Exception e) {
                        frameLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    frameLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(v, "Erro ao cadastrar usuário.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void openHome() {
        startActivity(new Intent(CadastroActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_down);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
