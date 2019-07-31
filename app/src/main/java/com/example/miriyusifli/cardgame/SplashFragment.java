package com.example.miriyusifli.cardgame;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.miriyusifli.cardgame.models.Card;
import com.example.miriyusifli.cardgame.models.DAO.Result;
import com.example.miriyusifli.cardgame.models.DAO.Topic;
import com.example.miriyusifli.cardgame.models.FireBaseRequestHandler;
import com.example.miriyusifli.cardgame.services.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {

    private ProgressBar progressBar;
    private int progress = 0;
    private List<Target> targets;

    private String from;
    private DatabaseReference databaseReference;

    private String to;
    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_splash, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isNetworkConnected()) {
            Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_LONG).show();
            return;

        }

       ActionBar actionBar= ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar!=null){
            actionBar.hide();
        }




        progressBar = (ProgressBar) view.findViewById(R.id.progress_splash);
        final FirebaseAuth auth = FirebaseAuth.getInstance();


        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String email = pref.getString("email", null);
        String pass = pref.getString("pass", null);


        if (CardGame.isSecured() || auth.getCurrentUser()!=null) {
            fetchData();
            fetchTopicResults();
        } else if (!Utils.isNull(email) && !Utils.isNull(pass)) {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful() && !Utils.isNull(auth.getCurrentUser()) && auth.getCurrentUser().isEmailVerified()) {
                        CardGame.setSecured(true);


                        final String user_id = auth.getCurrentUser().getUid();


                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();

                                from=(String) dataSnapshot.child("from").getValue();
                                to=(String) dataSnapshot.child("to").getValue();


                                editor.putString("id", user_id);
                                editor.putString("from",from); // Storing string
                                editor.putString("to", to);
                                editor.apply(); // commit changes



                                if (Utils.isNull(from) || Utils.isNull(to)) {
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.enter_from_right, R.anim.exit_to_left);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent,options.toBundle());

                                    return;
                                }

                                fetchData();
                                fetchTopicResults();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        } else {
                        getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit().clear().apply();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                        Fragment fragment = new LogInFragment();
                        fragmentTransaction.replace(R.id.first_activity, fragment);
                        fragmentTransaction.commit();

                    }

                }

            });


        } else {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = new LogInFragment();
            fragmentTransaction.replace(R.id.first_activity, fragment);
            fragmentTransaction.commit();
        }

    }


    private boolean isNetworkConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (null != connectivity && connectivity.getActiveNetworkInfo() != null && connectivity.getActiveNetworkInfo().isConnected() && connectivity.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED);
    }

    private void fetchData() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Topics");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                CardGame.setTopics(new ArrayList<Topic>());
                targets = new ArrayList<>();

                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax((int) dataSnapshot.getChildrenCount());


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    final Topic topic = new Topic();
                    topic.setName((String) snapshot.child("name").getValue());
                    topic.setImage_url((String) snapshot.child("image_url").getValue());
                    topic.setId(Long.parseLong(snapshot.getKey()));
                    CardGame.getTopics().add(topic);


                    Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            topic.setImage(bitmap);
                            progress++;
                            progressBar.setProgress(progress);

                            if (progress >= dataSnapshot.getChildrenCount()) {
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            Log.e("URL IS ",topic.getImage_url());
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };

                    targets.add(target);

                    CardGame.getPicasso().load(CardGame.storageURL+topic.getImage_url()).into(target);





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void fetchTopicResults() {

        SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String user_id = pref.getString("id", null);
        String from = pref.getString("from", null);
        String to = pref.getString("to", null);




        FirebaseDatabase.getInstance().getReference("Users").child(user_id).child(from + "-" + to).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                CardGame.setPoints(null);
                if(dataSnapshot.getValue()==null) return;

                    SparseArray<Integer> points = groupByTopic(dataSnapshot);




                CardGame.setPoints(points);








            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    private SparseArray<Integer> groupByTopic(DataSnapshot dataSnapshot){

        HashMap<Integer,List<Integer>> map=new HashMap<>();
        SparseArray<Integer>results=new SparseArray<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {



            int topic_id= ((Long)snapshot.child("topic_id").getValue()).intValue();
            int point= ((Long) snapshot.child("point").getValue()).intValue();

            if(map.containsKey(topic_id)){
                map.get(topic_id).add(point);
            }
            else {
                List<Integer>points=new ArrayList<>();
                points.add(point);
                map.put(topic_id,points);
            }


        }

       for(Map.Entry<Integer, List<Integer>> object:map.entrySet()){

            int point=0;
            for (Integer integer:object.getValue()){
                point=point+integer;
            }
            results.put(object.getKey(),point/object.getValue().size());

       }



        return results;
    }
}
