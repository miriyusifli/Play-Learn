package com.example.miriyusifli.cardgame.models.DAO;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by miriyusifli on 6/17/17.
 */

public class Topic implements Serializable {

    long id;
    String name;
    String image_url;
    Bitmap image;






    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
