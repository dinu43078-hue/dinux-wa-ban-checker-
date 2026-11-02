package com.dinux.wabanchecker;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends Activity {
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(28, 28, 28, 28);
        root.setBackgroundColor(Color.rgb(11,20,26));

        TextView logo = new TextView(this);
        logo.setText("DIN​​UX");
        logo.setTextSize(36);
        logo.setTextColor(Color.rgb(37,211,102));
        logo.setTypeface(null, 1);
        logo.setGravity(Gravity.CENTER);

        TextView name = new TextView(this);
        name.setText("WA BAN CHECKER");
        name.setTextSize(21);
        name.setTextColor(Color.WHITE);
        name.setGravity(Gravity.CENTER);

        TextView info = new TextView(this);
        info.setText("Version 1.0.0\\n\\nA native Android number status lookup app.\\n\\nDeveloped by DINUX.\\n\\nDISCLAIMER\\nThis app displays data returned by an unofficial third-party API. It is not affiliated with, endorsed by, or operated by WhatsApp. Results may be inaccurate or unavailable.");
        info.setTextSize(14);
        info.setTextColor(Color.LTGRAY);
        info.setGravity(Gravity.CENTER);
        info.setPadding(0, 24, 0, 0);

        root.addView(logo);
        root.addView(name);
        root.addView(info);
        setContentView(root);
    }
}
