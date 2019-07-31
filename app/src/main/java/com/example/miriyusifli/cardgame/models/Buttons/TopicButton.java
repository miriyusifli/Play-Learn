package com.example.miriyusifli.cardgame.models.Buttons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import com.example.miriyusifli.cardgame.CardGame;
import com.example.miriyusifli.cardgame.MainActivity;
import com.example.miriyusifli.cardgame.R;
import com.example.miriyusifli.cardgame.Subtopics_Fragment;
import com.example.miriyusifli.cardgame.models.DAO.Topic;

/**
 * Created by miriyusifli on 7/17/17.
 */

public class TopicButton extends RootTopicButton {


    public TopicButton(Context context, String text, float scale) {
        super(context, text, scale);

    }

    public void setOnClickEvent(final FragmentManager fragmentManager, final Topic topic) {
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CardGame.getUser().setTopic(topic);


                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Subtopics_Fragment subtopics_fragment = new Subtopics_Fragment();
                fragmentTransaction.replace(R.id.main_activity, subtopics_fragment);
                fragmentTransaction.addToBackStack("topics");
                fragmentTransaction.commit();

            }
        });


    }
}