package com.example.miriyusifli.cardgame;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {


    int showClickCounter, progress, point, i = 0;
    Word word1;
    ImageView imageView;
    TextView wordView;
    EditText editText;
    MediaPlayer mediaPlayer;
    Handler handler;
    ArrayList<Word> wordList;
    List<Target> targets = new ArrayList<Target>();
    Button skip, show;
    ProgressBar progressBar;
    String from, to;
    TextWatcher textWatcher;

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test, container, false);


        mediaPlayer = MediaPlayer.create(getContext(), getResources().getIdentifier("correct_answer", "raw", getActivity().getPackageName()));
        handler = new Handler();
        wordList = new ArrayList<>();
        imageView = (ImageView) view.findViewById(R.id.test_imageView);
        wordView = (TextView) view.findViewById(R.id.test_textView);
        editText = (EditText) view.findViewById(R.id.test_editText);
        skip = (Button) view.findViewById(R.id.button_skip);
        show = (Button) view.findViewById(R.id.button_show);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_test);
        progressBar.setVisibility(View.VISIBLE);


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
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        from = pref.getString("from", null); // getting String
        to = pref.getString("to", null);

        progressBar= (ProgressBar) view.findViewById(R.id.progress_test_page);
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Words");
        databaseReference.keepSynced(true);


        databaseReference.orderByChild("subtopic_id").equalTo((double)CardGame.getUser().getSubtopic().getId()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        Log.e("IMAGE COUNT IS ", String.valueOf(dataSnapshot.getChildrenCount()));
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final Word word = new Word();
                            word.setFrom(snapshot.child(from).getValue().toString());
                            word.setTo(snapshot.child(to).getValue().toString());
                            word.setImageURL(snapshot.child("image_url").getValue().toString());
                            wordList.add(word);


                            Target target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    word.setImage(bitmap);
                                    progress++;

                                    if (progress >= dataSnapshot.getChildrenCount()) {
                                        Collections.shuffle(wordList);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        word1 = wordList.get(0);
                                        imageView.setImageBitmap(wordList.get(0).getImage());
                                        wordView.setText(wordList.get(0).getFrom());

                                        setAllViewVisible();


                                    }

                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    Log.e("FAILED FILE  IS ",word.getImageURL());


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


        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, final int start, int before, int count) {


                if (word1.getTo().trim().equalsIgnoreCase(String.valueOf(s).trim())) {
                    point++;
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    }
                    mediaPlayer.start();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            i++;
                            if (i < wordList.size()) {
                                next();
                            } else {
                                end();
                            }


                        }


                    }, 300);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        };


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showClickCounter == 0) {
                    editText.removeTextChangedListener(textWatcher);

                    String english = word1.getTo();
                    SpannableString redSpannable = new SpannableString(english);


                    for (int j = 0; j < english.length(); j++) {


                        if (editText.getText().length() <= j || editText.getText().charAt(j) != english.charAt(j)) {
                            redSpannable.setSpan(new ForegroundColorSpan(Color.RED), j, j + 1, 0);
                        }
                    }

                    editText.setText(redSpannable);
                    showClickCounter++;
                    editText.addTextChangedListener(textWatcher);
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if (i < wordList.size()) {
                    next();
                } else {
                    end();

                }


            }
        });

        editText.addTextChangedListener(textWatcher);
    }

    private void setAllViewInvisible() {
        imageView.setVisibility(View.INVISIBLE);
        wordView.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.INVISIBLE);
        skip.setVisibility(View.INVISIBLE);
        show.setVisibility(View.INVISIBLE);
    }

    private void setAllViewVisible() {

        imageView.setVisibility(View.VISIBLE);
        wordView.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        skip.setVisibility(View.VISIBLE);
        show.setVisibility(View.VISIBLE);
    }

    private void next() {
        word1 = wordList.get(i);
        imageView.setImageBitmap(word1.getImage());
        wordView.setText(word1.getFrom());
        editText.setText("");
        showClickCounter = 0;

    }

    private void end() {





        Bundle bundle = new Bundle();
        bundle.putFloat("point", (int) Math.ceil((float) point / wordList.size() * 100));

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = new Result_Fragment();
        fragment.setArguments(bundle);

        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.choice_activity, fragment);
        fragmentTransaction.commit();




    }

}
