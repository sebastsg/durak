package com.sgundersen.durak.net;

import android.content.res.Resources;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AsyncHttpTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    @Setter
    private static String serverPath;

    protected final Gson gson = new Gson();

    @Getter
    private static String cookies;

    private void setProperties(HttpURLConnection connection) {
        if (cookies != null && !cookies.isEmpty()) {
            connection.setRequestProperty("Cookie", cookies);
        }
    }

    private void writeAll(HttpURLConnection connection, String body) {
        if (body.isEmpty()) {
            return;
        }
        connection.setDoOutput(true);
        connection.setFixedLengthStreamingMode(body.length());
        connection.setRequestProperty("Content-Type", "application/json");
        try (OutputStream stream = connection.getOutputStream()) {
            stream.write(body.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.warn("Failed to write to stream: {}", e.getMessage());
        }
    }

    private void readCookies(HttpURLConnection connection) {
        // This is fine. There will never be a reason to have another cookie than JSESSIONID.
        String headerCookie = connection.getHeaderField("Set-Cookie");
        if (headerCookie != null) {
            cookies = headerCookie.substring(0, headerCookie.indexOf(';'));
        }
    }

    private String readAll(HttpURLConnection connection) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream stream = connection.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            readCookies(connection);
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    stringBuilder.append(line);
                } catch (IOException e) {
                    break;
                }
            }
        } catch (IOException e) {
            log.warn("Failed to read from stream: {}", e.getMessage());
        }
        return stringBuilder.toString();
    }

    private String request(String method, String path, String body){
        try {
            URL url = new URL(serverPath + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod(method);
            setProperties(connection);
            writeAll(connection, body);
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.error("Connection error. Status code: {} ({} {})", responseCode, method, path);
                return null;
            }
            return readAll(connection);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    protected String get(String path) {
        return request("GET", path, "");
    }

    protected String post(String path, String body) {
        return request("POST", path, body);
    }

    protected String post(String path) {
        return post(path, "");
    }

}
