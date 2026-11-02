package com.dinux.wabanchecker;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryActivity extends Activity {
    private int dp(int x) { return (int)(x * getResources().getDisplayMetrics().density + 0.5f); }

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        showHistory();
    }

    private void showHistory() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(22), dp(18), dp(18));
        root.setBackgroundColor(Color.rgb(11,20,26));

        TextView title = new TextView(this);
        title.setText("Recent checks");
        title.setTextSize(25);
        title.setTextColor(Color.WHITE);
        title.setTypeface(null, 1);
        root.addView(title);

        Button clear = new Button(this);
        clear.setText("Clear history");
        clear.setAllCaps(false);
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(HistoryActivity.this)
                    .setTitle("Clear all history?")
                    .setMessage("Saved check records will be deleted from this device.")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Clear", new android.content.DialogInterface.OnClickListener() {
                        public void onClick(android.content.DialogInterface d, int w) {
                            HistoryStore.clear(HistoryActivity.this);
                            showHistory();
                        }
                    }).show();
            }
        });
        root.addView(clear);

        JSONArray a = HistoryStore.get(this);
        if (a.length() == 0) {
            TextView empty = new TextView(this);
            empty.setText("No checks yet. Your recent lookups will appear here.");
            empty.setTextColor(Color.LTGRAY);
            empty.setTextSize(14);
            empty.setPadding(0, dp(18), 0, 0);
            root.addView(empty);
        }

        for (int i = 0; i < a.length(); i++) {
            JSONObject o = a.optJSONObject(i);
            if (o == null) continue;
            long t = o.optLong("time", 0);
            String date = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(new Date(t));
            TextView item = new TextView(this);
            item.setText(o.optString("status", "UNKNOWN") + "  •  " + o.optString("number", "")
                + "\n" + o.optString("details", "") + "\n" + date);
            item.setTextColor(Color.WHITE);
            item.setTextSize(14);
            item.setPadding(dp(14), dp(14), dp(14), dp(14));
            item.setBackgroundResource(R.drawable.bg_card);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
            p.topMargin = dp(10);
            root.addView(item, p);
        }
        setContentView(root);
    }
}
