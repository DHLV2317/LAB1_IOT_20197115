package com.example.lab1;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvPlayer, tvStart, tvGames;
    private LinearLayout llGamesList;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_profile);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        UiHelpers.wireToolbar(this, topAppBar);

        tvPlayer = findViewById(R.id.tvPlayer);
        tvStart  = findViewById(R.id.tvStart);
        tvGames  = findViewById(R.id.tvGames);
        llGamesList = findViewById(R.id.llGamesList);
        MaterialButton btnReset = findViewById(R.id.btnReset);

        renderStats();

        btnReset.setOnClickListener(v -> {
            Stats.reset(this);
            renderStats();
        });
    }

    private void renderStats(){
        tvPlayer.setText("Jugador: " + safe(Stats.player(this)));
        tvStart.setText("Inicio: " + Stats.lastStartHuman(this));
        tvGames.setText("Cantidad de Partidas: " + Stats.gamesCount(this));

        UiHelpers.fillGamesList(this, llGamesList, Stats.games(this));
    }

    private String safe(String s){ return s==null||s.isEmpty()?"—":s; }
}