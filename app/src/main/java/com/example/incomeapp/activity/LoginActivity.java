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
import com.example.incomeapp.model.Usuario;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Usuario user;
    private FirebaseAuth firebaseAuth;
    private View v;
    private FrameLayout frameLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        login = findViewById(R.id.signIn);
        v = findViewById(android.R.id.content);
        frameLayout = findViewById(R.id.frameLayout);
        progressBar = findViewById(R.id.progressBar);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                String textEmail = email.getText().toString();
                String textPassword = password.getText().toString();

                if (!textEmail.isEmpty()) {
                    if (!textPassword.isEmpty()) {

                        user = new Usuario();
                        user.setEmail(textEmail);
                        user.setPassword(textPassword);
                        validarLogin();

                    } else {
                        frameLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Senha não preenchida", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    frameLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Email não preenchido", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void validarLogin() {
        firebaseAuth = ConfigFirebase.getFirebaseAuth();

        firebaseAuth.signInWithEmailAndPassword(
                user.getEmail(), user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    frameLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    openHome();

                } else {
                    frameLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(v, "Erro ao efetuar login.", Snackbar.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void openHome() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_down);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
