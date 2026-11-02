package com.dinux.wabanchecker;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;

public class HistoryStore {
    private static final String PREF = "dinux_history";
    private static final String KEY = "checks";

    public static JSONArray get(Context c) {
        String raw = c.getSharedPreferences(PREF, 0).getString(KEY, "[]");
        try { return new JSONArray(raw); }
        catch (Exception e) { return new JSONArray(); }
    }

    public static void add(Context c, JSONObject response) {
        try {
            JSONArray old = get(c);
            JSONArray next = new JSONArray();
            JSONObject row = new JSONObject();
            row.put("number", response.optString("number", ""));
            row.put("status", response.optString("status", "UNKNOWN"));
            row.put("details", response.optString("details", ""));
            row.put("accountType", response.optString("accountType", "Unknown"));
            row.put("cc", response.optString("cc", ""));
            row.put("time", System.currentTimeMillis());
            next.put(row);
            for (int i = 0; i < old.length() && i < 49; i++) next.put(old.getJSONObject(i));
            save(c, next);
        } catch (Exception ignored) { }
    }

    public static void clear(Context c) { save(c, new JSONArray()); }

    private static void save(Context c, JSONArray a) {
        SharedPreferences.Editor e = c.getSharedPreferences(PREF, 0).edit();
        e.putString(KEY, a.toString());
        e.apply();
    }
}
