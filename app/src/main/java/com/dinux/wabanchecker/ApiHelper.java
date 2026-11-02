package com.dinux.wabanchecker;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiHelper {
    private static final String API =
        "https://wptest.udmodz-2ab.workers.dev/test?number=";

    public static JSONObject check(String number) throws Exception {
        HttpURLConnection c = null;
        try {
            URL url = new URL(API + URLEncoder.encode(number, "UTF-8"));
            c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setConnectTimeout(15000);
            c.setReadTimeout(20000);
            c.setRequestProperty("Accept", "application/json");
            int code = c.getResponseCode();
            InputStream in = (code >= 200 && code < 300) ?
                c.getInputStream() : c.getErrorStream();
            if (in == null) throw new Exception("Server returned HTTP " + code);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) body.append(line);
            reader.close();
            if (code < 200 || code >= 300) throw new Exception("API error (HTTP " + code + ")");
            JSONObject result = new JSONObject(body.toString());
            if (!result.has("status")) throw new Exception("Invalid API response");
            return result;
        } finally {
            if (c != null) c.disconnect();
        }
    }
}
