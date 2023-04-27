package com.example.ta_sinarmas;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class JSONRequest {
    private int method = -1;
    // Ganti url jika link api-mu berbeda
    private String url = "https://api.themoviedb.org/",
            path;
    private HashMap postData;
    public static final int
            HTTP_GET = 0,
            HTTP_POST = 1;
    public JSONRequest setPath(String path){
        this.path = path;
        return this;
    }
    public JSONRequest setUrl(String url){
        this.url = url;
        return this;
    }
    public JSONRequest setMethod(int method){
        this.method = method;
        return this;
    }
    public JSONRequest setData(HashMap postData){
        this.postData = postData;
        return this;
    }
    @NonNull
    private String getPostDataString(@NonNull HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    public JSONObject execute() {
        try {
            if(method == -1) throw new Exception("Missing method!");
            if(path == null) throw new Exception("Missing path!");

            URL api = new URL(url + (Character.valueOf('/').equals(url.charAt(url.length() - 1)) ? "" : "/") + path);
            HttpURLConnection conn = (HttpURLConnection) api.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            switch(method){
                case HTTP_GET:
                    conn.setRequestMethod("GET");
                    break;
                case HTTP_POST:
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postData));
                    writer.flush();
                    writer.close();
                    os.close();
                    break;
            }
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new Exception("Failed to connect to the server!");
            InputStream stream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            stream.close();

            return new JSONObject(builder.toString());
        } catch (Exception e){
            Log.w("JSON Request", e);
        }
        return null;
    }
}
