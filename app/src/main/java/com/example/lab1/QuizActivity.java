package com.example.lab1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvTopic, tvPrompt, tvPerScore, tvScoreBox;
    private RadioGroup rg;
    private final RadioButton[] rb = new RadioButton[4];
    private MaterialButton btnPrev, btnNext, btnHint;

    private String topic, playerName;
    private List<Question> questions = new ArrayList<>();
    private int pos = 0, score = 0, streak = 0, hintsLeft = 3;
    private final int[] chosen = new int[7];
    private final int[] perQuestion = new int[7];
    private final boolean[] hintUsed = new boolean[7];

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_quiz);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        UiHelpers.wireToolbar(this, topAppBar);

        tvTopic    = findViewById(R.id.tvTopic);
        tvPrompt   = findViewById(R.id.tvPrompt);
        tvPerScore = findViewById(R.id.tvPerScore);
        tvScoreBox = findViewById(R.id.tvScoreBox);

        rg   = findViewById(R.id.rgOptions);
        rb[0]= findViewById(R.id.rb0);
        rb[1]= findViewById(R.id.rb1);
        rb[2]= findViewById(R.id.rb2);
        rb[3]= findViewById(R.id.rb3);

        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnHint = findViewById(R.id.btnHint);

        Arrays.fill(chosen, -1);

        topic = getIntent().getStringExtra("topic");
        playerName = getIntent().getStringExtra("playerName");
        tvTopic.setText(topic == null ? "—" : topic);
        tvScoreBox.setText("0");

        questions = QuestionBank.getFor(topic);
        if (questions == null || questions.isEmpty()) {
            Toast.makeText(this, "No hay preguntas para el tema", Toast.LENGTH_LONG).show();
            finish(); return;
        }

        rg.setOnCheckedChangeListener((g, checkedId) -> {
            int selected = indexFromId(checkedId);
            if (selected >= 0 && chosen[pos] == -1) evaluarSeleccion(selected);
        });

        btnPrev.setOnClickListener(v -> { if (pos > 0) { pos--; render(); } });
        btnNext.setOnClickListener(v -> {
            if (pos < questions.size() - 1) { pos++; render(); }
            else { goToResults(); }
        });
        btnHint.setOnClickListener(v -> useHint());

        render();
    }

    private int indexFromId(int id){
        if (id == rb[0].getId()) return 0;
        if (id == rb[1].getId()) return 1;
        if (id == rb[2].getId()) return 2;
        if (id == rb[3].getId()) return 3;
        return -1;
    }

    private void evaluarSeleccion(int selected){
        Question q = questions.get(pos);
        boolean ok = selected == q.correctIndex;
        int delta = calcDelta(ok);

        chosen[pos] = selected;
        perQuestion[pos] = delta;
        score += delta;

        tvScoreBox.setText(String.valueOf(score));
        tvPerScore.setText("Puntaje pregunta: " + delta);
        colorize(ok);
        btnNext.setEnabled(true);
    }

    // +2 / -3 y se duplica con racha de la misma señal
    private int calcDelta(boolean correct){
        int base = correct ? 2 : -3;
        int delta;
        if (streak == 0) { delta = base; streak = correct ? 1 : -1; }
        else if ((streak > 0 && correct) || (streak < 0 && !correct)) {
            delta = (int)(base * Math.pow(2, Math.abs(streak)));
            streak += correct ? 1 : -1;
        } else { delta = base; streak = correct ? 1 : -1; }
        return delta;
    }

    private void render(){
        Question q = questions.get(pos);

        tvPrompt.setText((pos + 1) + "/7. " + q.prompt);
        tvPerScore.setText("Puntaje pregunta: " + (chosen[pos] == -1 ? 0 : perQuestion[pos]));
        tvScoreBox.setText(String.valueOf(score));

        rg.setOnCheckedChangeListener(null);
        rg.clearCheck();
        for (int i = 0; i < 4; i++) {
            rb[i].setText(q.options.get(i));
            rb[i].setEnabled(true);
            rb[i].setTextColor(Color.BLACK);
        }

        if (chosen[pos] != -1) {
            rb[chosen[pos]].setChecked(true);
            colorize(chosen[pos] == q.correctIndex);
        }

        rg.setOnCheckedChangeListener((g, checkedId) -> {
            int sel = indexFromId(checkedId);
            if (sel >= 0 && chosen[pos] == -1) evaluarSeleccion(sel);
        });

        btnPrev.setEnabled(pos > 0);
        btnNext.setEnabled(chosen[pos] != -1);
        btnNext.setText(pos == questions.size() - 1 ? getString(R.string.btn_finish) : getString(R.string.btn_next));
        btnHint.setEnabled(!hintUsed[pos] && hintsLeft > 0);
    }

    private void colorize(boolean correct){
        int okColor = Color.parseColor("#2E7D32");
        int badColor = Color.parseColor("#C62828");
        int right = questions.get(pos).correctIndex;
        for (int i = 0; i < 4; i++) {
            if (i == right) rb[i].setTextColor(okColor);
            else if (chosen[pos] == i) rb[i].setTextColor(badColor);
            rb[i].setEnabled(false);
        }
    }

    private void useHint(){
        if (hintUsed[pos] || hintsLeft <= 0) return;
        List<Integer> wrongs = new ArrayList<>();
        int right = questions.get(pos).correctIndex;
        for (int i = 0; i < 4; i++) if (i != right) wrongs.add(i);
        Collections.shuffle(wrongs);
        rb[wrongs.get(0)].setEnabled(false);

        hintUsed[pos] = true;
        hintsLeft--;
        btnHint.setEnabled(false);
        Toast.makeText(this, "Pistas restantes: " + hintsLeft, Toast.LENGTH_SHORT).show();
    }

    private void goToResults(){
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("finalScore", score);
        i.putExtra("playerName", playerName);
        i.putExtra("topic", topic);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Si sales desde el quiz, se considera cancelado
        Stats.cancelCurrent(this);
        super.onBackPressed();
    }
}