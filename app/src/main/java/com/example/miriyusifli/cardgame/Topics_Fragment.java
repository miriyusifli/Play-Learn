package com.example.miriyusifli.cardgame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miriyusifli.cardgame.models.Buttons.TopicButton;
import com.example.miriyusifli.cardgame.models.Card;
import com.example.miriyusifli.cardgame.models.DAO.Topic;
import com.example.miriyusifli.cardgame.models.DAO.User;
import com.example.miriyusifli.cardgame.services.Utils;


public class Topics_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private GridLayout gridLayout;
    private int imageWidth;
    private int imageHeight;
    private float scale;
    private double wi;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridLayout = (GridLayout) view.findViewById(R.id.topic_layout);

        CardGame.setUser(new User());


        for (int i = 0; i < CardGame.getTopics().size(); i++) {

            Topic topic = CardGame.getTopics().get(i);
            TopicButton topicButton = new TopicButton(gridLayout.getContext(), topic.getName(), scale);
            topicButton.setOnClickEvent(getActivity().getSupportFragmentManager(), topic);


            if (!Utils.isNull(topic.getImage())) {
                topicButton.setImage(topic.getImage(), imageWidth, imageHeight);
            }

            FrameLayout frameLayout = new FrameLayout(getContext());
            Button button = new Button(getContext());
            button.setBackgroundResource(R.drawable.star);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) scale * 140, (int) scale * 140);


            layoutParams.width = (int) (70 * scale);
            layoutParams.height = (int) (70 * scale);
            layoutParams.setMargins((int) (90 * scale), (int) (90 * scale), 0, 0);


            button.setLayoutParams(layoutParams);
            frameLayout.addView(topicButton);


            if (!Utils.isNull(CardGame.getPoints()))

            {

                button.setText(String.valueOf(
                        CardGame.getPoints().get(
                                ((Long) topic.getId()).intValue(), 0)));

                if (!button.getText().equals("0"))
                    frameLayout.addView(button);


            }

            gridLayout.addView(frameLayout);

            if(gridLayout.getColumnCount()%2==1) {


                Space space = new Space(getContext());
                FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams((int) scale * 140, (int) scale * 140);


                layoutParams1.width = (int) (70 * scale);
                layoutParams1.height = (int) (70 * scale);
                layoutParams1.setMargins((int) (90 * scale), (int) (90 * scale), (int) (90 * scale), 0);

                space.setLayoutParams(layoutParams1);
                gridLayout.addView(space);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View topicsView = inflater.inflate(R.layout.fragment_topics_, container, false);
        gridLayout = (GridLayout) topicsView.findViewById(R.id.topic_layout);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Topics");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");


        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int dens = dm.densityDpi;
         wi = (double) width / (double) dens;
        scale = getResources().getDisplayMetrics().density;


        imageWidth = 80;
        imageHeight = 80;
        if (scale >= 2) {
            imageHeight = 150;
            imageWidth = 150;
        }


        gridLayout.setColumnCount((int) wi);




        return topicsView;
    }


}
