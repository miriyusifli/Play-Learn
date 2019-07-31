package com.example.miriyusifli.cardgame;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.miriyusifli.cardgame.services.AnimatedGifImageView;
import com.example.miriyusifli.cardgame.services.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.miriyusifli.cardgame.services.Utils.hideKeyboard;


public class Choose_language_Fragment extends Fragment {


    public Choose_language_Fragment() {
        // Required empty public constructor

    }

    private int i = 1;
    private int j = 4;
    private AnimatedGifImageView gifImageView_native;
    private AnimatedGifImageView gifImageView_foreign;
    private String from = null;
    private String to = null;
    private DatabaseReference databaseReference;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        /*SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        from = pref.getString("from", null);
        to = pref.getString("to", null);


        if (!Utils.isNull(from) && !Utils.isNull(to)) {
            if (CardGame.getTopics().size() == 0) {


                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new SplashFragment();
                fragmentTransaction.replace(R.id.first_activity, fragment);
                fragmentTransaction.commit();
                return;

            }
            Intent myIntent = new Intent(getActivity(), MainActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(myIntent);

            return;
        }
*/
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        final HashMap<Integer, Integer> flags = new HashMap<>();
        flags.put(1, R.drawable.azerbaijan_flag);
        flags.put(2, R.drawable.turkey_flag);
        flags.put(3, R.drawable.russia_flag);
        flags.put(4, R.drawable.united_kingdom_flag);


        gifImageView_native = (AnimatedGifImageView) view.findViewById(R.id.GifImageView_native);
        gifImageView_native.setAnimatedGif(R.drawable.azerbaijan_flag, AnimatedGifImageView.TYPE.AS_IS);


        gifImageView_foreign = (AnimatedGifImageView) view.findViewById(R.id.GifImageView_foreign);
        gifImageView_foreign.setAnimatedGif(R.drawable.united_kingdom_flag, AnimatedGifImageView.TYPE.AS_IS);


        Button back_native = (Button) view.findViewById(R.id.back_native);
        Button next_native = (Button) view.findViewById(R.id.next_native);


        Button back_foreign = (Button) view.findViewById(R.id.back_foreign);
        Button next_foreign = (Button) view.findViewById(R.id.next_foreign);


        back_native.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i--;
                if (i == j) i--;
                if (i <= 0) {
                    i = flags.size();
                    if (i == j) i--;

                }

                gifImageView_native.setAnimatedGif(flags.get(i), AnimatedGifImageView.TYPE.FIT_CENTER);


            }
        });

        next_native.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i++;
                if (i == j) i++;
                if (i > flags.size()) {
                    i = 1;
                    if (i == j) i++;

                }
                gifImageView_native.setAnimatedGif(flags.get(i), AnimatedGifImageView.TYPE.FIT_CENTER);


            }
        });


        back_foreign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                j--;
                if (i == j) j--;
                if (j <= 0) {
                    j = flags.size();
                    if (i == j) j--;

                }


                gifImageView_foreign.setAnimatedGif(flags.get(j), AnimatedGifImageView.TYPE.FIT_CENTER);


            }
        });

        next_foreign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j++;
                if (i == j) j++;
                if (j > flags.size()) {
                    j = 1;
                    if (i == j) j++;

                }
                gifImageView_foreign.setAnimatedGif(flags.get(j), AnimatedGifImageView.TYPE.FIT_CENTER);


            }
        });


        Button nextPage = (Button) view.findViewById(R.id.next_page);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                switch (i) {
                    case 1:
                        from = "azeri";
                        break;
                    case 2:
                        from = "turkish";
                        break;
                    case 3:
                        from = "russian";
                        break;
                    case 4:
                        from = "english";
                        break;
                }

                switch (j) {
                    case 1:
                        to = "azeri";
                        break;
                    case 2:
                        to = "turkish";
                        break;
                    case 3:
                        to = "russian";
                        break;
                    case 4:
                        to = "english";
                        break;
                }

                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("from", from);
                    jsonParam.put("to", to);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String id = pref.getString("id", null);

                DatabaseReference new_user = databaseReference.child(id);

                new_user.child("from").setValue(from);
                new_user.child("to").setValue(to);


                SharedPreferences.Editor editor = pref.edit();
                editor.putString("from", from); // Storing string
                editor.putString("to", to);
                editor.apply(); // commit changes


               /* if (CardGame.getTopics().size() == 0) {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment fragment = new SplashFragment();
                    fragmentTransaction.replace(R.id.first_activity, fragment);
                    fragmentTransaction.commit();
                    return;
                }*/
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment fragment = new SplashFragment();
                fragmentTransaction.replace(R.id.main_activity, fragment);
                fragmentTransaction.commit();

            }

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View choose_language = inflater.inflate(R.layout.fragment_choose_language_, container, false);

        hideKeyboard(getActivity());

        return choose_language;


    }


}