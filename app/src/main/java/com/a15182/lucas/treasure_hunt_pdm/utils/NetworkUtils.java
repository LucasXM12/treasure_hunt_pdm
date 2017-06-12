package com.a15182.lucas.treasure_hunt_pdm.utils;

import java.io.*;
import java.net.*;
import java.util.*;

import android.support.annotation.*;

public class NetworkUtils {

    @Nullable
    public static String getJSONFromAPI(String url, @NonNull HashMap<String, String> payload) {
        try {
            int respCod;
            InputStream is;
            URL apiEnd = new URL(url);
            HttpURLConnection connection;

            connection = (HttpURLConnection) apiEnd.openConnection();
            connection.setDoOutput(true);

            connection.setRequestProperty("action", payload.get("action"));
            connection.setRequestProperty("lat", payload.get("lat"));
            connection.setRequestProperty("lon", payload.get("lon"));
            connection.setRequestProperty("ra", payload.get("ra"));
            connection.setRequestProperty("dt", payload.get("dt"));
            connection.setRequestProperty("id", payload.get("id"));

            connection.setRequestMethod("PUT");

            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);

            connection.connect();

            respCod = connection.getResponseCode();

            if (respCod < HttpURLConnection.HTTP_BAD_REQUEST)
                is = connection.getInputStream();
            else
                is = connection.getErrorStream();

            String ret = inputStreamToString(is);
            is.close();

            connection.disconnect();

            return ret;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @NonNull
    private static String inputStreamToString(InputStream is) {
        StringBuffer buffer = new StringBuffer();

        try {
            String line;
            BufferedReader br;

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null)
                buffer.append(line);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }
}
