package com.example.miriyusifli.cardgame.models.DAO;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by miriyusifli on 6/18/17.
 */

public class Subtopic implements Serializable {
    int id;
    String name;
    String image_url;
    boolean learned;
    Bitmap image;



    public boolean isLearned() {
        return learned;
    }

    public void setLearned(int learned) {
        if(learned==0){this.learned=false;}
        else this.learned=true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public void setLearned(boolean learned) {
        this.learned = learned;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
