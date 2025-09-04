package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        UiHelpers.wireToolbar(this, topAppBar);

        int finalScore = getIntent().getIntExtra("finalScore", 0);
        String topic   = getIntent().getStringExtra("topic");

        // cierra registro de partida
        Stats.finish(this, finalScore);

        TextView tvTopicResult = findViewById(R.id.tvTopicResult);
        TextView tvScoreBig    = findViewById(R.id.tvScoreBig);
        MaterialButton btnPrev   = findViewById(R.id.btnResultPrev);
        MaterialButton btnReplay = findViewById(R.id.btnResultReplay);

        tvTopicResult.setText(topic == null ? "—" : topic);
        tvScoreBig.setText(String.valueOf(finalScore));

        btnPrev.setOnClickListener(v -> finish());
        btnReplay.setOnClickListener(v -> {
            Intent i = new Intent(this, TopicActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
    }
}