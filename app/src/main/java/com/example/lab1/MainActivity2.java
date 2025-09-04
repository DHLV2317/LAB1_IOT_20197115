package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); // usa el nombre exacto de tu XML

        etName = findViewById(R.id.etName);
        MaterialButton btn = findViewById(R.id.btnStart);

        btn.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                etName.setError("Ingresa tu nombre");
                return;
            }

            Intent i = new Intent(this, TopicActivity.class);
            i.putExtra("playerName", name);
            startActivity(i);
        });
    }
}