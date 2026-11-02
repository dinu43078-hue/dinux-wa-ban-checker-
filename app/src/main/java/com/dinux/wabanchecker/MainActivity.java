package com.dinux.wabanchecker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private EditText country, phone;
    private TextView status, numberText, details, account, resultLabel;
    private View resultPanel;
    private ProgressDialog dialog;
    private String copyText = "";

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        country = (EditText)findViewById(R.id.countryCode);
        phone = (EditText)findViewById(R.id.phoneNumber);
        status = (TextView)findViewById(R.id.resultStatus);
        numberText = (TextView)findViewById(R.id.resultNumber);
        details = (TextView)findViewById(R.id.resultDetails);
        account = (TextView)findViewById(R.id.resultAccount);
        resultLabel = (TextView)findViewById(R.id.resultLabel);
        resultPanel = findViewById(R.id.resultPanel);

        findViewById(R.id.checkButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { checkNumber(); }
        });
        findViewById(R.id.clearButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                phone.setText("");
                resultPanel.setVisibility(View.GONE);
                phone.requestFocus();
            }
        });
        findViewById(R.id.copyButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { copyResult(); }
        });
        findViewById(R.id.historyButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
        findViewById(R.id.aboutButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });
    }

    private void checkNumber() {
        String cc = country.getText().toString().trim().replace("+", "").replaceAll("[^0-9]", "");
        String p = phone.getText().toString().trim().replaceAll("[^0-9]", "");
        if (cc.length() < 1 || cc.length() > 4 || p.length() < 5) {
            Toast.makeText(this, "Enter a valid country code and phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        final String full = cc + p;
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
            .hideSoftInputFromWindow(phone.getWindowToken(), 0);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Checking number securely...");
        dialog.setCancelable(false);
        dialog.show();

        new Thread(new Runnable() {
            public void run() {
                try {
                    final JSONObject data = ApiHelper.check(full);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (dialog != null && dialog.isShowing()) dialog.dismiss();
                            render(data);
                            HistoryStore.add(MainActivity.this, data);
                        }
                    });
                } catch (final Exception ex) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (dialog != null && dialog.isShowing()) dialog.dismiss();
                            Toast.makeText(MainActivity.this,
                                "Unable to check: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void render(JSONObject data) {
        String s = data.optString("status", "UNKNOWN").toUpperCase();
        status.setText(s);
        if ("ACTIVE".equals(s)) {
            status.setTextColor(Color.rgb(37, 211, 102));
            resultLabel.setText("NUMBER LOOKUP RESULT");
        } else if ("BANNED".equals(s)) {
            status.setTextColor(Color.rgb(255, 91, 91));
            resultLabel.setText("NUMBER LOOKUP RESULT");
        } else {
            status.setTextColor(Color.rgb(255, 193, 7));
            resultLabel.setText("UNRECOGNIZED RESPONSE");
        }
        numberText.setText(data.optString("number", "—"));
        details.setText(data.optString("details", "No details provided by API"));
        account.setText("Account type: " + data.optString("accountType", "Unknown")
            + "     •     CC: " + data.optString("cc", "—"));
        copyText = "DIN​​UX WA BAN CHECKER\nStatus: " + s + "\nNumber: "
            + data.optString("number", "") + "\nDetails: "
            + data.optString("details", "") + "\nAccount type: "
            + data.optString("accountType", "");
        resultPanel.setVisibility(View.VISIBLE);
    }

    private void copyResult() {
        if (copyText.length() == 0) return;
        ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("Dinux check result", copyText));
        Toast.makeText(this, "Result copied", Toast.LENGTH_SHORT).show();
    }
}
