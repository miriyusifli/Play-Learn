package com.example.miriyusifli.cardgame;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Choose_Fragment extends Fragment {

    public Choose_Fragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button test=(Button)view.findViewById(R.id.test);
        Button play=(Button)view.findViewById(R.id.play);
        Button learn=(Button)view.findViewById(R.id.learn);

       /* final Result result1=subtopicDAO.getResult(MainActivity.user.getSubtopic().getId());
        if(result1.getTime()==0){
            result.setEnabled(false);
        }*/

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new GameFragment();
//                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.main_activity, fragment);
                fragmentTransaction.commit();
*/

                Intent intent=new Intent(getActivity(),ChoiceActivity.class);
                intent.putExtra("choice","game");
                startActivity(intent);

            }
        });


        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ChoiceActivity.class);
                intent.putExtra("choice","learn");
                startActivity(intent);


            }
        });


       /* result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChoosePage.this,ResultPage.class);
                user.setResult(result1);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
*/
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ChoiceActivity.class);
                intent.putExtra("choice","test");
                startActivity(intent);
            }
        });





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View chooseView=inflater.inflate(R.layout.fragment_choose_, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(CardGame.getUser().getTopic().getName());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle((CardGame.getUser().getSubtopic().getName()));

        return  chooseView;
    }

}
