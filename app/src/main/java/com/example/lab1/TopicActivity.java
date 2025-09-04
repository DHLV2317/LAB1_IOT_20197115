package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class TopicActivity extends AppCompatActivity {

    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        UiHelpers.wireToolbar(this, topAppBar);

        playerName = getIntent().getStringExtra("playerName");
        TextView tvPlayer = findViewById(R.id.tvPlayerName);
        tvPlayer.setText(playerName == null || playerName.isEmpty() ? "<Nombre>" : playerName);

        findViewById(R.id.btnRedes).setOnClickListener(v -> openQuiz("Redes"));
        findViewById(R.id.btnCyber).setOnClickListener(v -> openQuiz("Ciberseguridad"));
        findViewById(R.id.btnMicro).setOnClickListener(v -> openQuiz("Microondas"));
    }

    private void openQuiz(String topic) {
        // marca inicio de partida
        Stats.start(this, playerName, topic);

        Intent i = new Intent(this, QuizActivity.class);
        i.putExtra("playerName", playerName);
        i.putExtra("topic", topic);
        startActivity(i);
    }
}