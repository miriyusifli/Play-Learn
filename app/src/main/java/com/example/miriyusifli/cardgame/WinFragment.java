package com.example.miriyusifli.cardgame;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WinFragment extends Fragment {


    private Button try_again;
    private Button learn_again;


    public WinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_win, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView finish= (TextView) view.findViewById(R.id.textView5);
        boolean win=false;

        try_again= (Button) view.findViewById(R.id.try_again_game);
        learn_again= (Button) view.findViewById(R.id.learn_again_game);

        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new GameFragment();
                //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.choice_activity, fragment);
                fragmentTransaction.commit();
            }
        });


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



        Bundle bundle = this.getArguments();
        if (bundle != null) {
           win = bundle.getBoolean("win", false);
        }


        if(win)finish.setText("You Win");
        else finish.setText("You Lost");



    }
}
