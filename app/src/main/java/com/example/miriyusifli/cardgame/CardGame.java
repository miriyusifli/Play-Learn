package com.example.miriyusifli.cardgame;

import android.app.Application;
import android.util.SparseArray;

import com.example.miriyusifli.cardgame.models.DAO.Topic;
import com.example.miriyusifli.cardgame.models.DAO.User;
import com.example.miriyusifli.cardgame.models.FireBaseRequestHandler;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardGame extends Application {

    private static List<Topic> topics;
    private static boolean secured;
    private static User user = null;
    private static SparseArray<Integer> points;
    private static Picasso picasso;

    public static Picasso getPicasso() {
        return picasso;
    }

    public static final String storageURL="gs://playlearn-9f60e.appspot.com";

    public static List<Topic> getTopics() {
        return topics;
    }

    public static SparseArray<Integer> getPoints() {
        return points;
    }

    public static void setPoints(SparseArray<Integer> points) {
        CardGame.points = points;
    }

    public static void setTopics(List<Topic> topics) {
        CardGame.topics = topics;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        CardGame.user = user;
    }

    public static boolean isSecured() {
        return secured;
    }

    public static void setSecured(boolean secured) {
        CardGame.secured = secured;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        topics=new ArrayList<>();


        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
        Picasso.Builder builder=new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built=builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        picasso = new Picasso.Builder(this)
                .addRequestHandler(new FireBaseRequestHandler())
                .build();
    }
}
