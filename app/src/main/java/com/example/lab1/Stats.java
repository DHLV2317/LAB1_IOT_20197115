package com.example.lab1;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class Stats {
    private static final String PREF = "telequiz.stats";
    private static final String K_PLAYER   = "player";
    private static final String K_GAMES    = "games_json";  // historial
    private static final String K_START_MS = "current_start_ms";
    private static final String K_TOPIC    = "current_topic";

    private static SharedPreferences sp(Context c){
        return c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    // --------- API pública ---------

    public static void start(Context c, String player, String topic){
        SharedPreferences p = sp(c);
        p.edit()
                .putString(K_PLAYER, player==null?"":player)
                .putLong(K_START_MS, System.currentTimeMillis())
                .putString(K_TOPIC, topic==null?"":topic)
                .apply();

        // agrega registro "en curso"
        List<Game> list = load(c);
        Game g = new Game();
        g.topic = topic;
        g.startMs = System.currentTimeMillis();
        g.endMs = 0;
        g.score = 0;
        g.status = "in_progress";
        list.add(g);
        save(c, list);
    }

    public static void finish(Context c, int finalScore){
        SharedPreferences p = sp(c);
        long startMs = p.getLong(K_START_MS, 0);
        String topic = p.getString(K_TOPIC, "—");

        List<Game> list = load(c);
        if (!list.isEmpty()){
            Game last = list.get(list.size()-1);
            if ("in_progress".equals(last.status)){
                last.endMs  = System.currentTimeMillis();
                last.score  = finalScore;
                last.status = "finished";
                save(c, list);
            }
        }
        // limpiar "en curso"
        p.edit().remove(K_START_MS).remove(K_TOPIC).apply();
    }

    public static void cancelCurrent(Context c){
        List<Game> list = load(c);
        if (!list.isEmpty()){
            Game last = list.get(list.size()-1);
            if ("in_progress".equals(last.status)){
                last.endMs  = System.currentTimeMillis();
                last.status = "canceled";
                save(c, list);
            }
        }
        sp(c).edit().remove(K_START_MS).remove(K_TOPIC).apply();
    }

    // Lecturas para la UI
    public static String player(Context c){
        return sp(c).getString(K_PLAYER,"");
    }

    public static int gamesCount(Context c){
        return load(c).size();
    }

    public static List<Game> games(Context c){
        return load(c);
    }

    public static String lastStartHuman(Context c){
        long start = sp(c).getLong(K_START_MS, 0);
        if (start==0 && !load(c).isEmpty())
            start = load(c).get(load(c).size()-1).startMs;
        return start==0 ? "—" : DateFormat.getDateTimeInstance().format(new Date(start));
    }

    // --------- Persistencia ---------

    public static class Game {
        public String topic;
        public long startMs;
        public long endMs;
        public int score;
        public String status; // in_progress | finished | canceled

        public long durationSec(){
            long end = (endMs==0)?System.currentTimeMillis():endMs;
            return Math.max(0, (end - startMs)/1000);
        }
    }

    private static List<Game> load(Context c){
        ArrayList<Game> out = new ArrayList<>();
        String json = sp(c).getString(K_GAMES,"[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i=0;i<arr.length();i++){
                JSONObject o = arr.getJSONObject(i);
                Game g = new Game();
                g.topic   = o.optString("topic","—");
                g.startMs = o.optLong("start",0);
                g.endMs   = o.optLong("end",0);
                g.score   = o.optInt("score",0);
                g.status  = o.optString("status","finished");
                out.add(g);
            }
        } catch (JSONException ignored) {}
        return out;
    }

    private static void save(Context c, List<Game> list){
        JSONArray arr = new JSONArray();
        try {
            for (Game g : list){
                JSONObject o = new JSONObject();
                o.put("topic", g.topic);
                o.put("start", g.startMs);
                o.put("end",   g.endMs);
                o.put("score", g.score);
                o.put("status", g.status);
                arr.put(o);
            }
        } catch (JSONException ignored) {}
        sp(c).edit().putString(K_GAMES, arr.toString()).apply();
    }

    // ✅ Reset solo borra partidas, mantiene nombre del jugador
    public static void reset(Context c){
        sp(c).edit().remove(K_GAMES).apply();
    }
}