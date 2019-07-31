package com.example.miriyusifli.cardgame;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.miriyusifli.cardgame.models.Card;
import com.example.miriyusifli.cardgame.models.DAO.Result;
import com.example.miriyusifli.cardgame.models.DAO.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.willy.ratingbar.ScaleRatingBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Result_Fragment extends Fragment {
    private float point;
    private ScaleRatingBar myRatingBar;

    public Result_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.fragment_result_, container, false);

        return resultView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myRatingBar = (ScaleRatingBar) getActivity().findViewById(R.id.ratingBar);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            point = bundle.getFloat("point", 0);

        }


        TextView timeTextView = (TextView) getActivity().findViewById(R.id.result_time);
        timeTextView.setText((int) point + " %");

        Drawable drawableTop;

        Button learn_again = (Button) getActivity().findViewById(R.id.learn_again);
        Button try_again = (Button) getActivity().findViewById(R.id.try_again);


        SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String id = pref.getString("id", null);
        String from = pref.getString("from", null);
        String to = pref.getString("to", null);


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(id);


        userRef.child(from + "-" + to).child(String.valueOf(CardGame.getUser().getSubtopic().getId()))
                .setValue(new Result(CardGame.getUser().getTopic().getId(), (int) point));

        myRatingBar.setRating((float) 5 * point / 100);
        myRatingBar.setEnabled(false);


        if (myRatingBar.getRating() > 3.5) {
            playWinMusic();
        } else playLoseMusic();

        drawableTop = getResources().getDrawable(R.drawable.exam);
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new TestFragment();
                //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.choice_activity, fragment);
                fragmentTransaction.commit();
            }
        });


        try_again.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);


        learn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new LearnFragment();
                //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.choice_activity, fragment);
                fragmentTransaction.commit();
            }
        });


        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bounce);
        //animation.setDuration(1000);

        myRatingBar.startAnimation(animation);

    }


    private void playWinMusic() {
        MediaPlayer mediaPlayer;

        mediaPlayer = MediaPlayer.create(getContext(), getResources().getIdentifier("win", "raw", getActivity().getPackageName()));

        mediaPlayer.start();
    }

    private void playLoseMusic() {
        MediaPlayer mediaPlayer;

        mediaPlayer = MediaPlayer.create(getContext(), getResources().getIdentifier("lose", "raw", getActivity().getPackageName()));

        mediaPlayer.start();
    }

}
