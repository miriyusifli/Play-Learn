package com.example.miriyusifli.cardgame.models.DAO;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by miriyusifli on 7/8/17.
 */

public class Result implements Serializable {

   private int point;
   private long topic_id;


    public Result( long topic_id,int point) {
        this.point = point;
        this.topic_id = topic_id;
    }

    public long getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(long topic_id) {
        this.topic_id = topic_id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }





    public Result() {
    }



}
