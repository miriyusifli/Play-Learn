package com.example.miriyusifli.cardgame.models.DAO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by miriyusifli on 6/23/17.
 */

public class User implements Parcelable {
    private String id;
    private Topic topic;
    private Subtopic subtopic;
    private Result result;
    private String username;
    private String secret;
    private String email;

    public Result getResult() {
        if(this.result==null)this.result=new Result();

        return result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Subtopic getSubtopic() {
        return subtopic;
    }

    public void setSubtopic(Subtopic subtopic) {
        this.subtopic = subtopic;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.topic);
        dest.writeSerializable(this.subtopic);
        dest.writeSerializable(this.result);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(secret);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.topic = (Topic) in.readSerializable();
        this.subtopic = (Subtopic) in.readSerializable();
        this.result = in.readParcelable(Result.class.getClassLoader());
        this.email=in.readString();
        this.username=in.readString();
        this.secret=in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
