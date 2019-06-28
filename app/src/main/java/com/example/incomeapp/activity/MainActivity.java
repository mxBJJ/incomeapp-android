package com.example.incomeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.incomeapp.R;
import com.example.incomeapp.activity.CadastroActivity;
import com.example.incomeapp.activity.LoginActivity;
import com.example.incomeapp.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {
    private TextView btnLogin;
    private Button btnJoinUs;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onStart() {
        super.onStart();
        verifyUserLogin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        btnJoinUs = findViewById(R.id.btnJoinUs);
        btnLogin = findViewById(R.id.btnLogin);

        setButtonBackVisible(false);
        setButtonNextVisible(false);
        setPageScrollDuration(500);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.backgroundApp)
                .backgroundDark(R.color.indicatorOff)
                .fragment(R.layout.tutorial_1)
                .canGoForward(true)
                .canGoBackward(false)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(R.color.backgroundApp)
                .backgroundDark(R.color.indicatorOff)
                .fragment(R.layout.tutorial_2)
                .canGoForward(true)
                .canGoBackward(true)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(R.color.backgroundApp)
                .backgroundDark(R.color.indicatorOff)
                .fragment(R.layout.tutorial_3)
                .canGoForward(true)
                .canGoBackward(true)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(R.color.backgroundApp)
                .backgroundDark(R.color.indicatorOff)
                .fragment(R.layout.tutorial_4)
                .canGoForward(true)
                .canGoBackward(true)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .fragment(R.layout.cadastro_intro)
                .background(R.color.backgroundApp)
                .backgroundDark(R.color.indicatorOff)
                .canGoForward(false)
                .build());

    }

    private void verifyUserLogin() {
        firebaseAuth = ConfigFirebase.getFirebaseAuth();
        if (firebaseAuth.getCurrentUser() != null) {
            openHome();

        }
    }

    private void openHome() {
        startActivity(new Intent(MainActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_down);
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }

    public void joinUs(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

}
