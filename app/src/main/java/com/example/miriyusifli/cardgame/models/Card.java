package com.example.miriyusifli.cardgame.models;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import com.example.miriyusifli.cardgame.R;

import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.COLOR;

/**
 * Created by miriyusifli on 6/10/17.
 */

public class Card extends MyButton {
    private int uniqueID=0;
    private String word;
    private int key;

    public Card(Context context, String text, int key) {
        super(context);
        this.word = text;
        this.key = key;
        setText(text);
      //  setTextColor(getResources().getColor(R.color.White));
        setGradientColor("#ffcc66", "#ffcc66", GradientDrawable.Orientation.LEFT_RIGHT);


    }

    public int getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
