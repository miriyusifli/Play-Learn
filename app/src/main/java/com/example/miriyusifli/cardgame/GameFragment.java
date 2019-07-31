package com.example.miriyusifli.cardgame;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.miriyusifli.cardgame.models.Card;
import com.example.miriyusifli.cardgame.models.DAO.Word;
import com.example.miriyusifli.cardgame.models.MyButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    private List<Word> vocablary;
    private GridLayout gridLayout;
    private String from, to;
    private ProgressBar progressBar;


    private Card[] cards;
    private Card[] azeriCards;
    private Card[] englishCards;
    private int cardsCount;
    private Card recentCard;
    private int chance = 3;
    private TextView chanceCountView;
    private Animation animation;

    private Typeface type;

    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridLayout = (GridLayout) view.findViewById(R.id.game_layout);

        chanceCountView = (TextView) view.findViewById(R.id.chance_count);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_bounce);
        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster.otf");
        chanceCountView.setTypeface(type);


        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        from = pref.getString("from", null); // getting String
        to = pref.getString("to", null);


        progressBar = (ProgressBar) view.findViewById(R.id.progress_game_page);

        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Words");

        databaseReference.orderByChild("subtopic_id").equalTo((double) CardGame.getUser().getSubtopic().getId()
        ).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        vocablary = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Word word = new Word();
                            word.setFrom((String) snapshot.child(from).getValue());
                            word.setTo((String) snapshot.child(to).getValue());
                            word.setId(Integer.parseInt(snapshot.getKey()));
                            vocablary.add(word);


                            if (vocablary.size() >= dataSnapshot.getChildrenCount()) {

                                createCards(vocablary);

                                mixCards();

                                progressBar.setVisibility(View.INVISIBLE);

                                addCardsToLayout(gridLayout);


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public Card[] getCards() {
        return cards;
    }

    public void mixCards() {
        for (int i = 0; i < azeriCards.length; i++) {
            int random = (int) (Math.random() * azeriCards.length);
            Card c = azeriCards[random];
            azeriCards[random] = azeriCards[i];
            azeriCards[i] = c;

        }


        for (int i = 0; i < englishCards.length; i++) {
            int random = (int) (Math.random() * englishCards.length);
            Card c = englishCards[random];
            englishCards[random] = englishCards[i];
            englishCards[i] = c;

        }

    }


    public void addCardsToLayout(GridLayout gridLayout) {
        for (int i = 0; i < cards.length / 2; i++) {


            gridLayout.addView(azeriCards[i]);
            gridLayout.addView(englishCards[i]);


        }

    }


    public Card[] createCards(List<Word> vocablary) {
        //get all words


        final Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.anim_shake);


        final int say = (vocablary.size()) * 2;


        cardsCount = say;


        cards = new Card[say];
        azeriCards = new Card[say / 2];
        englishCards = new Card[say / 2];
        recentCard = new Card(getActivity(), "", 0);
        for (int i = 0; i <= say - 1; i++) {
            if (i < say / 2) {
                cards[i] = new Card(getActivity(), vocablary.get(i).getTo(), vocablary.get(i).getId());
                englishCards[i] = cards[i];

            } else {
                cards[i] = new Card(getActivity(), vocablary.get(i - say / 2).getFrom(), vocablary.get(i - say / 2).getId());
                azeriCards[i - (say / 2)] = cards[i];
            }
            cards[i].setSizes(getActivity(), 2, 22);
            cards[i].setUniqueID(i + 1);


            cards[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Card card = (Card) v;
                    card.setGradientColor("#0099FF", "#0099FF", GradientDrawable.Orientation.LEFT_RIGHT);

                    if (recentCard.getUniqueID() != 0) {

                        if(recentCard.getUniqueID() == card.getUniqueID()){
                            recentCard.setGradientColor("#ffcc66", "#ffcc66", GradientDrawable.Orientation.LEFT_RIGHT);
                            card.setGradientColor("#ffcc66", "#ffcc66", GradientDrawable.Orientation.LEFT_RIGHT);
                            recentCard=null;

                        }

//eslesdiler
                        else if (recentCard.getKey() == card.getKey()) {

                            cardsCount = cardsCount - 2;
                            recentCard.setVisibility(INVISIBLE);
                            card.setVisibility(INVISIBLE);

                        }
                        //eslesmediler

                        else {
                            card.startAnimation(shake);
                            recentCard.startAnimation(shake);


                            chance--;
                            if (chance == 0) {
                                finishGame(false);
                            } else {
                                showChance();

                            }

                            card.setGradientColor("#ffcc66", "#ffcc66", GradientDrawable.Orientation.LEFT_RIGHT);
                            recentCard.setGradientColor("#ffcc66", "#ffcc66", GradientDrawable.Orientation.LEFT_RIGHT);

                        }
                        recentCard = new Card(getActivity(), "", 0);


                        if (cardsCount == 0) {

                            finishGame(true);

                        }


                    } else {

                        recentCard = card;

                    }


                }


            });

        }
        return cards;
    }


    private void finishGame(boolean winning) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("win", winning);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = new WinFragment();
        fragment.setArguments(bundle);

        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.choice_activity, fragment);
        fragmentTransaction.commit();
    }

    private void showChance() {
        chanceCountView.setText(String.valueOf(chance));
        chanceCountView.setTextSize(200);
        chanceCountView.setVisibility(View.VISIBLE);
        chanceCountView.startAnimation(animation);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                chanceCountView.setVisibility(INVISIBLE);
            }
        }, 2000);

    }
}
