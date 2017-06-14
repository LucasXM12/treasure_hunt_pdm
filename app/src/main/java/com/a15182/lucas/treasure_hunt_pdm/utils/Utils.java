package com.a15182.lucas.treasure_hunt_pdm.utils;

import org.json.*;
import org.apache.http.*;

import java.util.*;

import android.support.annotation.*;

public class Utils {

    public static Hint getInfo(@NonNull String url, @NonNull List<NameValuePair> payload) {
        return parseJson(NetworkUtils.getJSONFromAPI(url, payload));
    }

    @Nullable
    private static Hint parseJson(String json) {
        try {
            if (json != null) {
                JSONObject jsonObj = new JSONObject(json);

                int id = jsonObj.getInt("id");
                String dica = jsonObj.getString("dica");
                String image = jsonObj.getString("image");

                return new Hint(id, dica, image);
            } else
                return null;
        } catch (Exception e) {
            return null;
        }
    }
}
