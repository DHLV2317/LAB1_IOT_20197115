package com.example.lab1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import java.text.DateFormat;
import java.util.List;

public final class UiHelpers {

    /** Conecta navegación, infla SIEMPRE el menú y maneja el botón de Perfil. */
    public static void wireToolbar(Activity a, MaterialToolbar toolbar){
        // Flecha (atrás en el back stack)
        toolbar.setNavigationOnClickListener(v -> a.onBackPressed());

        // Asegura el menú (por si el layout no lo infló)
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_main);

        // Click en "perfil/estadísticas"
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_stats) {
                a.startActivity(new Intent(a, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    /** Llena la lista de partidas con color por estado. */
    public static void fillGamesList(android.content.Context c, LinearLayout container, List<Stats.Game> games){
        container.removeAllViews();
        DateFormat df = DateFormat.getDateTimeInstance();
        int i = 1;
        for (Stats.Game g : games){
            TextView tv = new TextView(c);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            String estado;
            int color;
            if ("in_progress".equals(g.status)) {
                estado = " | En curso";
                color = Color.parseColor("#FFA000"); // ámbar
            } else if ("canceled".equals(g.status)) {
                estado = " | Canceló";
                color = Color.parseColor("#212121"); // gris oscuro
            } else {
                estado = " | Puntaje: " + g.score;
                color = (g.score >= 0) ? Color.parseColor("#2E7D32") : Color.parseColor("#C62828");
            }

            tv.setText("Juego " + (i++) + ": " + g.topic +
                    " | Inicio: " + df.format(g.startMs) + estado);
            tv.setTextSize(14f);
            tv.setPadding(8, 8, 8, 8);
            tv.setTextColor(color);
            if ("finished".equals(g.status) && g.score >= 0) tv.setTypeface(Typeface.DEFAULT_BOLD);

            container.addView(tv);
        }
    }

    private UiHelpers() {}
}