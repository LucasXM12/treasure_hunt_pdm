package com.a15182.lucas.treasure_hunt_pdm.utils;

import org.json.*;

import java.util.*;

import android.support.annotation.*;

public class Utils {

    public Hint getInfo(@NonNull String url, @NonNull HashMap<String, String> payload) {
        return parseJson(NetworkUtils.getJSONFromAPI(url, payload));
    }

    @Nullable
    private Hint parseJson(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);

            int id = jsonObj.getInt("id");
            String dica = jsonObj.getString("dica");
            String image = jsonObj.getString("image");

            return new Hint(id, dica, image);
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }
}
