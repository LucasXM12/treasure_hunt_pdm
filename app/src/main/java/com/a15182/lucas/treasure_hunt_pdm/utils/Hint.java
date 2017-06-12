package com.a15182.lucas.treasure_hunt_pdm.utils;

import android.util.*;
import android.graphics.*;
import android.support.annotation.*;

public class Hint {
    private int id;
    private String dica;
    private Bitmap image;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDica() {
        return this.dica;
    }

    public void setDica(String dica) {
        this.dica = dica;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(@NonNull Bitmap image) {
        this.image = image;
    }

    public void setImage(@NonNull String image) {
        byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
        this.image = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public Hint(int id, String dica, @NonNull Bitmap image) {
        this.id = id;
        this.dica = dica;
        this.image = image;
    }

    public Hint(int id, String dica, @NonNull String image) {
        this.id = id;
        this.dica = dica;

        byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
        this.image = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
