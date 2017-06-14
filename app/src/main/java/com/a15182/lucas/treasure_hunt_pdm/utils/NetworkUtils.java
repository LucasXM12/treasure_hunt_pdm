package com.a15182.lucas.treasure_hunt_pdm.utils;

import java.io.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;

import android.support.annotation.*;

public class NetworkUtils {

    @Nullable
    public static String getJSONFromAPI(String url, @NonNull List<NameValuePair> payload) {
        HttpPost httpPost = new HttpPost(url);
        HttpClient httpClient = new DefaultHttpClient();

        String ret = null;

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(payload));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (entity != null)
                ret = inputStreamToString(entity.getContent());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ret;
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

        String ret = buffer.toString();
        return ret;
    }
}
