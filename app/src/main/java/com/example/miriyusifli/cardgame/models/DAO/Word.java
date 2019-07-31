package com.example.miriyusifli.cardgame.models.DAO;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by miriyusifli on 6/14/17.
 */

public class Word implements Parcelable {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    String from;
    String to;
    String imageURL;
    Bitmap image;


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeString(this.imageURL);
        //dest.writeB(this.image);
    }

    public Word() {
    }

    protected Word(Parcel in) {
        this.id = in.readInt();
        this.to = in.readString();
        this.from = in.readString();
        this.imageURL = in.readString();
       // this.image = in.createByteArray();
    }

    public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel source) {
            return new Word(source);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };
}
