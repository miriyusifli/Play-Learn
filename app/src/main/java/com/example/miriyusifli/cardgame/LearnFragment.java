package com.example.miriyusifli.cardgame;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.miriyusifli.cardgame.models.DAO.Word;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LearnFragment extends Fragment {

    private int i;
    private long start;
    private int progress = 0;
    private ArrayList<Word> wordList;
    private List<Target> targets = new ArrayList<Target>();
    private ImageView imageView;
    private TextView wordView;
    private TextView descriptionView;
    private Button nextButton, prevButton;
    private String from, to;
    private ProgressBar progressBar;

    public LearnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_learn, container, false);

        i = 0;
        imageView = (ImageView) view.findViewById(R.id.imageView);
        wordView = (TextView) view.findViewById(R.id.test_textView);
        descriptionView = (TextView) view.findViewById(R.id.textView2);
        nextButton = (Button) view.findViewById(R.id.next);
        prevButton = (Button) view.findViewById(R.id.prev);
        //final ImageButton pronunciation = (ImageButton) findViewById(R.id.pronunciation);

        setAllViewInvisible();


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point screen = new Point();
        display.getSize(screen);
        params.height = screen.y / 3;
        imageView.setLayoutParams(params);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wordList = new ArrayList<>();


        progressBar = (ProgressBar) view.findViewById(R.id.progress_learn);

        progressBar.setVisibility(View.VISIBLE);


        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        from = pref.getString("from", null); // getting String
        to = pref.getString("to", null);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Words");


        databaseReference.orderByChild("subtopic_id").equalTo((double)CardGame.getUser().getSubtopic().getId()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final Word word = new Word();
                            word.setFrom((String) snapshot.child(from).getValue());
                            word.setTo((String) snapshot.child(to).getValue());
                            word.setImageURL((String) snapshot.child("image_url").getValue());
                            wordList.add(word);


                            Target target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    word.setImage(bitmap);
                                    progress++;

                                    if (progress >= dataSnapshot.getChildrenCount()) {

                                        progressBar.setVisibility(View.INVISIBLE);
                                        wordView.setText(wordList.get(0).getTo());
                                        descriptionView.setText(wordList.get(0).getFrom());
                                        imageView.setImageBitmap(wordList.get(0).getImage());

                                        setAllViewVisible();


                                    }

                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    //Here you should place a loading gif in the ImageView to
                                    //while image is being obtained.
                                }
                            };


                            targets.add(target);

                            CardGame.getPicasso().load(CardGame.storageURL+word.getImageURL()).into(target);



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;

                if (i == wordList.size() - 1) {
                    nextButton.setVisibility(View.INVISIBLE);
                }
                prevButton.setVisibility(View.VISIBLE);
                Word word = wordList.get(i);


                imageView.setImageBitmap(word.getImage());
                wordView.setText(word.getTo());
                descriptionView.setText(word.getFrom());
            }


        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i--;
                if (i == 0) {
                    prevButton.setVisibility(View.INVISIBLE);
                }

                nextButton.setVisibility(View.VISIBLE);
                Word word = wordList.get(i);

                imageView.setImageBitmap(word.getImage());
                wordView.setText(word.getTo());
                descriptionView.setText(word.getFrom());

            }
        });


       /* pronunciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Word word1 = wordList.get(i);
                final MediaPlayer mediaPlayer = MediaPlayer.create(LearnPage.this, getResources().getIdentifier("v" + String.valueOf(word1.getId()), "raw", getPackageName()));

                mediaPlayer.start();
            }
        });*/


    }

    private void setAllViewInvisible() {
        wordView.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        prevButton.setVisibility(View.INVISIBLE);
        descriptionView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        prevButton.setVisibility(View.INVISIBLE);


    }

    private void setAllViewVisible() {

        wordView.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        descriptionView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }
}
