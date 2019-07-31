package com.example.miriyusifli.cardgame;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.miriyusifli.cardgame.models.Buttons.SubtopicButton;
import com.example.miriyusifli.cardgame.models.DAO.Subtopic;
import com.example.miriyusifli.cardgame.services.Utils;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Subtopics_Fragment extends Fragment {

    private int imageWidth = 80;
    private int imageHeight = 80;
    private int progress = 0;
    private float scale;

    private List<Target> targets = new ArrayList<Target>();

    private GridLayout gridLayout;
    private Map<Integer, FrameLayout> subtopicButtons;
    private ProgressBar progressBar;


    public Subtopics_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View subtopicsView = inflater.inflate(R.layout.fragment_subtopics_, container, false);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(CardGame.getUser().getTopic().getName());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");


        progressBar = (ProgressBar) subtopicsView.findViewById(R.id.progress_topics);
        progressBar.setVisibility(View.VISIBLE);

        gridLayout = (GridLayout) subtopicsView.findViewById(R.id.subtopic_layout);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        scale = getResources().getDisplayMetrics().density;


        imageWidth = 80;
        imageHeight = 80;
        if (scale >= 2) {
            imageHeight = 150;
            imageWidth = 150;
        }
        gridLayout.setColumnCount((int) wi);

        return subtopicsView;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subtopicButtons = new TreeMap<>();


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Subtopics");

        databaseReference.orderByChild("topic_id").equalTo((double) CardGame.getUser().getTopic().getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    final Subtopic subtopic = new Subtopic();
                    subtopic.setName((String) snapshot.child("name").getValue());
                    subtopic.setImage_url((String) snapshot.child("image_url").getValue());
                    subtopic.setId(Integer.valueOf(snapshot.getKey()));


                    if (getActivity() == null) return;
                    FrameLayout frameLayout = new FrameLayout(getActivity());


                    final SubtopicButton subtopicButton = new SubtopicButton(getContext(), subtopic.getName(), scale);
                    subtopicButton.setEnabled(true);
                    subtopicButton.setResource();
                    subtopicButton.setOnClickEvent(getActivity().getSupportFragmentManager(), subtopic);


                    frameLayout.addView(subtopicButton);
                    subtopicButtons.put(subtopic.getId(), frameLayout);


                    Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            subtopicButton.setImage(bitmap, imageWidth, imageHeight);
                            progress++;


                            if (progress >= dataSnapshot.getChildrenCount()) {

                                progressBar.setVisibility(View.INVISIBLE);
                                gridLayout.removeAllViews();


                                for (Map.Entry<Integer, FrameLayout> entry : subtopicButtons.entrySet()) {
                                    gridLayout.addView(entry.getValue());
                                }


                            }

                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {


                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };


                    targets.add(target);


                    CardGame.getPicasso().load(CardGame.storageURL + subtopic.getImage_url()).into(target);


                }

                fetchSubtopicResult((int) CardGame.getUser().getTopic().getId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void fetchSubtopicResult(int id) {

        SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String user_id = pref.getString("id", null);
        String from = pref.getString("from", null);
        final String to = pref.getString("to", null);


        FirebaseDatabase.getInstance().getReference("Users").child(user_id).child(from + "-" + to).
                orderByChild("topic_id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    Button button = new Button(getContext());
                    button.setBackgroundResource(R.drawable.star);

                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) scale * 140, (int) scale * 140);

                    layoutParams.width = (int) (70 * scale);
                    layoutParams.height = (int) (70 * scale);
                    layoutParams.setMargins((int) (90 * scale), (int) (90 * scale), 0, 0);
                    button.setLayoutParams(layoutParams);

                    int key = Integer.valueOf(snapshot.getKey());


                    String point = String.valueOf(snapshot.child("point").getValue());
                    if (!Utils.isNull(point) && subtopicButtons.get(key) != null) {
                        button.setText(point);

                        subtopicButtons.get(key).addView(button);
                    }


                }


                boolean clickable = true;
                for (Map.Entry<Integer, FrameLayout> entry : subtopicButtons.entrySet()) {

                    if (!clickable) {

                        Button button = (Button) entry.getValue().getChildAt(0);

                        button.setBackgroundResource(R.drawable.disable_circle);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toasty.warning(getContext(), "You should get 90% from previous one.", Toast.LENGTH_SHORT, true).show();


                            }
                        });

                        //  button.setEnabled(false);
                    }
                    if (!exitsInDataSnapshot(dataSnapshot, entry.getKey())) {
                        clickable = false;
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    private boolean exitsInDataSnapshot(DataSnapshot dataSnapshot, int key) {

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            if (!Utils.isNull(snapshot.getKey())
                    &&
                    snapshot.getKey().equals(String.valueOf(key))
                    &&
                    snapshot.child("point").getValue() != null
                    &&
                    (Long) snapshot.child("point").getValue() > 90) return true;
        }
        return false;
    }




}