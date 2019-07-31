package com.example.miriyusifli.cardgame.models.Buttons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.GridLayout;

import com.example.miriyusifli.cardgame.CardGame;
import com.example.miriyusifli.cardgame.Choose_Fragment;
import com.example.miriyusifli.cardgame.MainActivity;
import com.example.miriyusifli.cardgame.R;
import com.example.miriyusifli.cardgame.models.DAO.Subtopic;

/**
 * Created by miriyusifli on 7/17/17.
 */

public class SubtopicButton extends RootTopicButton {


    public SubtopicButton(Context context, String text, float scale) {
        super(context, text, scale);
    }

    public void setOnClickEvent(final FragmentManager fragmentManager, final Subtopic subtopic) {
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CardGame.getUser().setSubtopic(subtopic);
                Choose_Fragment choose_fragment=new Choose_Fragment();
                fragmentTransaction.replace(R.id.main_activity,choose_fragment);
                fragmentTransaction.addToBackStack("subtopics");
                fragmentTransaction.commit();

            }
        });
    }
}