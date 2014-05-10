package com.pwr.pdw.app.api.helpers;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created
 */
public class ConnectionHelper {
    public static final int TIMEOUT=10000;
    public static String postRequest( String url, List<NameValuePair> params) throws IOException {

        URL reqURL = new URL(url + getQuery(params));
        Log.i("PWD", "url " + url + getQuery(params));

        HttpURLConnection request = (HttpURLConnection) (reqURL.openConnection());
        request.setReadTimeout(TIMEOUT);
        request.setConnectTimeout(TIMEOUT);
        request.setDoInput(true);
        request.setDoOutput(true);
        request.setUseCaches(false);
        request.setRequestMethod("POST");

        request.connect();

        String resp = "";
        try {
            InputStream in = new BufferedInputStream(request.getInputStream());
            resp = responseToString(in);
        } catch (IOException e) {
            InputStream in = new BufferedInputStream(request.getErrorStream());
            resp = responseToString(in);
        } finally {
            request.disconnect();
        }
        Log.i("PWD", "api response: " + resp);

        return resp;
    }

    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (pair.getValue() != null) {
                if (first) {
                    result.append("?");
                    first = false;
                } else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }
        }

        return result.toString();
    }

    private static String responseToString(InputStream is) {
        String result = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
