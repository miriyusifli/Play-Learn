package com.example.miriyusifli.cardgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ChoiceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(CardGame.getUser().getTopic().getName());
        getSupportActionBar().setSubtitle((CardGame.getUser().getSubtopic().getName()));


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        String choice = getIntent().getStringExtra("choice");
        switch (choice) {
            case "test":
                fragment = new TestFragment();
                break;
            case "game":
                fragment = new GameFragment();
                break;
            case "learn":
                fragment = new LearnFragment();
        }

        fragmentTransaction.replace(R.id.choice_activity, fragment);
        fragmentTransaction.commit();
    }

}
